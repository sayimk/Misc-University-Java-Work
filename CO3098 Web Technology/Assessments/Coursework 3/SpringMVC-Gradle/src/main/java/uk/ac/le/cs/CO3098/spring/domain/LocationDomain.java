package uk.ac.le.cs.CO3098.spring.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LocationDomain {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;
    
    @Column
	private String locationName;
    
    @Column
	private String latitude;
    
    @Column
	private String longitude;
    
    
    public LocationDomain() {
    	
    }
    
    public LocationDomain(String locationName, String latitude, String longitude) {
    	this.locationName= locationName;
    	this.latitude=latitude;
    	this.longitude=longitude;
    }
    
    public void setLocationName(String locationName) {
    	this.locationName=locationName;
    }
    
    public String getLocationName() {
    	return locationName;
    }
    
    public void setLongitude(String longitude) {
    	this.longitude = longitude;
    }
    
    public void setLatitude(String latitude) {
    	this.latitude=latitude;
    }
    
    public String getLongitude() {
    	return longitude;
    }
    
    public String getLatitude() {
    	return latitude;
    }
    
    public String getLatLngJSON() {
    	String output = "";
    	
    		output= output +"{lat: "+latitude+", lng: "+longitude+"}";
    	
    	return output;
    }
	
}
