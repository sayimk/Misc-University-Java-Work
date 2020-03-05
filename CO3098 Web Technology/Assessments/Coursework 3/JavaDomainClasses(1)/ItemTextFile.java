package uk.ac.le.cs.CO3098.cw3.domain;

import uk.ac.le.cs.CO3098.cw3.domain.BookmarkEntity.Type;

public class ItemTextFile extends Item{
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ItemTextFile(String title, String text) {
		super(title);
		setType(Type.TEXTFILE);
		this.text = text;
	}
	String text;


}
