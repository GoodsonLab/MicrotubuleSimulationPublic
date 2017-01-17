
public class Main{
    /// Change log - added June 29th by Nick Carroll
    // Version 3.1
    /*
    1.simulations can now be run above 100 without crashing 
    
    
    */
    ///Version 3.0 - change Summary
    /*
    
    1.This version allows for the user to run simulations of varying concentration in batch.   The updated input file contains 2 new parameters. These parameters are "simNumber" and "increaseConc".  simNumber is the total number of the simulations that the user wants to run, and increaseConc is the amount the concentration is increased between each simulation.  For example, if the initial concentration is 0.1, and the increaseConc value is 0.05, and the simNumber value is 3, then the program will run a simulation at 0.1 M, 0.15 M, and 0.2 M.  

    2.Another new feature is that all of these new simulations are compiled together to make a concentration plot at the end of all the simulations.  This file – plotConc.png – is will appear after all of the simulations have been run.

    3.Finally, for each simulation, the program now automatically creates a folder to store the relevant datafiles for each simulation ran.  This makes it much easier to manage the data.  
    
    */
    /// Version 2.1 - change summary  changes made by Nicholas Carroll 
    /*
    
    This version includes the following additions:
1. Different on and off rates for GDP and GTP Tubulin and the subunit that it is falling off of or coming onto. The general nomenclature that was used in the input file was "kxony," where x is the identity of the subunit (D for GDP or T for GTP) and y is the identity of the subunit it is attaching or detaching from. For example kTonD would be the on rate for a GTP subunit to come onto a GDP end. 

A quick note on this is that there are still only two off rates at the minus end (one for GTP and one for GDP). This is because a subunit coming on/falling off the minus end "doesn't care" what the nucleotide state of the subunit it is attaching to/detaching from. In order to be consistent and make the simulation as robust as possible I'm thinking we should just set it up the same as the plus end. Any thoughts?

Also there is a slight glitch in using rate constants this way for when you get down to just one subunit. Because the way the math is set up, there needs to be a subunit below the subunit in question in order to calculate the probability of falling off. So for now if the microtubule is down to one subunit, the simulation will use the original off rates (which are still present in the input file) to determine what happens to the one subunit. My thought is that if we are down to one subunit the microtubule should disappear anyways so if a microtubule of length 2 loses a subunit it should disappear. But I may not be thinking right given that one subunit contains 13 dimers. I think I'm typing myself into confusion now. Holly and Erin any thoughts here?

2. Tubulin turnover in solution. The input file has a rate that can be input to change how quickly GDP subunits in solution become GTP subunits.

3. Nucleotide exchange at the plus end. Again there is now a rate that can be input to change how quickly this happens.
    
    */
    public static void main(String[] args){
        AppGUI application = new AppGUI();
    }
    
}

