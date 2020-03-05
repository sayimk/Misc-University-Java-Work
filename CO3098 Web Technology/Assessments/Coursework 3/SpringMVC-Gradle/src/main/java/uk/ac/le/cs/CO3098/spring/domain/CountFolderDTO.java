package uk.ac.le.cs.CO3098.spring.domain;

public class CountFolderDTO {

	private int Direct = 0;
	private int Indirect = 0;
	
	public CountFolderDTO() {
		
	}
	
	public CountFolderDTO(int Direct, int Indirect) {
		this.Direct = Direct;
		this.Indirect = Indirect;
	}
	
	public int getDirect() {
		return Direct;
	}
	
	public int getIndirect() {
		return Indirect;
	}
	

}
