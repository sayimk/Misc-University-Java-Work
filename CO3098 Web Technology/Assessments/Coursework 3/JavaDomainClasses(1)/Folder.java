package uk.ac.le.cs.CO3098.cw3.domain;

import java.util.Vector;


public class Folder extends BookmarkEntity {
	
	
	Vector<BookmarkEntity> list=new 	Vector<>();
	

	public Folder(String name) {
		super(name);
		setType(Type.FOLDER);
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	public void add(BookmarkEntity f) {
		list.add(f);
	}
	
	public void remove(BookmarkEntity f) {
		list.remove(f);
	}

	

}
