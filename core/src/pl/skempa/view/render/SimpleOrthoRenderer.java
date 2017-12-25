package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
<<<<<<< HEAD
<<<<<<< HEAD
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

=======
import com.badlogic.gdx.math.Vector3;

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
import com.badlogic.gdx.math.Vector3;

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.skempa.model.Model;
<<<<<<< HEAD
<<<<<<< HEAD
import pl.skempa.model.object.rawdata.OsmRawDataSet;

=======
import pl.skempa.model.object.rawdata.Way;
import pl.skempa.model.object.rawdata.Node;
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
import pl.skempa.model.object.rawdata.Way;
import pl.skempa.model.object.rawdata.Node;
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
/**
 * Created by Mymon on 2017-10-08.
 */

public class SimpleOrthoRenderer implements ObjectsRenderer {

    private ShapeRenderer shapeRenderer;
<<<<<<< HEAD
<<<<<<< HEAD
    private boolean firts = true;
    private OsmRawDataSet dataSet;

    public SimpleOrthoRenderer() {
        shapeRenderer = new ShapeRenderer();

=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c


    public SimpleOrthoRenderer() {
        shapeRenderer = new ShapeRenderer();
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    }


    @Override
    public void renderObjects(Model model) {
<<<<<<< HEAD
<<<<<<< HEAD
        if (firts){
            firts=false;
            model.getMesh();
            dataSet= model.getOsmRawDataSet();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(model.getCameraMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
<<<<<<< HEAD
<<<<<<< HEAD
        drawWays();
        shapeRenderer.end();
    }

    private void drawWays() {

        Map<Long,Way> ways = dataSet.getWays();
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
        drawWays(model);
        shapeRenderer.end();
    }

    private void drawWays(Model model) {
        Map<Long,Way> ways = model.getObjects();
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
        for (Map.Entry<Long,Way> way : ways.entrySet()) {
            drawWay(way.getValue());
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    public static final String BUILDING = "building";
    public static final String WAY = "highway";
    private void drawWay(Way way) {

        boolean isBuilding = false;
        boolean isWay = false;
        for( Tag tag : way.getTags()){
            if(tag.getKey().equals(BUILDING)){
                isBuilding = true;
            }
            else if(tag.getKey().equals(WAY)){
                isWay = true;
            }
        }
        if(isBuilding) {
            shapeRenderer.setColor(1.f, 0f, 0f, 1);
        }else if(isWay){
            shapeRenderer.setColor(0, 0, 1, 1);
        }else{
            shapeRenderer.setColor(0, 1, 0, 0.5f);
        }

        List<WayNode> nodes = way.getWayNodes();
        Iterator<WayNode> pointsIterator = nodes.iterator();
        WayNode previousNode = pointsIterator.next();

        Vector3 previousPoint = toPoint(previousNode);
        while(pointsIterator.hasNext()) {
            WayNode actualNode = pointsIterator.next();
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    private void drawWay(Way way) {
        if(way.getTags().containsKey("building")){
            shapeRenderer.setColor(0, 0, 1, 1);
        }else if(way.getTags().containsKey("highway")){
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
        }
        else{
            shapeRenderer.setColor(1, 1, 0, 1);
        }

        List<Node> nodes = way.getNodes();
        Iterator<Node> pointsIterator = nodes.iterator();
        Node previousNode = pointsIterator.next();
        Vector3 previousPoint = toPoint(previousNode);
        while(pointsIterator.hasNext()) {
            Node actualNode = pointsIterator.next();
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
            Vector3 actualPoint = toPoint(actualNode);
            shapeRenderer.line(previousPoint,actualPoint);
            previousPoint=actualPoint;
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private Vector3 toPoint(WayNode wayNode) {
        Node node = dataSet.getNodes().get(wayNode.getNodeId());
        return new Vector3(normalizePosition(node,dataSet.getBound()),0);
    }
    private static Vector2 normalizePosition(Node node, Bound bound) {
        double x =  (node.getLongitude() - bound.getLeft())*100;
        double y =  (node.getLatitude() - bound.getBottom())*100;
        return new Vector2((float)x,(float)y);
    }
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    private Vector3 toPoint(Node actualNode) {
        return new Vector3(actualNode.getLon(),actualNode.getLat(),0);
    }

<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
}
