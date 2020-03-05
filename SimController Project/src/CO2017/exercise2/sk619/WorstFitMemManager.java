//Written by Sayim Khan
package CO2017.exercise2.sk619;

public class WorstFitMemManager extends MemManager{

	WorstFitMemManager(int s) {
		super(s);
	}

	@Override
	int findSpace(int s) {
		int foundSpace=-1;
		int largest =0;
		for (int i=0; i<_memory.length;i++){
			if(countFreeSpaceAt(i)>largest){
				foundSpace=i;
				largest=countFreeSpaceAt(i);
			}
		}
		return foundSpace;
	}

}
