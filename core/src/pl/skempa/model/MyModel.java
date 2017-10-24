package pl.skempa.model;

import com.badlogic.gdx.math.Matrix4;

import java.util.List;

import pl.skempa.model.object.Building;
import pl.skempa.model.camera.MapCamera;
import pl.skempa.model.camera.MyMapCamera;
import pl.skempa.util.DegreePosition;
import pl.skempa.model.object.ObjectsManager;
import pl.skempa.model.object.ObjectsManagerImpl;

/**
 * Created by Mymon on 2017-10-22.
 */

public class MyModel implements Model {
    //TODO refactor name later
    private MapCamera camera;

    private ObjectsManager objectsManager;


    @Override
    public void init() {
        objectsManager = new ObjectsManagerImpl();
        objectsManager.init();

        camera = new MyMapCamera();
        camera.setPosition(objectsManager.getObjects().get(0).getWallPoints().get(1));
    }

    @Override
    public void update() {
        objectsManager.update(camera.getPosition());
    }


    @Override
    public Matrix4 getCameraMatrix() {
        return camera.getMatrix();
    }

    @Override
    public List<Building> getObjects() {
        return objectsManager.getObjects();
    }

    @Override
    public void moveCamera(int deltaX, int deltaY, int i) {
        camera.moveCamera(deltaX,deltaY,0);
    }

    @Override
    public void zoomCamera(int amount) {
        camera.zoomCamera(amount);
    }
}
