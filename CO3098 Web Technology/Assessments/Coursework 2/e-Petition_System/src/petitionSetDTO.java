//simple Data Transfer Object for carrying petition Data

public class petitionSetDTO {

	String petitionName= "";
	int id = 0;
	String petitionContents = "";
	String creator = "";
	String date="";
	int currentVotes = 0;
	
	public petitionSetDTO(int Id, String PetitionName, String PetitionContents, String Date,String Creator,
			int VoteNumber) {
		id =Id;
		petitionName = PetitionName;
		petitionContents = PetitionContents;
		creator = Creator;
		currentVotes = VoteNumber;
		date=Date;
		
	}
	
	
	public int getId() {
		
		return id;
	}
	
	public String getPetitionName() {
		
		return petitionName;
	}
	
	public String getDate() {
		
		return date;
	}
	
	public String getPetitionContent() {
		return petitionContents;
	}
	
	public String getCreator() {
		
		return creator;
	}
	
	public int getCurrentVotes() {
		return currentVotes;
	}
}
