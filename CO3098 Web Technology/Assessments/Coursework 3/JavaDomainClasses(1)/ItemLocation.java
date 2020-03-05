package uk.ac.le.cs.CO3098.cw3.domain;

import uk.ac.le.cs.CO3098.cw3.domain.BookmarkEntity.Type;

public class ItemLocation extends Item{
	
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public ItemLocation(String name,double latitude, double longitude) {
		super(name);
		setType(Type.LOCATION);
		Latitude = latitude;
		this.longitude = longitude;
	}
	
	double Latitude;
	double longitude;

}
