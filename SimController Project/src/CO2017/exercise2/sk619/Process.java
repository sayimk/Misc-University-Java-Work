//Written by Sayim Khan
package CO2017.exercise2.sk619;

public class Process extends Object implements Runnable {
	
	//Class variables
	int Address =-1; 			//memory address
	char ID; 				//process ID
	int Size; 				//process memory requirement
	int Runtime_length; 	//process runtime length
	MemManager m; 			//current memManager
	
	//run method for Runnable
	@Override
	public void run() {
		System.out.println(this.toString()+" waiting to run.");
		m.allocate(this); //Runs m's allocate method on THIS Process
		System.out.println(this.toString()+" running.");

		//puts thread to sleep
		try {
			Thread.sleep(Runtime_length*100); //this simulated the process and sleeps for the runtimes duration 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		m.free(this); //Runs m's allocate method on THIS Process
		System.out.println(toString()+" has finished.");
	}
	
	//Constructor
	public Process(MemManager m, char i, int s, int r){
		this.m = m;
		ID = i;
		Size = s;
		Runtime_length=r;
	}
	
	//returns Size
	public int getSize(){
		return Size;
	}
	
	//returns address
	public int getAddress(){
		return Address;
	}
	
	//returns ID
	public char  getID(){
		return ID;
	}
	
	//sets Memory address
	public void setAddress(int a){
		Address =a;
	}
	
	//toString method returns the class as a string
	public String toString(){
		
		//constructing output string
		String output= ID+":";
		
		if (Address==-1)
			output =ID+":"+"  U"+"+"+padding2(Size); //if there isn't an address, will default to U
		else output =ID+":"+padding3(Address)+"+"+padding2(Size); //else outputs all required info with padding via padding2 and 3
		
		return output;
	}
	
	//padding for 3 characters
	public String padding3(int i){
		String padded ="";
		if (i<10)
			padded="  "+Integer.toString(i);
		if ((i>9)&&(i<100))
			padded=" "+Integer.toString(i);
		if (i>99)
			padded=Integer.toString(i);
		
		return padded;
	}
	
	//padding for 2 characters
	public String padding2(int i){
		String padded ="";
		if (i<9)
			padded=" "+Integer.toString(i);
		if (i>9)
			padded=Integer.toString(i);
		
		return padded;
	}
}
