
public class CommentDTO {

	private String comment="";
	private String MP = "";
	
	public 	CommentDTO(String comment, String MP) {
		this.comment = comment;
		this.MP = MP;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getMP() {
		return MP;
	}
}
