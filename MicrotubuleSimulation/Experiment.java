
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import treadmilling.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nick
 */
public class Experiment {
    // this experiment takes one input file, and then uses it to run mulitple simulations 
    
    private treadmilling.Simulation simCoarsen; // the simulation 
    private double critFree[]; /// stores the free tubulin concentration for each of the simulations tat is run
    private double critPoly[]; // stores the polymerised tubulin concentrations for each of the simulations that is run
    private double conc[]; // stored the concentration of each experiemnt
    private Formatter outFileConc;
    private Formatter outFileJofC;
    private File folder;
    private boolean dilute;
    private File input;
    private int num;
    private double increase;
    
   
    
    private String path;
    
    private double JofCFlux[];
    private double JofCConc[];
    
    public Experiment() throws FileNotFoundException{
        FileInputStream in =  null;
    }
    
    public void readthree(File fileName) throws IOException
    {
        path = System.getProperty("user.dir"); // get the path of the jar file
        FileInputStream in = new FileInputStream(fileName.getAbsoluteFile()); // create a new filestream from the file that is given to you
        int j;  // used to iterate through the file
        num = 1; // number of simulation to be run, defaults to one
        increase = 0.0; // increase the concentration by a particular amount 
        String concStr = ""; // used to create the folder name
        input = fileName;
        simCoarsen = new treadmilling.Simulation(0); // create and allocate memory for the simulation
        simCoarsen.readConfig(fileName.getAbsolutePath()); // read in the data forthe simulation
        dilute = simCoarsen.getDilute(); // figure out whether the simulation is a dilution or not
        num = simCoarsen.getRunCount();// figure out how many simulaitons need to be run
        increase = simCoarsen.getIncreaseConc();
        simCoarsen = null; // set the simulation to null for it to be used next time 
        critFree = new double[num]; // allocate memory for all of the information
        critPoly = new double[num];
        conc =  new double[num];
        JofCFlux = new double[num];
        JofCConc = new double[num]; 
        
        if (dilute)
        { // if its a dilution experiemnt 
            diluteExp();  // run a dilution experiment 
        }
        else
        { // otherwise
            concExp(); // run a soncentration experiemnt  
        }
    }
 
        
    public void concExp() throws FileNotFoundException
    {
        outFileConc = new Formatter(System.getProperty("user.dir")+"/conc.dat"); // create a new output file
        int j = 0 ;
        for (j = 0; j < num; j++) // go through all of the simulations
        {
           
            simCoarsen = null;  /// set the simulation to null
            simCoarsen = new treadmilling.Simulation(j);  // create a new simulation
            simCoarsen.readConfig(input.getAbsolutePath()); // read in the data forthe simulation
            simCoarsen.increaseConc(increase*j); // increase the concentration for the current simulation
            String concStr = String.format("%.2g", simCoarsen.getConc()); // get the concentration, and format so it round to hundredth pace
            if (concStr.length() == 5) // if it didnt round correctly
                {
                concStr = String.format("%.1g", simCoarsen.getConc()); // round it again
                }
            concStr += 'M';   // add a M at the end of the string so the folder properly reflects concetration , like 0.01M
            Boolean val = new File(path+"/"+concStr).mkdirs();
            simCoarsen.setPath(path+"/"+concStr); // set the correct path for the entry // set the file as the proper path for the simulation to output to
            simCoarsen.run(); // run the simulation
            critPoly[j] = simCoarsen.getCritPoly(); // get the concentration information from the previous simulatipn
            critFree[j] = simCoarsen.getCritFree();
            conc[j] = simCoarsen.getConc();
            
            outFileConc.format("%.4f %.4f %.4f\n", conc[j], critFree[j], critPoly[j]); // write the concentration information to the ourputfile 
        }
        outFileConc.close(); // close the outpu file
        Plot plotMT = new Plot(); //create a plot object
        plotMT.drawConc(path); // draw the plot of the concentration usin the plot object.
    }
    public void diluteExp() throws FileNotFoundException
    { // run a dilution simulation 
        outFileJofC = new Formatter(System.getProperty("user.dir")+"/JofC.dat"); // create a new output file
        int j=0; // intialize the iterator to zero
        for (j = 0; j < num; j++) // go through all of the simulations
        {
           
            simCoarsen = null;// set the simulation to null
            simCoarsen = new treadmilling.Simulation(j); //create a new simulation, number j
            simCoarsen.readConfig(input.getAbsolutePath()); // read in the data forthe simulation
            //System.out.println(simCoarsen.getDiluteConc());
            simCoarsen.increaseDiluteConc(increase*j); // increase the concetentration by the appropriate amount
            String concStr = String.format("%.2g", simCoarsen.getDiluteConc()); // get the concentration, and format so it round to hundredth pace
            if (concStr.length() == 5) // if it didnt round correctly
                {
                concStr = String.format("%.1g", simCoarsen.getDiluteConc()); // round it again
                }
            concStr += 'M';   // add a M at the end of the string so the folder properly reflects concetration , like 0.01M
            Boolean val = new File(path+"/"+concStr).mkdirs(); // make the file to store the data in
            simCoarsen.setPath(path+"/"+concStr); // set the correct path for the entry // set the file as the proper path for the simulation to output to
            simCoarsen.run(); // run the simulation
            JofCFlux[j] = simCoarsen.getFlux(); // save the import values
            JofCConc[j] = simCoarsen.getDiluteConc();
            //simCoarsen = null;// set the simulation to null
            outFileJofC.format("%.4f %.4f\n", JofCConc[j], JofCFlux[j]); // write to the outpu file
        }
        outFileJofC.close(); //close the output file
        Plot plotMT = new Plot(); //create a plot object
        plotMT.drawJofC(path); // draw a j of C plot  
    }
    
    public int getNFrames(){
        return simCoarsen.getNFrames();
        
    }
    
public boolean getDilute(){
        return simCoarsen.getDilute();
        
    }


public String getMovieConc(){
    
   String concStr = String.format("%.2g",  simCoarsen.getK("movieConc"));
   if (concStr.length() == 5) // if it didnt round correctly
                {
                concStr = String.format("%.1g", simCoarsen.getDiluteConc()); // round it again
                }
            concStr += 'M';   // add a M at the end of the string so the folder properly reflects concetration , like 0.01M
            return concStr;
    
    
    
}

public String getConcString(){

String concStr = "";
if (simCoarsen.getDilute())
{
    concStr = String.format("%.2g", simCoarsen.getDiluteConc()); // get the concentration, and format so it round to hundredth pace
if (concStr.length() == 5) // if it didnt round correctly
                {
                concStr = String.format("%.1g", simCoarsen.getDiluteConc()); // round it again
                }
            concStr += 'M';   // add a M at the end of the string so the folder properly reflects concetration , like 0.01M
            return concStr;
}
else
{
    concStr = String.format("%.2g", simCoarsen.getConc()); // get the concentration, and format so it round to hundredth pace
if (concStr.length() == 5) // if it didnt round correctly
                {
                concStr = String.format("%.1g", simCoarsen.getConc()); // round it again
                
                }
            concStr += 'M';   // add a M at the end of the string so the folder properly reflects concetration , like 0.01M
            return concStr;
}



}





}

