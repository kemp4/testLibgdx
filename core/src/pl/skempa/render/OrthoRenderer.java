package pl.skempa.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;
import java.util.List;

import pl.skempa.Building;
import pl.skempa.object.ObjectsManager;

/**
 * Created by Mymon on 2017-10-08.
 */

public class OrthoRenderer implements ObjectsRenderer {

    ShapeRenderer shapeRenderer;
    ObjectsManager objectsManager;

    public OrthoRenderer( ObjectsManager objectsManager) {
        shapeRenderer = new ShapeRenderer();
        this.objectsManager = objectsManager;
    }

    @Override
    public void init() {

    }

    @Override
    public void renderObjects(Camera camera) {
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        drawBuildings();
        shapeRenderer.end();
    }

    private void drawBuildings() {
        List<Building> buildings = objectsManager.getObjects();
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
