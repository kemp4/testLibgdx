package pl.skempa;


import java.util.List;

import com.badlogic.gdx.math.Vector3;


public class Building {
	
	protected List<Vector3> wallPoints;
	protected int houseNumber;
	protected String streetName; 

	public List<Vector3> getWallPoints() {
		return wallPoints;
	}
	public void setWallPoints(List<Vector3> wallPoints) {
		this.wallPoints = wallPoints;
	}
	public int getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
}
