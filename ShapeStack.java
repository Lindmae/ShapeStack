/*
 * CS342 HW01 01/19/2017
 * Isabel Lindmae (ilindm2)
 *
 * Description:
 *      Program that implements the use of buttons, objects, and a GUI. The program spawns either rectangles or polygons where the user clicks and has the object fall to the
 *      bottom of the screen. If rectangles are created then they will stack on each other. Polygons will not stack unless dropped directly ontop of each other. The user can click buttons found on the right side to either change the shape, change the radius of the polygons, or change the number of sides
 *      for the polygons. 15 objects can be created at max (either polygons or rectangles but not both). Once 15 objects are created, the screen is wiped.
 * Sources:
 *      sample code by Steve Huang
 *      MKyong.com - How to delay in Java (https://www.mkyong.com/java/java-how-to-delay-few-seconds/) was referenced to have a pause when the ‘gravity’ function is called on objects
 *      cs.usfca.edu - Java Arrays of Objects (http://www.cs.usfca.edu/~wolber/courses/110s/lectures/ArrayOfObjs.htm) was used as a reference for building arrays of objects
 *
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class ShapeStack extends JFrame implements ActionListener{
    static JButton  closeButton = null;
    static JButton  shapeButton = null;
    static JButton  reduceRadiusButton = null;
    static JButton  increaseRadiusButton = null;
    static JButton  increaseSidesButton = null;
    static JButton  decreaseSidesButton = null;
    
    static JPanel mjp = null;
    static JPanel sjp = null;
    static Graphics gg = null;
    private MyCanvas canvas = null;
    
    static int radius;
    static int sides;
    
    public static void main(String[] args) {
        ShapeStack dm = new ShapeStack();
    }
    
    public ShapeStack() {
        radius = 10;
        sides = 8;
        closeButton = new JButton("close");
        closeButton.addActionListener(this);
        
        mjp = new JPanel();
        mjp.setBackground(Color.black);
        mjp.setBounds(0, 0, 400, 300);
        mjp.setSize(400,300);
        
        sjp = new JPanel();
        sjp.add(closeButton);
        sjp.setBackground(Color.gray);
        sjp.setBounds(300, 0, 400, 300);
        sjp.setSize(100,300);
        
        increaseRadiusButton = new JButton("+radius");
        increaseRadiusButton.addActionListener(this);
        sjp.add(increaseRadiusButton);
        
        reduceRadiusButton = new JButton("-radius");
        reduceRadiusButton.addActionListener(this);
        sjp.add(reduceRadiusButton);
        
        increaseSidesButton = new JButton("+sides");
        increaseSidesButton.addActionListener(this);
        sjp.add(increaseSidesButton);
        
        decreaseSidesButton = new JButton("-sides");
        decreaseSidesButton.addActionListener(this);
        sjp.add(decreaseSidesButton);
        
        shapeButton = new JButton("Change shape");
        shapeButton.addActionListener(this);
        sjp.add(shapeButton);
        
        this.setTitle("ShapeStack");
        this.setSize(400, 300);
        this.getContentPane().setBackground(Color.BLACK);
        this.add(sjp);
        this.add(mjp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        
        canvas = new MyCanvas();
        this.add(canvas);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "close"){
            System.exit(0);
        }
        
        if(e.getActionCommand() == "-radius"){
            radius--;
            canvas.increaseRadius(radius);
        }
        
        if(e.getActionCommand() == "+radius"){
            radius++;
            canvas.increaseRadius(radius);
        }
        
        if(e.getActionCommand() == "-sides"){
            sides--;
            canvas.increaseTess(sides);
        }
        
        if(e.getActionCommand() == "+sides"){
            sides++;
            canvas.increaseTess(sides);
        }
        
        if(e.getActionCommand() == "Change shape"){
            canvas.resetMyCanvas();
        }
    }
    
}

class MyCanvas extends JPanel implements MouseListener{
    private int x,y;
    private int count;
    private myPolygon[] objects;
    private myRectangle[] rectangles;
    private myRectangle canvas;
    private myRectangle topcanvas;
    private int type;
    
    public MyCanvas() {
        this.addMouseListener(this);
        this.setBackground(Color.black);
        this.setSize(400,300);
        canvas = new myRectangle(300,300);
        canvas.setVerticies();
        canvas.setCords();
        topcanvas = new myRectangle(100,100);
        topcanvas.setVerticies();
        topcanvas.setCords();
        x = 0;
        y = 0;
        count = 0;
        type = 1; //odd values are set as rectangles, even set to polygons
        objects = new myPolygon[15];
        for(int i = 0; i < 15 ; i++){
            objects[i] = new myPolygon();
            objects[i].setVerticies(8);
            objects[i].setRadius(10);
            objects[i].setCords(0,0);
            objects[i].movePolygon(x,y);
        }
        rectangles = new myRectangle[15];
        for(int i = 0; i < 15 ; i++){
            rectangles[i] = new myRectangle(10,5);
            rectangles[i].setVerticies();
            rectangles[i].setCords();
            rectangles[i].movePolygon(x,y);
        }
    }
    
    public void resetMyCanvas() {
        Graphics gg = getGraphics();
        canvas.fillmyPolygon(gg, Color.black);
        resetPolygons(gg);
        count = 0;
        type++;
    }
    
    public void resetTopCanvas() {
        Graphics gg = getGraphics();
        topcanvas.fillmyPolygon(gg, Color.black);
    }
    
    public void increaseRadius(int givenRadius){
        Graphics g = getGraphics();
        for(int i = 0; i < 15 ; i++){
            objects[i].fillmyPolygon(g,Color.black);
            objects[i].setRadius(givenRadius);
            int xCenter = objects[i].getXcenter();
            int yCenter = objects[i].getYcenter();
            objects[i].setCords(xCenter, yCenter );
            objects[i].fillmyPolygon(g,Color.red);
//            objects[i].applyGravity(g,objects,count);
        }
//        objects[0].applyGravity(g,objects,count);
        resetTopCanvas();
    }
    
    public void increaseTess(int givenTess){
        Graphics g = getGraphics();
        for(int i = 0; i < 15 ; i++){
            objects[i].fillmyPolygon(g,Color.black);
            objects[i].setVerticies(givenTess);
            int xCenter = objects[i].getXcenter();
            int yCenter = objects[i].getYcenter();
            objects[i].setCords(xCenter, yCenter );
            objects[i].fillmyPolygon(g,Color.red);
        }
        resetTopCanvas();
    }
    
    public void resetPolygons(Graphics g){
        for(int i = 0; i < 15; i++){
            objects[i].fillmyPolygon(g,Color.black);
            objects[i].setCords(0,0);
            rectangles[i].fillmyPolygon(g,Color.black);
            rectangles[i].setCords();
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Graphics gg = getGraphics();
        x = e.getX();
        y = e.getY();
        
        if(count < 14 )
            count++;
        else{
            count = 0;
            resetPolygons(gg);
        }
        
        if(type % 2 == 0){
            objects[count].movePolygon(x,y);
            objects[count].fillmyPolygon(gg, Color.red);
            objects[count].applyGravity(gg, objects,count);
        }
        else {
            rectangles[count].movePolygon(x,y);
            rectangles[count].fillmyPolygon(gg, Color.blue);
            rectangles[count].applyGravity(gg, rectangles, count);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}

class myPolygon {
    private int verticies;
    private int xCord[];
    private int yCord[];
    private int radius;
    private int xCenter;
    private int yCenter;
    
    public myPolygon(){
        xCord = new int[4];
        yCord = new int[4];
    }
    
    public void setVerticies(int value){
        verticies = value;
        xCord = new int[verticies];
        yCord = new int[verticies];
    }
    
    public void setRadius(int value){
        radius = value;
    }
    
    public void setCords(int givenX, int givenY){
        xCenter = givenX;
        yCenter = givenY;
        
        for(int i = 0; i < verticies ; i++){
            double xint = radius * Math.cos(i * ((2*Math.PI)/verticies));
            xCord[i] = (int)xint + xCenter;
            
            double yint = radius * Math.sin(i * ((2*Math.PI)/verticies));
            yCord[i] = (int)yint + yCenter;
        }
    }
    
    public void movePolygon(int newX, int newY){
        xCenter = newX;
        yCenter = newY;
        
        for(int i = 0; i < verticies; i++){
            xCord[i] += newX;
            yCord[i] += newY;
        }
    }
    
    
    public int getVerticies(){
        return verticies;
    }
    
    public int[] getxCord(){
        return xCord;
    }
    
    public int[] getyCord(){
        return yCord;
    }
    
    public int getXcenter(){
        return xCenter;
    }
    
    public int getYcenter(){
        return yCenter;
    }
    
    public void fillmyPolygon(Graphics g, Color color){
        g.setColor(color);
        g.fillPolygon(xCord, yCord, verticies);
    }
    
    public boolean checkCollisions(myPolygon[] objects, int count){
        for(int i = 0 ; i < verticies ; i++){
            if(yCord[i] > 270){
                return false;
            }
        }
        for(int i = 0; i < 15; i++){
            if(((Math.pow(objects[count].xCenter - objects[i].xCenter,2)) + (Math.pow(objects[count].yCenter - objects[i].yCenter,2))) == (4*Math.pow(radius,2))){
                return false;
            }
        }
        return true;
    }
    
    public void applyGravity(Graphics g, myPolygon[] objects, int count){
        while(checkCollisions(objects,count)){
            fillmyPolygon(g,Color.black);
            for(int i= 0; i < verticies; i++){
                yCord[i]++;
            }
            yCenter++;
            fillmyPolygon(g,Color.red);
            try {                       //MKyong.com used as reference
                Thread.sleep(10);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
}

class myRectangle extends myPolygon{
    
    private int length;
    private int width;
    private int verticies;
    private int xCord[];
    private int yCord[];
    private int radius;
    private int xCenter;
    private int yCenter;
    
    public myRectangle(int Width, int Length){
        xCord = new int[4];
        yCord = new int[4];
        width = Width;
        length = Length;
    }
    
    public void setCords(){
        xCenter = 0;
        yCenter = 0;
        
        xCord[0] = xCenter - width;
        yCord[0] = xCenter - length;
        
        xCord[1] = xCenter + width;
        yCord[1] = xCenter - length;
        
        xCord[2] = xCenter + width;
        yCord[2] = xCenter + length;
        
        xCord[3] = xCenter - width;
        yCord[3] = xCenter + length;
    }
    
    public void setVerticies(){
        verticies = 4;
        xCord = new int[verticies];
        yCord = new int[verticies];
    }
    
    public void movePolygon(int newX, int newY){
        xCenter = newX;
        yCenter = newY;
        
        for(int i = 0; i < verticies; i++){
            xCord[i] += newX;
            yCord[i] += newY;
        }
    }
    
    public void fillmyPolygon(Graphics g, Color color){
        g.setColor(color);
        g.fillPolygon(xCord, yCord, verticies);
    }
    
    
    public boolean checkCollisions(myRectangle[] objects, int count){
        //collisions with bottom of canvas
        for(int i = 0 ; i < verticies ; i++){
            if(yCord[i] > 270){
                return false;
            }
        }
        //collisions with other rectangles
        for(int i =0; i < 15; i++){
            if(objects[i].yCenter - objects[count].yCenter == length * 2){
                if(objects[count].xCenter > objects[i].xCenter){
                    if(objects[count].xCenter - objects[i].xCenter < width * 2){
                        return false;
                    }
                }
                else if(objects[count].xCenter < objects[i].xCenter){
                    if(objects[i].xCenter - objects[count].xCenter < width * 2){
                        return false;
                    }
                }
            }
        }
        return true; //no collisions
    }
    
    public void applyGravity(Graphics g, myRectangle[] objects, int current){
        while(checkCollisions(objects,current)){
            fillmyPolygon(g,Color.black);
            for(int i= 0; i < verticies; i++){
                yCord[i]++;
            }
            yCenter++;
            fillmyPolygon(g,Color.blue);
            try {                           //MKyong.com used as reference
                Thread.sleep(10);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
}
