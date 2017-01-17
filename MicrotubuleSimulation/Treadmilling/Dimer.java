package treadmilling;

class Dimer {

    // use public right now, need to modify make them private
    public byte d;     // state of the dimer 1 means T, 0 means D
    public Dimer next; // the next dimer, from minus end to plus end
    public Dimer pre;
    
    public Dimer(){
	d    = -1;
	next = null;
	pre  = null;
    }
    
    public void hydrolyze(){
	d = 0;
    }
    
    public void exchange(){
	d = 1;
    }

}

