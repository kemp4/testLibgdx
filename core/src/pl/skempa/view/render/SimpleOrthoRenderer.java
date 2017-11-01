package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.skempa.model.Model;
import pl.skempa.model.object.rawdata.Way;
import pl.skempa.model.object.rawdata.Node;
/**
 * Created by Mymon on 2017-10-08.
 */

public class SimpleOrthoRenderer implements ObjectsRenderer {

    private ShapeRenderer shapeRenderer;


    public SimpleOrthoRenderer() {
        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void renderObjects(Model model) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(model.getCameraMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        drawWays(model);
        shapeRenderer.end();
    }

    private void drawWays(Model model) {
        Map<Long,Way> ways = model.getObjects();
        for (Map.Entry<Long,Way> way : ways.entrySet()) {
            drawWay(way.getValue());
        }
    }

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
            Vector3 actualPoint = toPoint(actualNode);
            shapeRenderer.line(previousPoint,actualPoint);
            previousPoint=actualPoint;
        }
    }

    private Vector3 toPoint(Node actualNode) {
        return new Vector3(actualNode.getLon(),actualNode.getLat(),0);
    }

}
