package pl.skempa.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.skempa.model.object.rawdata.Node;

public class XmlUtilImpl implements XmlUtil {

    Array<Element> xmlNodes;
    Map<Long, Node> nodes = new HashMap<Long, Node>();

    @Override
    public List<pl.skempa.model.object.Building> readXml(InputStream input) throws IOException {
        List<pl.skempa.model.object.Building> buildings = new ArrayList<pl.skempa.model.object.Building>();
        XmlReader xmlReader=new XmlReader();
        Element xmlRoot = xmlReader.parse(input);
        readXmlNodeFields(xmlRoot);
        readBuildings(buildings, xmlRoot);
        return buildings;
    }


    private Vector2 readBounds(Element xmlRoot) {
        Element bounds;

        bounds = xmlRoot.getChildByName("bounds");
        float yCameraPos = bounds.getFloatAttribute("minlat",0.f);
        float xCameraPos = bounds.getFloatAttribute("minlon",0.f);
        return new Vector2(xCameraPos,yCameraPos);
    }

    private void readBuildings(List<pl.skempa.model.object.Building> buildings, Element xmlRoot) {
        Array<Element> xmlWays;
        xmlWays = xmlRoot.getChildrenByName("way");
        for (Element xmlWay : xmlWays) {
            pl.skempa.model.object.Building building = toBuilding(xmlWay);
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

    private pl.skempa.model.object.Building toBuilding(Element xmlWay) {
        pl.skempa.model.object.Building building = new pl.skempa.model.object.Building();

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
