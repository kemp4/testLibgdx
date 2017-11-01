package pl.skempa.util;


import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pl.skempa.model.object.Building;
import pl.skempa.model.object.rawdata.Node;
import pl.skempa.model.object.rawdata.RawDataSet;
import pl.skempa.model.object.rawdata.Way;


/**
 * Created by szymk on 10/28/2017.
 */

public class XmlUtilBySax implements XmlUtil {

    Map<Long, Node> nodes= new HashMap<Long, Node>();
    Map<Long, Way> ways = new HashMap<Long, Way>();
    Way way;
    private enum ReadingMode {
        BASE, WAY, RELATION;
    }
    ReadingMode readingMode=ReadingMode.BASE;

    @Override
    public RawDataSet readXml(InputStream input) throws IOException {
        try {
            parse(input);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return new RawDataSet(nodes,ways);
    }

    private void parse(InputStream input) throws ParserConfigurationException, SAXException, IOException {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new MyHandler();
            saxParser.parse(input,handler);

    }

    private class MyHandler extends DefaultHandler{
        private List<Node> buildingNodes;

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {

            if(readingMode==ReadingMode.BASE) {
                if (qName.equalsIgnoreCase("node")) {
                    addNode(attributes);
                } else if (qName.equalsIgnoreCase("way")) {
                    addWay(attributes);
                    readingMode =ReadingMode.WAY;

                } else if (qName.equalsIgnoreCase("relation")) {
                    readingMode= ReadingMode.RELATION;
                }
            }else if(readingMode==ReadingMode.WAY) {
                if (qName.equalsIgnoreCase("tag")) {
                    way.getTags().put(attributes.getValue("k"),attributes.getValue("v"));
                }else if(qName.equalsIgnoreCase("nd")){
                    long refId = Long.parseLong(attributes.getValue("ref"));
                    Node node = nodes.get(refId);
                    way.getNodes().add(node);
                }
            }else if(readingMode==ReadingMode.RELATION){
                // TODO read relations
            }
        }

        private void addWay(Attributes attributes) {
            way = new Way();
            long id = Long.parseLong(attributes.getValue("id"));
            way.setId(id);
            way.setTags(new HashMap<String,String>());
            way.setNodes(new LinkedList<Node>());
            ways.put(way.getId(),way);
        }

        private void addNode(Attributes attributes) {
            Node node = new Node();
            long id = Long.parseLong(attributes.getValue("id"));
            node.setId(id);
            node.setLat(Float.parseFloat(attributes.getValue("lat")));
            node.setLon(Float.parseFloat(attributes.getValue("lon")));
            node.setVisible(Boolean.parseBoolean("visible"));
            nodes.put(id,node);
        }


        @Override
        public void endElement(String uri,
                               String localName, String qName) throws SAXException {
            if (qName.equalsIgnoreCase("way")||qName.equalsIgnoreCase("relation")) {
                readingMode = ReadingMode.BASE;
            }
       }



    }

}