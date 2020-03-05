package uk.ac.le.cs.CO3098.cw3.domain;

public class ItemLink  extends Item{
	
	public ItemLink(String name,String uri, String title) {
		super(name);
		setType(Type.LINK);
		this.uri = uri;
		this.title = title;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	String uri;
	String title;

}
