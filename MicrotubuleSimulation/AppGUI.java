// The general user interface

import treadmilling.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppGUI extends JFrame{

    // member for GUI
    private JMenu selectMenu;  // select the models, macro or detailed
    private JMenu fileMenu;    // select the input file
    private JMenu displayMenu; // display snapshots
    private JMenu simMenu;     // simulation
    private JMenuBar bar;      // the whole menu bar
    private boolean watched;

    private JTextArea textArea;
    private JScrollPane scrollPane;

    // member for model, modify later
    private File fileName;     // input configuration
    private treadmilling.Simulation simCoarsen;
    private Experiment changeConc;
    
    private int select; // coarsen-1, detailed-2

    public AppGUI(){
	super("Microtubule Simulation Engine");

        
        
        watched = false;
	bar = new JMenuBar(); // add the Menu Bar on top of the GUI
	setJMenuBar(bar);

        // add selection menu onto the menu bar
        selectMenu = new JMenu("Model Select");
        selectMenu.setMnemonic('M');
        JMenuItem coarsenItem = new JMenuItem("Simplified Model");
        selectMenu.add(coarsenItem);

	// add File Menu into Menu Bar
	fileMenu = new JMenu("File");
	fileMenu.setMnemonic('F');
	JMenuItem openItem = new JMenuItem("Open");
	JMenuItem saveItem = new JMenuItem("Save");
	JMenuItem exitItem = new JMenuItem("Exit");
	fileMenu.add(openItem);
	fileMenu.add(saveItem);
	fileMenu.add(exitItem);

	// add Analyze Menu into Menu Bar
	simMenu = new JMenu("Simulation");
	simMenu.setMnemonic('S');
	JMenuItem simItem = new JMenuItem("Run");
	simMenu.add(simItem);

	// add Display Menu into Menu bar
	displayMenu = new JMenu("Graphic");
	displayMenu.setMnemonic('G');
	JMenuItem displayItem = new JMenuItem("Animation");
	displayMenu.add(displayItem);
        JMenuItem plotItem = new JMenuItem("Plot");
        displayMenu.add(plotItem);

        bar.add(selectMenu);
	bar.add(fileMenu);
	bar.add(simMenu);
	bar.add(displayMenu);

	// basic GUI setup
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(1000, 500);
	setVisible(true);

        // add Handlers for each Item
        CoarsenHandler coarsenHandler = new CoarsenHandler();
        coarsenItem.addActionListener(coarsenHandler);

	OpenHandler openHandler = new OpenHandler();
	openItem.addActionListener(openHandler);
	ExitHandler exitHandler = new ExitHandler();
	exitItem.addActionListener(exitHandler);

	SimHandler simHandler = new SimHandler();
	simItem.addActionListener(simHandler);

	DisplayHandler displayHandler = new DisplayHandler();
	displayItem.addActionListener(displayHandler);
        PlotHandler plotHandler = new PlotHandler();
        plotItem.addActionListener(plotHandler);

	// Add and set the Text Area to store the log
	textArea = new JTextArea();

	textArea.setEditable(false); // set not editable
	textArea.setBackground(Color.WHITE); // background white
	textArea.setForeground(Color.BLACK); // foreground black (formerly blue)
	textArea.setFont(new Font("Arial", Font.BOLD, 30));
 // Arial 
 // chage colors
	// welcome message
	textArea.append("*** Welcome to Microtubule Simulation Engine! ***\n");
        textArea.append("    1. Select the model, simplified or detailed\n");
	textArea.append("    2. Use File menu bar to select input file\n");
	textArea.append("    3. Use Simulation menu to run the simulation\n");
	textArea.append("    4. Use display menu to play the saved frames\n");
	textArea.append("    Credits: Chunlei Li, Holly Goodson, Mark Alber, University of Notre Dame\n");
	textArea.append("*****************************************************************************\n");
	textArea.append("*****************************************************************************\n");

	scrollPane = new JScrollPane(textArea);
	add(scrollPane, BorderLayout.CENTER);
	validate();
    }

    // select handler
    private class CoarsenHandler implements ActionListener{

        public void actionPerformed(ActionEvent event){
            select = 1;
            textArea.append("*** Simplified model is selected. ***\n");
        }

    }


    // file chooser handler
    private class OpenHandler implements ActionListener{
	
	public void actionPerformed(ActionEvent event){
            fileName = null;
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.showOpenDialog(AppGUI.this);
	    fileName = fileChooser.getSelectedFile();
	    
	    if ( fileName.exists()){
		textArea.append("   "+fileName.getName()+" is opened.\n");
	    }else{
		textArea.setForeground(Color.RED); 
		textArea.append("   "+fileName.getName()+" cannot be opened!!!\n");
	    }
	}
    }

    private class ExitHandler implements ActionListener{

	public void actionPerformed(ActionEvent event){
	    System.exit(0);
	}
    }

    // run simulation
    private class SimHandler implements ActionListener{

	public void actionPerformed(ActionEvent event){
            // select: coarsen-1, detailed-2
            if (select == 1){
               // run an expiment 
               
               //Experiment changeConc;
                try {
                    changeConc= new Experiment(); // create an experiment 
                    changeConc.readthree(fileName); // read in the file for the experiment 
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(AppGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                textArea.append("*** Simplified Simulation ("+fileName.getName()+") completes ***\n"); // let the user know the simulaiton has been completed
            }
	}
    }

    // display handler
    private class DisplayHandler implements ActionListener{

	public void actionPerformed(ActionEvent event){
            if (select == 1){ 
                if (watched != true)// if you havent already watched the movie
                {
                watched = true; // set the watched value to true
                
                /*  this part is is the code as a temporary fix to a memory issue with part of this code.  when the program is allowed to run the animation 
                multiple times in a row, the program runs into some error, most notably out of memory errors.  as such, the temporary fix for this error is
                prevent the user from running the animation more than one time*/
                
		JFrame displayFrame = new JFrame("movie"); /// create a new window for the movie
		displayFrame.setSize(1000, 500); // set the size for the window
		displayFrame.setVisible(true); // make the window visible 
                displayFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // set the close value of the video
		
		int nf = changeConc.getNFrames(); // get the number of frames from the simulation
                String Conc = changeConc.getMovieConc(); // get the concentration
                
             
                
		treadmilling.Animation animation = new treadmilling.Animation(nf, System.getProperty("user.dir")+"/"+Conc); // create the animation
                
		JScrollPane dscrollPane = new JScrollPane(animation); // use the  animation on a scroll pane?
		displayFrame.add(dscrollPane, BorderLayout.CENTER);
		animation.startAnimation(); // start the animation
                animation = null; // dereference the animation
                }
            }
	}
    }

    private class PlotHandler implements ActionListener{

        public void actionPerformed(ActionEvent event){

	    if (select == 1){
          
		if (changeConc.getDilute())  // check whether it is a dilution experiment or a normal experiment
                {
                    
                JFrame displayFrame = new JFrame("J(c) Plot"); // create a new win
		displayFrame.setSize(850, 850);  // set the size of the window
		displayFrame.setVisible(true); // set the screen to visible
		displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // se the windows close status
		treadmilling.ShowPlot plotMT = new treadmilling.ShowPlot(System.getProperty("user.dir")+"/plotJofC.png"); // create the plot
                JScrollPane dscrollPane = new JScrollPane(plotMT); // create a scrol pane to display the
		displayFrame.add(dscrollPane, BorderLayout.CENTER); // 
                }
                else // if it is a normal experiment 
                {
                JFrame displayFrame = new JFrame("Conc Plot"); // create a new window
		displayFrame.setSize(850, 850); // sets the size of the window 
		displayFrame.setVisible(true); /// sets the window to be visible
		displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // set the window's close status
                treadmilling.ShowPlot plotMT = new treadmilling.ShowPlot(System.getProperty("user.dir")+"/plotConc.png"); //create the plot shown to others
                JScrollPane dscrollPane = new JScrollPane(plotMT); // create a scroll pane to display thevideo
		displayFrame.add(dscrollPane, BorderLayout.CENTER);
                }
			   

	    }
        }
    }

}

