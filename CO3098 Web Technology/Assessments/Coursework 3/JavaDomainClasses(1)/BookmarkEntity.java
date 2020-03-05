package uk.ac.le.cs.CO3098.cw3.domain;

public abstract class BookmarkEntity {
	
	enum Type{
		FOLDER,
		LINK,
		LOCATION,
		TEXTFILE
	}

	public BookmarkEntity(String name) {
		super();
		this.name = name;
	}


	public String getPath() {
		return "";
	}


	public boolean isReadOnly() {
		return readOnly;
	}


	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	

	public BookmarkEntity getParentFolder() {
		return parentFolder;
	}


	public void setParentFolder(BookmarkEntity parentFolder) {
		this.parentFolder = parentFolder;
		
		if(parentFolder.getType()==Type.FOLDER) {
			((Folder)parentFolder).add(this);
		}
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}

	
	
	@Override
	public String toString() {
		
		StringBuilder fullpath=new StringBuilder();
		
		BookmarkEntity current=this;
		
		boolean first=true;
		
		while(current!=null) {
			if(first) {
				fullpath.insert(0,current.getName());
				first=false;
			}else {
				fullpath.insert(0, current.getName()+"|");
			}
			
			current=current.getParentFolder();	
		}
		
		return  fullpath.toString();
		
		
	}


	String name;
	boolean readOnly;
	BookmarkEntity parentFolder;
	Type type;


	

}
