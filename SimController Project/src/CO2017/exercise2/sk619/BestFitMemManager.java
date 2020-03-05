//Written by Sayim Khan
package CO2017.exercise2.sk619;

public class BestFitMemManager extends MemManager{

	BestFitMemManager(int s) {
		super(s);
	}

	@Override
	int findSpace(int s) {
		
		int foundSpace=-1;
		int largest =_memory.length;
		
		for (int i=0; i<_memory.length;i++){
			if((countFreeSpaceAt(i)>=s)&&(countFreeSpaceAt(i)<largest)){
				foundSpace=i;
				largest=countFreeSpaceAt(i);
			}
		}
		return foundSpace;
	}

}
