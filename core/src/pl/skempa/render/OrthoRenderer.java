package pl.skempa.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.skempa.Building;
import pl.skempa.XmlUtilImpl;
import pl.skempa.object.ObjectsManager;

/**
 * Created by Mymon on 2017-10-08.
 */

public class OrthoRenderer implements ObjectsRenderer {
    private static final float resizeCameraSpeed = 0.03f;
    private static final float moveCameraSpeed = 0.002f;
    ShapeRenderer shapeRenderer;
    ObjectsManager objectsManager;
    Camera camera;
    List<Building> buildings;

    public OrthoRenderer( ObjectsManager objectsManager) {
        camera = new OrthographicCamera(0.01f, 0.005f);
        shapeRenderer = new ShapeRenderer();
        buildings=new ArrayList<Building>();
        FileHandle xmlMap = Gdx.files.internal("mapFiles/mapOchojec.osm");
        this.objectsManager=objectsManager;
        buildings = objectsManager.getObjects();
        // TODO set camera pos
        camera.translate(buildings.get(0).getWallPoints().get(0));
    }

    @Override
    public void init() {

    }

    @Override
    public void renderObjects() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        drawBuildings();
        shapeRenderer.end();
    }

    @Override
    public void zoomCamera(float amount) {
        float intAmount = (int)amount;
        camera.viewportHeight*= 1+intAmount* resizeCameraSpeed;
        camera.viewportWidth*= 1+intAmount* resizeCameraSpeed;
    }

    @Override
    public void moveCamera(float deltaX, float deltaY, float deltaZ) {
        camera.translate(deltaX*camera.viewportWidth*moveCameraSpeed,deltaY*camera.viewportHeight*moveCameraSpeed,0.0f);
    }

    private void drawBuildings() {
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
