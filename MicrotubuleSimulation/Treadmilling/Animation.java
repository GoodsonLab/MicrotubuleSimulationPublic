package treadmilling;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Animation extends JPanel {

    private ImageIcon images[];
    private int nImages;
    private int currImage;
    private final int ANIMATION_DELAY = 100;
    private int width;
    private int height;
    private String parentPath;

    private Timer animationTimer;

    public Animation(final int n, final String p){
	nImages = n; /// the number of images, or frames, that is used in the animation
	images = new ImageIcon[nImages]; // create an array of image icons
        parentPath = p; //where the images are stored 
        BufferedImage bi = null; // buffered image that is read in

	for (int i = 0; i < nImages; ++i){ // go through all of the images

          try{ // attempt to read in the 
            bi = ImageIO.read(new File(parentPath+"/"+Integer.toString(i)+".png")); //load the image
            
	    images[i] = new ImageIcon(bi); // load the image into the image array
          }catch (IOException e){} // catch asny errors

	}
	width = images[0].getIconWidth(); // get the width of the image files
	height = images[0].getIconHeight(); // get the height of the image files
	setPreferredSize(new Dimension(width, height)); // set the ?window size?
    }

    public void paintComponent(Graphics g){
	super.paintComponent(g);

	images[currImage].paintIcon(this, g, 0, 0);

	if(animationTimer.isRunning())
	    currImage = (currImage+1)%nImages;
    }

    public void startAnimation(){
	if(animationTimer == null){ // start the animation 
	    currImage = 0; // set the current image to 0.
	    animationTimer = new Timer(ANIMATION_DELAY, new TimerHandler()); // create a time
	    animationTimer.start(); //start the timer
	}else{
	    if(!animationTimer.isRunning())  // if the timer is not already running
		animationTimer.restart(); // then restart it
	}
    }

    public void stopAnimation(){
	animationTimer.stop(); /// stop the timer
    }

    private class TimerHandler implements ActionListener{
	public void actionPerformed(ActionEvent actionEvent){
	    repaint();
	}
    }


}
