package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.skempa.model.Model;
import pl.skempa.model.object.rawdata.OsmRawDataSet;

/**
 * Created by Mymon on 2017-10-08.
 */

public class SimpleOrthoRenderer implements ObjectsRenderer {

    private ShapeRenderer shapeRenderer;

    private boolean firts = true;
    private OsmRawDataSet dataSet;

    public SimpleOrthoRenderer() {
        shapeRenderer = new ShapeRenderer();

    }


    @Override
    public void renderObjects(Model model) {

        if (firts){
            firts=false;
            model.getMesh();
            dataSet= model.getOsmRawDataSet();
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(model.getCameraMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);

        drawWays();
        shapeRenderer.end();
    }

    @Override
    public void renderStage(Stage stage) {

    }

    private void drawWays() {

        Map<Long, Way> ways = dataSet.getWays();
        for (Map.Entry<Long,Way> way : ways.entrySet()) {
            drawWay(way.getValue());
        }
    }


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
            Vector3 actualPoint = toPoint(actualNode);
            shapeRenderer.line(previousPoint,actualPoint);
            previousPoint=actualPoint;
        }
    }

    private Vector3 toPoint(WayNode wayNode) {
        Node node = dataSet.getNodes().get(wayNode.getNodeId());
        return new Vector3(normalizePosition(node,dataSet.getBound()),0);
    }
    private static Vector2 normalizePosition(Node node, Bound bound) {
        double x =  (node.getLongitude() - bound.getLeft())*100;
        double y =  (node.getLatitude() - bound.getBottom())*100;
        return new Vector2((float)x,(float)y);
    }
}
