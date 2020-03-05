//Written by Sayim Khan
package CO2017.exercise2.sk619;

public class FirstFitMemManager extends MemManager {

	FirstFitMemManager(int s) {
		super(s);
	}

	@Override
	int findSpace(int s) {
		
		for (int i=0; i<_memory.length;i++){
			if(countFreeSpaceAt(i)>=s){
				return i;
			}
			
		}
		
		return -1;
	}
	

}
