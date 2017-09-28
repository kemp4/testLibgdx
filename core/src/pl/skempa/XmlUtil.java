package pl.skempa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class XmlUtil {

	Array<Element> xmlNodes;
	Map<Long,Node> nodes = new HashMap<Long,Node>();

	public List<Building> readXml(InputStream input) throws IOException {
		List<Building> buildings = new ArrayList<Building>();
		XmlReader xmlReader=new XmlReader();
		Element xmlRoot = xmlReader.parse(input);
		readXmlNodeFields(xmlRoot);
		readBuildings(buildings, xmlRoot);
		return buildings;
	}

	private void readBuildings(List<Building> buildings, Element xmlRoot) {
		Array<Element> xmlWays;
		xmlWays = xmlRoot.getChildrenByName("way");
		for (Element xmlWay : xmlWays) {
			Building building = toBuilding(xmlWay);
			buildings.add(building);
		}
	}

	private void readXmlNodeFields(Element xmlRoot) {
		xmlNodes = xmlRoot.getChildrenByName("node");
		for (Element xmlNode : xmlNodes) {
			Node node =toNode(xmlNode);
			nodes.put(node.getId(),node);
		}
	}

	private Building toBuilding(Element xmlWay) {
		Building building = new Building();

		//TODO get streetName and houseNumber
		int houseNumber = xmlWay.getInt("addr:housenumber",0);
		String streetName = xmlWay.getAttribute("addr:street", "not named street");


		Array<Element> xmlPoints = xmlWay.getChildrenByName("nd");
		List<Vector3> points = toPoints(xmlPoints);
		building.setHouseNumber(houseNumber);
		building.setStreetName(streetName);
		building.setWallPoints(points);
		return building;
	}

	private List<Vector3> toPoints(Array<Element> xmlPointsInOrder) {
		List<Vector3> points= new ArrayList<Vector3>();
		for(Element xmlPoint : xmlPointsInOrder) {
			long refId = Long.parseLong((xmlPoint.getAttribute("ref")));
			Node node = nodes.get(refId);
			Vector3 point = new Vector3(node.getLon() , node.getLat(),0.f);
			points.add(point);
		}
		return points;
	}

	private Node toNode(Element xmlNode) {
		Node node = new Node();
		node.setId( Long.parseLong((xmlNode.getAttribute("id"))));
		node.setLat(xmlNode.getFloatAttribute("lat"));
		node.setLon(xmlNode.getFloatAttribute("lon"));
		node.setVisible(xmlNode.getBooleanAttribute("visible"));
		return node;
	}


}
