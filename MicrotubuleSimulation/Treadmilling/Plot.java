package treadmilling;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JApplet;
import javax.swing.JFrame;

import javax.swing.JComponent;
import java.io.*;
import static java.lang.Math.abs;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Vector;


public class Plot{
    /// this class creates plots for different values in the simulation

    public Plot(){}

    public void draw(String p, int simnum){
    
    
    

	try {

            FileInputStream fstream = new FileInputStream(p+"/aveLength"+simnum+".dat"); // read in the data file
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream)); // use the data file to create a buffered reader
            String strLine; // the line of the data file
            String delimiter = "[ ]+"; // the delimeter used for the data files. in this case, the delimiter is a series of whitespace
            double xmax = 0.0, ymax = 0.0; // largest value on x-axis and y-axis

            while ( (strLine = br.readLine()) != null){ // while there is still values in the data file

                String strTmp[]; // establish the string 
                strTmp = strLine.split(delimiter); // split the string b y the delimiter
                double x2 = Double.parseDouble(strTmp[0]); // parse the x value from the split string
                double y2 = Double.parseDouble(strTmp[1]); // parse the y value  from the split string
                xmax = x2; // set the x2 value to the the most recent valiue (x value is only going to go up
                if (ymax < y2) ymax = y2;// if the nrewly read in value is the highest, save it as the ymax value

            }
            ymax = ymax+150.0; // this is important for scaling the graph, but I dont really understand the motivation behind it for this graph
            ymax = Math.ceil(ymax/50.0)*50.0; // i dont really understand, but it works with this so Im leaving it in.
            xmax = Math.ceil(xmax/50.0)*50.0;

        
            int width = 1100, height = 1100; // establish the width and the height of the 
	    
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED); // create the buffered image 

	    Graphics2D ig2 = bi.createGraphics(); // create some graphics using graphics 2d
            ig2.setColor(Color.WHITE); // change to white
            ig2.fillRect(0, 0, (int)width, (int)height); // draw the background
            ig2.setColor(Color.BLACK); // set the color to black
            ig2.setStroke(new BasicStroke(3F)); // set the stroke ?width?

            int bMargin = 50, lMargin = 50, rMargin = 50, tMargin = 50; // bottom margin, left margin
            int diff = 200; // every 50 pixel has a tics

            ig2.draw(new Line2D.Double(lMargin, height-bMargin, width-rMargin, height-bMargin)); // x-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, lMargin, height-bMargin)); // y-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, width-rMargin, tMargin)); // top-box boundary
            ig2.draw(new Line2D.Double(width-rMargin, tMargin, width-rMargin, height-bMargin)); // right-box boundary

            Font font = new Font("Times", Font.PLAIN, 20);   // create the font  
            ig2.setFont(font);// set the font for the 
            // x-tics
            int tics = 0; // start the tics at 0.
            for (; tics <= 5; ++tics){
                ig2.drawString("|", tics*diff+lMargin-5, height-bMargin); // draw a tic mark every fifth of the the way across the graph
                ig2.drawString(Integer.toString(tics*(int)xmax/5), tics*diff+lMargin, height-25); // draw an integer every fifth of the way across the graph
            }
            tics = 0; // reset the ticks to 0.
            for (; tics <= 5; ++tics){ 
                ig2.drawString(Integer.toString(tics*(int)ymax/5), 5, height-bMargin-tics*diff); // draw a int mark every fifth of the the way across the graph
                ig2.drawString("-", lMargin, height-bMargin-tics*diff+5);  //  draw tick marks
            }

            double x1 = -1, y1 = 0.0; //

            fstream.getChannel().position(0);
            br = new BufferedReader(new InputStreamReader(fstream));
            
            strLine = br.readLine();
                    
                    
                    
            while ( (strLine = br.readLine()) != null){ // read in the input file
                String strTmp[]; // reset the line
                strTmp = strLine.split(delimiter); // split the line
                double x2 = Double.parseDouble(strTmp[0]); // parse the x value
                double y2 = Double.parseDouble(strTmp[1]); // parse the y value
                
                if (x1 != -1)
                {
                 // draw the line if the input is not the first
                ig2.draw(new Line2D.Double(x1/xmax*(double)(width-lMargin-rMargin)+lMargin, height-y1/ymax*(double)(height-tMargin-bMargin)-bMargin,
                                           x2/xmax*(double)(width-lMargin-rMargin)+lMargin, height-y2/ymax*(double)(height-tMargin-bMargin)-bMargin));
                
                }
                x1 = x2;// shift the values 
                y1 = y2;
            }

	    ImageIO.write(bi, "PNG", new File(p+"/aveLengthHistory"+ simnum+".png")); // write the file
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	
    }
    public void drawHistory(String p, int simnum){
    //public void draw(byte d[][], int bh[], int indx, String p){

	try {

            FileInputStream fstream = new FileInputStream(p+"/concentration"+simnum+".dat"); // read in the file 
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream)); // 
            String strLine;
            String delimiter = "[ ]+"; // delimiter
            double xmax = 0.0, ymax = 0.0, zmax = 0.0; // largest value on x-axis and y-axis
            while ( (strLine = br.readLine()) != null){ // go through the entire file
              
                String strTmp[];
                strTmp = strLine.split(delimiter); // split the line
                
                double x2 = Double.parseDouble(strTmp[0]);
               
                double y2 = Double.parseDouble(strTmp[2]); // parse the appropraite values from the input file 
                double z2 =  Double.parseDouble(strTmp[3]);
                xmax = x2;
                if (ymax < y2) ymax = y2;
                if (zmax < z2) zmax = z2; // figure out the maximum values

            }
            
            ymax = ymax+50.0;   // scale the maximum values for for each maximum
            ymax = Math.ceil(ymax/50.0)*50.0;
            xmax = Math.ceil(xmax/50.0)*50.0;
            zmax = Math.ceil(zmax/50.0)*50.0;
            //double width = Math.ceil(xmax), height = Math.ceil(ymax)+100;
            int width = 1100, height = 1100; // set the sidth and height
	    //BufferedImage bi = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_BYTE_INDEXED);
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);

	    Graphics2D ig2 = bi.createGraphics();
            ig2.setColor(Color.WHITE);
            ig2.fillRect(0, 0, (int)width, (int)height); // create the white background
            ig2.setColor(Color.BLACK);
            ig2.setStroke(new BasicStroke(3F)); // switch the graphics so it can draw black lines

            int bMargin = 50, lMargin = 50, rMargin = 50, tMargin = 50; // bottom margin, left margin
            int diff = 200; // every 50 pixel has a tics

            ig2.draw(new Line2D.Double(lMargin, height-bMargin, width-rMargin, height-bMargin)); // x-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, lMargin, height-bMargin)); // y-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, width-rMargin, tMargin)); // top-box boundary
            ig2.draw(new Line2D.Double(width-rMargin, tMargin, width-rMargin, height-bMargin)); // right-box boundary

            Font font = new Font("Times", Font.PLAIN, 20); //create the font
            ig2.setFont(font); // set the font 
            // x-tics
            int tics = 0; // set the ticks equal
            for (; tics <= 5; ++tics){
                ig2.drawString("|", tics*diff+lMargin-5, height-bMargin); // draw 5 tisks across the bottom of the graph
                ig2.drawString(Integer.toString(tics*(int)xmax/5), tics*diff+lMargin, height-25); // draw 5 properly scaled values at the bottom o the graph
            }
            tics = 0;
            for (; tics <= 5; ++tics){
              
                ig2.drawString(Integer.toString(tics*(int)ymax/5), 5, height-bMargin-tics*diff);  // 
                ig2.drawString("-", lMargin, height-bMargin-tics*diff+5); // // draw 5 properly scaled tick marks and interger 
            }
            
            
            // create two labels corresponding to the two axises on the charts
             font = new Font("Times", Font.PLAIN, 30);
            ig2.setColor(Color.BLUE);
            ig2.drawString("Polymerized", lMargin+10, 100);
            ig2.setColor(Color.RED);
            ig2.drawString("Free", lMargin+10, 140);
            
            
            

            double x1 = -1, y1 = 0.0, z1 = 0.0; // establis the first values

            fstream.getChannel().position(0);
            br = new BufferedReader(new InputStreamReader(fstream)); // read in the fiel
            while ( (strLine = br.readLine()) != null){ // go through the data file
                String strTmp[];
                strTmp = strLine.split(delimiter); // split the data input line
                double x2 = Double.parseDouble(strTmp[0]); // parse out the values
                double y2 = Double.parseDouble(strTmp[2]);
                double z2 = Double.parseDouble(strTmp[3]);
                z2 *=100/Double.parseDouble(strTmp[1]); // scale the vlaues based off of the total 
                y2 *=100/Double.parseDouble(strTmp[1]);
                
                if (x1 != -1) // if this is not the first point
                {// then draw both lines in  the appropriate color
                    
                    
                ig2.setColor(Color.RED);
                ig2.draw(new Line2D.Double(x1/xmax*(double)(width-lMargin-rMargin)+lMargin, height-y1/ymax*(double)(height-tMargin-bMargin)-bMargin,
                                           x2/xmax*(double)(width-lMargin-rMargin)+lMargin, height-y2/ymax*(double)(height-tMargin-bMargin)-bMargin));
                
                
                
                
                ig2.setColor(Color.BLUE);
                ig2.draw(new Line2D.Double(x1/xmax*(double)(width-lMargin-rMargin)+lMargin, height-z1/ymax*(double)(height-tMargin-bMargin)-bMargin,
                                           x2/xmax*(double)(width-lMargin-rMargin)+lMargin, height-z2/ymax*(double)(height-tMargin-bMargin)-bMargin));
                }
                
                x1 = x2; // shift the point to the next value
                y1 = y2;
                z1 = z2;
            }

	    ImageIO.write(bi, "PNG", new File(p+"/ConcentrationPlot"+ simnum+".png")); // write out the file
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	
    }
    
      
    public void drawConc(String p){
   

	try {
            
            /// add axis  total concentration, concentration,   different color lines , scale, legend

            FileInputStream fstream = new FileInputStream(p+"/conc.dat"); // open up the correct file 
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream)); // turn the file into a buffered reader
            String strLine;
            String delimiter = "[ ]+"; // delimiter is whitespace
            double xmax = 0.0, ymax = 0.0; // largest value on x-axis and y-axis
            double xmin = 0; // set the minimum  value to zero
            double ymin = 0;
            int x = 0; //used to determine whether this is the first run or not
        
            while ( (strLine = br.readLine()) != null){

                String strTmp[];
                strTmp = strLine.split(delimiter);
                double x2 = Double.parseDouble(strTmp[0]);
                double y2 = Double.parseDouble(strTmp[1]);
                double z2 = Double.parseDouble(strTmp[2]);
                if (x ==0)
                { 
                xmin = x2; // reset the minimum values to the first values in the input file
                
                ymin = y2; 
                }
                 
                
                // all of these are used to determine what the highest/ lowest values were for 
                if (ymax < y2) ymax = y2;
                if (xmax < x2) xmax = x2; 
               if (ymax < z2) ymax = z2;
                if (ymin > y2) ymin = y2;
                if (ymin > z2) ymin = z2;
                if (xmin > x2) xmin = x2;
                
                x++;
                
            }
            
            
          
           
            double range = xmax - xmin; // create ranges to properly scale the axises 
            double yrange = ymax - ymin;
            
            
           
            
            
            int width = 1100, height = 1100; // create the width and hieght of the 
	    //BufferedImage bi = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_BYTE_INDEXED);
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);

	    Graphics2D ig2 = bi.createGraphics();
            ig2.setColor(Color.WHITE);
            ig2.fillRect(0, 0, (int)width, (int)height);
            ig2.setColor(Color.BLACK);
            ig2.setStroke(new BasicStroke(3F));

            int bMargin = 50, lMargin = 50, rMargin = 50, tMargin = 50; // bottom margin, left margin
            int diff = 200; // every 50 pixel has a tics

            ig2.draw(new Line2D.Double(lMargin, height-bMargin, width-rMargin, height-bMargin)); // x-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, lMargin, height-bMargin)); // y-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, width-rMargin, tMargin)); // top-box boundary
            ig2.draw(new Line2D.Double(width-rMargin, tMargin, width-rMargin, height-bMargin)); // right-box boundary

            Font font = new Font("Times", Font.PLAIN, 20);
            ig2.setFont(font);
            // x-tics
            int tics = 0;
            for (; tics <= 5; ++tics){
                ig2.drawString("|", tics*diff+lMargin-5, height-bMargin);
                ig2.drawString((Double.toString((1*tics*(Double)range/5)+xmin)), tics*diff+lMargin, (height)-25);
               // ig2.drawString(Integer.toString(2*tics*(int)xmax/50), tics*diff+lMargin, height-25);
               
            }
            
            
            
            
            tics = 0;
            for (; tics <= 5; ++tics){
                BigDecimal round = new BigDecimal(((1*tics*(Double)yrange/5)+ymin)); // calculate the value
                round = round.round(new MathContext(2)); // round the value
                ig2.drawString( Double.toString(round.doubleValue()),  5, height-bMargin-tics*diff); // print the string
                ig2.drawString("-", lMargin, height-bMargin-tics*diff+5);
            }

            
            font = new Font("Times", Font.PLAIN, 25);
            ig2.setFont(font);
            ig2.drawString("Concentration", lMargin+410, height- bMargin - 10); // write the concentration label
            
            font = new Font("Times", Font.PLAIN, 30); //  write the labels 
            ig2.setColor(Color.BLUE);
            ig2.drawString("Polymerized", lMargin+10, 100); 
            ig2.setColor(Color.RED);
            ig2.drawString("Free", lMargin+10, 140);
            
            
            
            double x1 = -1, y1 = 0.0, z1 = 0.0; // set the intitial vales

            fstream.getChannel().position(0);
            br = new BufferedReader(new InputStreamReader(fstream)); // re-open file to read the data for the chart
            while ( (strLine = br.readLine()) != null){
                String strTmp[];
                strTmp = strLine.split(delimiter); // split the input string
                double x2 = Double.parseDouble(strTmp[0]); // parse the input value
                double y2 = Double.parseDouble(strTmp[1]);
                double z2 = Double.parseDouble(strTmp[2]);
                
                
                if (x1 != -1) // of this is not the first run
                {
                 
                ig2.setColor(Color.RED); // change the color to red
           
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                // draw a line
                
                ig2.setColor(Color.BLUE); // change the color to blue
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((z1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((z2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                
                 // draw another line
                
                }
                
                x1 = x2; // shift the values
                y1 = y2;
                z1 = z2;
            }

           
	    ImageIO.write(bi, "PNG", new File(p+"/plotConc.png")); // save the values
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	
    }
    
      
    public void firstThree(String p, int simnum){
   

	try {
            
            /// add axis  total concentration, concentration,   different color lines , scale, legend

            FileInputStream fstream = new FileInputStream(p+"/length"+simnum+".dat"); // open up the correct file 
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream)); // turn the file into a buffered reader
            String strLine;
            String delimiter = "[ ]+"; // delimiter is whitespace
            double xmax = 0.0, ymax = 0.0; // largest value on x-axis and y-axis
            double xmin = 0; // set the minimum  value to zero
            double ymin = 0;
            
            int x = 0; //used to determine whether this is the first run or not
        
            while ( (strLine = br.readLine()) != null){
                System.out.println(strLine);
                
                String strTmp[];
                if (x !=0)
                {
                strTmp = strLine.split(delimiter);
                double x2 = Double.parseDouble(strTmp[0]);
                double y2 = Double.parseDouble(strTmp[1]);
                double z2 = Double.parseDouble(strTmp[2]);
                double a2 = Double.parseDouble(strTmp[3]);
                
                if (x ==1)
                { 
                xmin = x2; // reset the minimum values to the first values in the input file
                
                ymin = y2; 
                }
                 
                
                // all of these are used to determine what the highest/ lowest values were for 
                if (ymax < y2) ymax = y2;
                if (xmax < x2) xmax = x2; 
                if (ymax < z2) ymax = z2;
                if (ymax < a2) ymax = a2;
                if (ymin > y2) ymin = y2;
                if (ymin > z2) ymin = z2;
                if (xmin > x2) xmin = x2;
                if (ymin > a2) ymin = a2;
                }
                x++;
                
            }
            
            
          
           
            double range = xmax - xmin; // create ranges to properly scale the axises 
            double yrange = ymax - ymin;
            
            
           
            
            
            int width = 1100, height = 1100; // create the width and hieght of the 
	    //BufferedImage bi = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_BYTE_INDEXED);
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);

	    Graphics2D ig2 = bi.createGraphics();
            ig2.setColor(Color.WHITE);
            ig2.fillRect(0, 0, (int)width, (int)height);
            ig2.setColor(Color.BLACK);
            ig2.setStroke(new BasicStroke(3F));

            int bMargin = 50, lMargin = 50, rMargin = 50, tMargin = 50; // bottom margin, left margin
            int diff = 200; // every 50 pixel has a tics

            ig2.draw(new Line2D.Double(lMargin, height-bMargin, width-rMargin, height-bMargin)); // x-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, lMargin, height-bMargin)); // y-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, width-rMargin, tMargin)); // top-box boundary
            ig2.draw(new Line2D.Double(width-rMargin, tMargin, width-rMargin, height-bMargin)); // right-box boundary

            Font font = new Font("Times", Font.PLAIN, 20);
            ig2.setFont(font);
            // x-tics
            int tics = 0;
            for (; tics <= 5; ++tics){
                ig2.drawString("|", tics*diff+lMargin-5, height-bMargin);
                ig2.drawString((Double.toString((1*tics*(Double)range/5)+xmin)), tics*diff+lMargin, (height)-25);
               // ig2.drawString(Integer.toString(2*tics*(int)xmax/50), tics*diff+lMargin, height-25);
               
            }
            
            
            
            
            tics = 0;
            for (; tics <= 5; ++tics){
                BigDecimal round = new BigDecimal(((1*tics*(Double)yrange/5)+ymin)); // calculate the value
                round = round.round(new MathContext(2)); // round the value
                ig2.drawString( Double.toString(round.doubleValue()),  5, height-bMargin-tics*diff); // print the string
                ig2.drawString("-", lMargin, height-bMargin-tics*diff+5);
            }

            
            font = new Font("Times", Font.PLAIN, 25);
            ig2.setFont(font);
            ig2.drawString("Concentration", lMargin+410, height- bMargin - 10); // write the concentration label
            
            font = new Font("Times", Font.PLAIN, 30); //  write the labels 
            ig2.setColor(Color.BLUE);
            ig2.drawString("First MT", lMargin+10, 100); 
            ig2.setColor(Color.RED);
            ig2.drawString("Second MT", lMargin+10, 140);
            ig2.setColor(Color.GREEN);
            ig2.drawString("Third MT", lMargin+10, 180);
            
            
            
            double x1 = -1, y1 = 0.0, z1 = 0.0, a1 = 0.0; // set the intitial vales

            fstream.getChannel().position(0);
            br = new BufferedReader(new InputStreamReader(fstream)); // re-open file to read the data for the chart
            while ( (strLine = br.readLine()) != null){
                String strTmp[];
                strTmp = strLine.split(delimiter); // split the input string
                double x2 = Double.parseDouble(strTmp[0]); // parse the input value
                double y2 = Double.parseDouble(strTmp[1]);
                double z2 = Double.parseDouble(strTmp[2]);
                double a2 = Double.parseDouble(strTmp[3]);
                
                
                if (x1 != -1) // of this is not the first run
                {
                 
                ig2.setColor(Color.RED); // change the color to red
           
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                // draw a line
                
                ig2.setColor(Color.BLUE); // change the color to blue
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((z1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((z2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                
                 // draw another line
                 
                 ig2.setColor(Color.GREEN); // change the color to green
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((a1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((a2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                
                 // draw a third line
                
                }
                
                x1 = x2; // shift the values
                y1 = y2;
                z1 = z2;
                a1 = a2;
            }

           
	    ImageIO.write(bi, "PNG", new File(p+"/individuals.png")); // save the values
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	
    }
    
    
     
    public void drawJofC(String p){
    //public void draw(byte d[][], int bh[], int indx, String p){

	try {
            /// add axis  total concentration, concentration,   different color lines , scale, legend
            FileInputStream fstream = new FileInputStream(p+"/JofC.dat"); // read the file from the data
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream)); // create a image
            String strLine;
            String delimiter = "[ ]+"; // delimeter
            double xmax = 0.0, ymax = 0.0; // largest value on x-axis and y-axis
            
            
            double xmin = 0;
            double ymin = 0;
            int x = 0;
            while ( (strLine = br.readLine()) != null){ // read through the whole document
                String strTmp[];
                strTmp = strLine.split(delimiter);
                double x2 = Double.parseDouble(strTmp[0]);  // first entry in the file is the x value
                double y2 = Double.parseDouble(strTmp[1]); // second entry is the y value
                if (x ==0) // if this is the first line being read in
                {
                    xmin = x2;   // set the minimum to the first value 
                    ymin = y2; 
                }
                if (ymax < y2) ymax = y2; // assign the maximum values for each
                if (xmax < x2) xmax = x2;
                if (ymax < y2) ymax = y2;
                if (xmax < x2) xmax = x2;
                if (ymin > y2) ymin = y2;
                if (xmin > x2) xmin = x2;
                x++; ///increment the x value
              
            }
            if (abs(ymin) < abs(ymax)) // figure whether the minimum or the maximum has a larger magnitude
            {
                ymin = -1*abs(ymax);  // if the max is larger, the y max s equals the positive maginute of the value, and the min equals the negative magnitude of this value
                ymax =  abs(ymax);
            }
            else
            {
                ymin = -1*abs(ymin); // otherwise, base the magnitude off of the negative value
                ymax =  abs(ymin);
            }
            
            double range = xmax - xmin;  // create both the y range and x range 
            double yrange = ymax - ymin;
            
            
            int width = 1100, height = 1100; // width and height of the window
	    
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED); // create a buffered image

	    Graphics2D ig2 = bi.createGraphics();  // create graphics to work with
            ig2.setColor(Color.WHITE);  //change the color to white
            ig2.fillRect(0, 0, (int)width, (int)height); // draw the background
            ig2.setColor(Color.BLACK); // change the color to clack
            ig2.setStroke(new BasicStroke(3F)); // set the stroke of the draw 

            int bMargin = 50, lMargin = 50, rMargin = 50, tMargin = 50; // bottom margin, left margin
            int diff = 200; // every 50 pixel has a tics

            ig2.draw(new Line2D.Double(lMargin, height-bMargin, width-rMargin, height-bMargin)); // x-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, lMargin, height-bMargin)); // y-axis
            ig2.draw(new Line2D.Double(lMargin, tMargin, width-rMargin, tMargin)); // top-box boundary
            ig2.draw(new Line2D.Double(width-rMargin, tMargin, width-rMargin, height-bMargin)); // right-box boundary
            ig2.draw(new Line2D.Double(lMargin, height/2, width-rMargin, height/2)); // right-box boundary
            Font font = new Font("Times", Font.PLAIN, 20);
            ig2.setFont(font);
            // x-tics
            int tics = 0; // 
            for (; tics <= 5; ++tics){ // go throught the x axis and draw the different tics
                ig2.drawString("|", tics*diff+lMargin-5, height/2);   // draw the tick marks
                BigDecimal round = new BigDecimal((tics*(Double)range/5)+xmin); // calculate the value
                round = round.round(new MathContext(2)); // round the number
                
                
                ig2.drawString(Double.toString(round.doubleValue()), tics*diff+lMargin, (height/2)+25); //draw the number on the chart
            }
            
             // create positive ticks
            tics = 0;
            for (; tics <= 5; ++tics){ // go through the positive y axis and make tics
                
                BigDecimal round = new BigDecimal((2*tics*(Double)ymax/5));
                round = round.round(new MathContext(2));
                
                
                ig2.drawString(Double.toString(round.doubleValue()), 5, (height/2)-tics*diff );
                ig2.drawString("-", lMargin, (height/2)-tics*diff+5);
            }
            //create negative ticks
            tics = 1;
            for (; tics <= 5; ++tics){ // go through the negative y axis and make tics
                
                BigDecimal round = new BigDecimal((2*tics*(Double)ymax/5)); // calculate the value
                round = round.round(new MathContext(2)); // round the number
                ig2.drawString("-"+Double.toString(round.doubleValue()), 5, (height/2)+tics*diff ); // place the number witha  negative sign
                ig2.drawString("-", lMargin, (height/2)+tics*diff+5); // place the ticks
            }

            
            font = new Font("Times", Font.PLAIN, 25);   // change the font for the labels
            ig2.setFont(font); // set th font 
            ig2.drawString("Dilution Concentration", lMargin+410, height- bMargin - 10); // write the label
           
            font = new Font("Times", Font.PLAIN, 30); // se the font for the label
            ig2.setColor(Color.RED); //set the color for the font 
            ig2.drawString("Flux", lMargin+10, 140); // write the font
            
            
            
            double x1 = -1, y1 = 0.0, z1 = 0.0;  /// set the intial starting positions for all of the stuff

            fstream.getChannel().position(0); //  i have no clue
            br = new BufferedReader(new InputStreamReader(fstream));  
            while ( (strLine = br.readLine()) != null){
                String strTmp[];
                strTmp = strLine.split(delimiter);
                double x2 = Double.parseDouble(strTmp[0]); // parse out the correct nuber from the file
                double y2 = Double.parseDouble(strTmp[1]); // same
                
                
                if (x1 != -1) // if this is not the first value
                {
                
                ig2.draw(new Line2D.Double((((x1-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y1-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin,
                                           (((x2-xmin)/range)*(double)(width-lMargin-rMargin))+lMargin, height-((y2-ymin)/yrange)*(double)(height-tMargin-bMargin)-bMargin));
                }
               
                x1 = x2; // shift to the next point
                y1 = y2; // shift to the next point
               
            }

	    ImageIO.write(bi, "PNG", new File(p+"/plotJofC.png"));  // write out the file
	} catch (IOException ie) {
	    ie.printStackTrace();
	}
	
    }
}


    



