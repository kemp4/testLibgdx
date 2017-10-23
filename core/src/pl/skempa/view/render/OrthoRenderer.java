package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;
import java.util.List;

import pl.skempa.model.object.Building;
import pl.skempa.model.Model;

/**
 * Created by Mymon on 2017-10-08.
 */

public class OrthoRenderer implements ObjectsRenderer {

    private ShapeRenderer shapeRenderer;


    public OrthoRenderer() {
        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void renderObjects(Model model) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setProjectionMatrix(model.getCameraMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        drawBuildings(model);
        shapeRenderer.end();
    }

    private void drawBuildings(Model model) {
        List<Building> buildings = model.getObjects();
        for (Building building : buildings) {
            drawBuilding(building);
        }
    }

    private void drawBuilding(Building building) {
        List<Vector3> points = building.getWallPoints();
        Iterator<Vector3> pointsIterator = points.iterator();
        Vector3 previousPoint = pointsIterator.next();
        while(pointsIterator.hasNext()) {
            Vector3 actualPoint = pointsIterator.next();
            shapeRenderer.line(previousPoint,actualPoint);
            previousPoint=actualPoint;
        }
    }

}
