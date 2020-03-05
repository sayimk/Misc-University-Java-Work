package uk.ac.le.cs.CO3098.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LinksDomain {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @Column
	private String linkName;
    
    @Column
	private String linkUrl;
    
    
    public LinksDomain() {
    	
    }
    
    public LinksDomain(String linkName, String linkUrl) {
    	this.linkName = linkName;
    	this.linkUrl=linkUrl;
    }
    
    public void setLinkURL(String URL) {
    	linkUrl = URL;
    }
    
    public String getLinkUrl() {
    	return linkUrl;
    }
    
    public void setLinkName(String linkName) {
    	this.linkName = linkName;
    }
    
    public String getLinkName() {
    	return linkName;
    }
    
    
	
}
