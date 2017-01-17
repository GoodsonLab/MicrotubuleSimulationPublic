package treadmilling;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Dimension;

public class DrawMT{

    public DrawMT(){
        //setPreferredSize(new Dimension(1200, 1200));
    }

    public void draw(final byte d[][], int indx, int ps[], String p){

	try{

	BufferedImage bi = new BufferedImage(1200, 1200, BufferedImage.TYPE_BYTE_INDEXED);

	Graphics2D ig2 = bi.createGraphics();

	double unitH = 10;
	double gap = 4;
	double unitW = 0.2; // microtubioles per pixel
	double x, y;

        for (int i = 0; i < d.length; ++i){
	    for (int j = 0; d[i] != null && j < d[i].length ; ++j){
               
                
                
	        if(d[i][j] == (byte) 1) // GTP
		    ig2.setColor(Color.RED);
	        else
		    ig2.setColor(Color.GREEN);
		// The following is for drawing double points
             //x = (j*unitW+ps[i])%1200;
                double offset = 550 - ps[i]; // ps[i] stores the begining position of the microtubiole if one pixel cooresponded to one sub-unit
                x = (j*unitW-(offset*unitW)+(550))%1200; //in order to find the correct begining position, we use the offset information in conjunction with the MT/pixel
            
		 y = i*unitH+100;
                 
                ig2.fill( new Rectangle2D.Double(x, y, unitW, unitH-gap) );
                
                if (i%3 == 1) // add a reference point every third MT
                {
                    ig2.setColor(Color.RED); // changes color and draws negative reference point
                    ig2.fill( new Rectangle2D.Double(550, y, 5, unitH-gap) );
                    ig2.setColor(Color.BLUE); // changes color and draws right reference point
                    ig2.fill( new Rectangle2D.Double(550 +(100*unitW), y, 5, unitH-gap) );
                }
		//ig2.fillRect(x, y, unitW, unitH-gap);
	    }
        }
        
        /*
       // this part of the code is to test writing to the file, and should eventually be erased.
        for (int i = 0; i < d.length; ++i){
            ig2.setColor(Color.BLUE);
            y = i*unitH+100;
            ig2.fill( new Rectangle2D.Double(50, y, 10, unitH-gap) );
        }
        */
        
	ImageIO.write(bi, "PNG", new File(p+"/"+Integer.toString(indx)+".png"));

	}catch(IOException ie){
	    ie.printStackTrace();
	}
    }

}

