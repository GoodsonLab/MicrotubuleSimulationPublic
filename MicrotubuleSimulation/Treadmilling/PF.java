package treadmilling;

import treadmilling.Dimer;
// PF is a double-linked-list structure

class PF{ // Proto fillament ?
    
    private int  currLen; // current length of PF
    private int  iniLen;  // initial length of PF
    private int  maxLen;  // maximum length of PF
    private int  beginP;   // beginnning position of PF
    
    Dimer plusEndDimer;  // plus end dimer
    Dimer minusEndDimer; // minus end dimer

    public PF(){
	// default setup, all dimers in PF are GTP
	currLen = 0;
	iniLen  = 0; 

	minusEndDimer = null;
	plusEndDimer = null;
    }
    
    public PF(int len){
	// default setup, all dimers in PF are GTP
	currLen = len;
	iniLen  = len; 
	maxLen  = 10000; 
	beginP  = 550;
	
	// I assume minus end to be the start of PF
	minusEndDimer = new Dimer();
	minusEndDimer.d = 0;
	Dimer tmp = minusEndDimer;
	Dimer tmp_pre = minusEndDimer;
	for (int i = 1; i < iniLen; ++i){
	    tmp.next = new Dimer();
	    tmp_pre = tmp;
	    tmp = tmp.next;
	    tmp.pre = tmp_pre;
	    tmp.d = (byte) 1; 
	}
	plusEndDimer = tmp;
	plusEndDimer.pre = tmp_pre;
	plusEndDimer.next = null;
    }
    
    public void display(){
	if (currLen > 0){
            System.out.printf("Length is %d.\n", currLen);
	    System.out.printf("- ");
	    Dimer tmp = minusEndDimer;
	    while (tmp != plusEndDimer){
		System.out.printf("%d ", tmp.d);
		tmp = tmp.next;
	    }
	    System.out.printf("%d ", plusEndDimer.d);
	    System.out.printf("+\n");
	    
	    System.out.printf("+ ");
	    tmp = plusEndDimer;
	    while (tmp != minusEndDimer){
		System.out.printf("%d ", tmp.d);
		tmp = tmp.pre;
	    }
	    System.out.printf("%d ", minusEndDimer.d);
	    System.out.printf("-\n");
	}else{
	    System.out.printf("PF is empty\n");
	}
    }
    
    public boolean isEmpty(){
	if (currLen == 0)
	    return true;
	else
	    return false;
    }
    
    // probably need overload byte and Dimer version
    public void plusAdd(byte newDimer){
	if (currLen > 0){
	    plusEndDimer.next = new Dimer();
	    Dimer tmp = plusEndDimer;
	    plusEndDimer = plusEndDimer.next;
	    plusEndDimer.pre = tmp;
	    plusEndDimer.d = newDimer;
	    ++currLen;
	}
	// MT can not polymerize without a seed
	// We may allow polymerize without seed later.
	//else{
	//  plusEndDimer = new Dimer();
	//  minusEndDimer = plusEndDimer;
	//  plusEndDimer.d = newDimer;
	//  ++currLen;  
	//}
    }
    
    
    
    
    // remove one dimer at a time
    public Dimer plusRemove(){
	if (currLen > 2){
	    Dimer tmp = plusEndDimer;
	    plusEndDimer = plusEndDimer.pre;
	    //tmp = null;
	    plusEndDimer.next = null;
	    --currLen;
            tmp.pre = null;
            tmp.next = null;
            return tmp;
            
	}else{ // When currLen = 1, plusEndDimer.pre = null,
	    // you can not use plusEndDimer.next = null.next
	    // Thus, we want to separate case 1 from others
            Dimer tmp = plusEndDimer;
            tmp.pre = null;
            tmp.next = null;
            
	    plusEndDimer = null;
	    minusEndDimer = null;
	    currLen = 0;
            beginP = 550;
            
            return tmp;
	}
    }
    
    // probably need overload byte and Dimer version
    public void minusAdd(byte newDimer){
	if (currLen> 0){
	    minusEndDimer.pre = new Dimer();
	    Dimer tmp = minusEndDimer;
	    minusEndDimer = minusEndDimer.pre;
	    minusEndDimer.next = tmp;
	    minusEndDimer.d = newDimer;
	    ++currLen;
	    --beginP;
	}
	// We may allow polymerize without seed later.
    }
    
