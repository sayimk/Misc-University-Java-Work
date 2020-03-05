package uk.ac.le.cs.CO3098.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TextDomain {

	   @Id
	    @GeneratedValue
	    @Column(name = "id")
	    private Integer id;
	    
	    @Column
		private String fileName;
	    
	    @Column
		private String fileContents;
	    
	    public TextDomain() {
	    	
	    }
	    
	    public TextDomain(String fileName, String FileTextContents) {
	    	this.fileName = fileName;
	    	this.fileContents=FileTextContents;
	    }
	    
	    public void setFileContents(String text) {
	    	fileContents = text;
	    }
	    
	    public String getFileContents() {
	    	return fileContents;
	    }
	    
	    public void setFileName(String linkName) {
	    	this.fileName = linkName;
	    }
	    
	    public String getFileName() {
	    	return fileName;
	    }
	    
	    
}
