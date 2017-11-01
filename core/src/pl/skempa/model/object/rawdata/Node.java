package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class Node {
	private long id;
	private boolean visible;
	private float lat;
	private float lon;
	private HashMap<String,String> tags;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	public HashMap<String, String> getTags() {
		return tags;
	}
	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	public Vector3 getPosition() {
		return new Vector3(lat,lon,0);
	}
}