    // remove one dimer at a time
    public Dimer minusRemove(){
	if (currLen > 2){
	    Dimer tmp = minusEndDimer;
	    minusEndDimer = minusEndDimer.next;
	    //tmp = null;
	    minusEndDimer.pre = null;
	    --currLen;
	    ++beginP;
            
            tmp.pre = null;
            tmp.next = null;
            return tmp;
	}else{
            
            Dimer tmp = plusEndDimer;
	    plusEndDimer = null;
	    minusEndDimer = null;
	    currLen = 0;
            beginP = 550;
            
            return tmp;
            
            
	}
    }

    public Dimer getMinusEnd(){
	if (currLen > 0)
	    return minusEndDimer;
	return null;
    }

    public Dimer getPlusEnd(){
	if (currLen > 0)
	    return plusEndDimer;
	return null;
    }

    public byte[] getMT(){
	byte ret[];
	if (!isEmpty()){
	    ret = new byte[currLen];
	    int i = 0;
	    Dimer tmp = minusEndDimer;
	    while (tmp != plusEndDimer){
		ret[i] = tmp.d;
		tmp = tmp.next;
		++i;
	    }
	    ret[i] = plusEndDimer.d;
	    return ret;
	}
	return null;
    }

    public int getLength(){
        return currLen;
    }

    public int getBegin(){
	return beginP;
    }

    public void nucleation() {
	// default setup, all dimers in PF are GTP
	currLen = 2;
	iniLen  = 2; 
	maxLen  = 10000; 
	beginP  = 550;
	
	// I assume minus end to be the start of PF
	minusEndDimer = new Dimer();
	minusEndDimer.d = 0;
	Dimer tmp = minusEndDimer;
	Dimer tmp_pre = minusEndDimer;
	for (int i = 1; i < iniLen; ++i){
	    tmp.next = new Dimer();
	    tmp_pre = tmp;
	    tmp = tmp.next;
	    tmp.pre = tmp_pre;
	    tmp.d = (byte) 1; 
	}
	plusEndDimer = tmp;
	plusEndDimer.pre = tmp_pre;
	plusEndDimer.next = null;
    }
    
    public Dimer popPlus(){
        if (currLen > 2){
	    Dimer tmp = plusEndDimer;
	    plusEndDimer = plusEndDimer.pre;
	    tmp.pre=null;
	    plusEndDimer.next = null;
	    --currLen;
            return tmp;
	}else{ // When currLen = 1, plusEndDimer.pre = null,
	    // you can not use plusEndDimer.next = null.next
	    // Thus, we want to separate case 1 from others
            Dimer tmp = plusEndDimer;
	    plusEndDimer = null;
	    minusEndDimer = null;
	    currLen = 0;
            beginP = 550;
            return tmp;
	}
    }
    
    public Dimer popMinus(){
        if (currLen > 2){
	    Dimer tmp = minusEndDimer;
	    plusEndDimer = plusEndDimer.next;
	    tmp.next=null;
	    minusEndDimer.pre = null;
	    --currLen;
            return tmp;
	}else{ // When currLen = 1, plusEndDimer.pre = null,
	    // you can not use plusEndDimer.next = null.next
	    // Thus, we want to separate case 1 from others
            Dimer tmp = minusEndDimer;
	    plusEndDimer = null;
	    minusEndDimer = null;
	    currLen = 0;
            beginP = 550;
            return tmp;
	}
    }
    
    
    public void addMinus(Dimer tmp){
        if (currLen > 1){
	   
            minusEndDimer.pre = tmp;
            tmp.next = minusEndDimer;
            minusEndDimer = tmp;
            minusEndDimer.pre = null;
            
	    ++currLen;
	    --beginP;
	}
    }
    
     public void addPlus(Dimer tmp){
        if (currLen > 1){
	   
            plusEndDimer.next = tmp;
            tmp.pre = plusEndDimer;
            plusEndDimer = tmp;
            plusEndDimer.next = null;
            
	    ++currLen;
	     
	}
        
     }
     
     /*
        public void remove(){
        currLen--;
     }
     */
        
        /*
        
         plusEndDimer.next = new Dimer();
	    Dimer tmp = plusEndDimer;
	    plusEndDimer = plusEndDimer.next;
	    plusEndDimer.pre = tmp;
	    plusEndDimer.d = newDimer;
	    ++currLen;
        
        */
    
     
     
    
    

}

