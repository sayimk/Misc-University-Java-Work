//Written by Sayim Khan
package CO2017.exercise2.sk619;
public abstract class MemManager{

	//class Variables
	
	boolean _changed; 	//if memory state has been changed since last toString
	int _largestSpace; 	//size of the largest free memory block
	char[] _memory; 	//array representation of the memory
	
	//class constructor
	MemManager(int s){
		//creating memory
		_memory = new char[s];
		
		//initializing memory
		for (int i=0; i<_memory.length; i++){
			_memory[i]='.';
		}
		
		//initializing other values
		_largestSpace=_memory.length;
		_changed=true;
	}
	
	//allocating a memory location to a process
	public synchronized void allocate(Process p){

		//keep looping until a largest enough space is avaliable
		while(p.Size>_largestSpace){
			try{
				wait();
			}catch(InterruptedException e){}
			 }	

		//free space found, preparing to occupy space
		p.setAddress(findSpace(p.getSize()));
		int nextMemLocation =p.getAddress();
		
		//filling free space with process
		for (int i=0; i<p.getSize(); i++){
			_memory[nextMemLocation]=p.getID();
			nextMemLocation=nextMemLocation+1;
		}
		
		//setting largest space
		_largestSpace=-1;
		for (int i=0; i<_memory.length;i++){
			if(countFreeSpaceAt(i)>_largestSpace){
				_largestSpace=countFreeSpaceAt(i);
			}
		}
		_changed=true;
		notify(); //notify next blocked process
	}
	
	//method used to count the amount of free spaces between occupied blocks
	public int countFreeSpaceAt(int pos){
		int free=0;
		
		//loop though every empty memory block and counts the free block between
		//and the next occupied block
		for (int i=pos; i<_memory.length; i++){
			if(_memory[i]=='.'){
				free=free+1;
			}else return free;
		}
		return free;
	}
	
	//searches for free space, method defined in sub-classes
	abstract int findSpace(int s);
	
	//frees memory from a process
	public synchronized void free(Process p){
		
		//search through the memory array and reset
		for(int i=0; i<_memory.length; i++){
			if(_memory[i]==p.getID())
				_memory[i]='.';
		}
	//recalculating the largest Space	
		_largestSpace=-1;
		for (int i=0; i<_memory.length;i++){
			if(countFreeSpaceAt(i)>_largestSpace){
				_largestSpace=countFreeSpaceAt(i);
			}
		}
		p.setAddress(-1);
		notify();
		_changed=true;
	}
	
	//returns boolean if memory state has been changed since last toString
	public boolean isChanged(){
		return _changed;
	}
	
	public String toString(){
		String outputString="";
		
		//looping through until end of array
		for (int i=0; i<_memory.length; i++){
			if((i % 20 == 0)){
				if(i!=0){
				outputString=outputString+'|';
				outputString=outputString+'\n';
				}
				outputString=outputString+padding(i)+'|';
			}
			outputString=outputString+_memory[i];
		}
		outputString=outputString+'|'+'\n'+"ls: "+Integer.toString(_largestSpace);
		_changed=false;
		
		return outputString;
		
	}
	
	//this method is used for setting the padding of any integer input to 1,2 or 0 spaces
	public String padding(int i){
		String padded ="";
		
		if (i<10)
			padded="  "+Integer.toString(i);
		if ((i>9)&&(i<100))
			padded=" "+Integer.toString(i);
		if (i>99)
			padded=Integer.toString(i);
		
		return padded;
	}
}
