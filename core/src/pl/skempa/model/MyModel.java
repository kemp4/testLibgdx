package pl.skempa.model;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import pl.skempa.model.camera.MapCamera;
import pl.skempa.model.camera.MyMapCamera;
import pl.skempa.model.object.ObjectsManager;
import pl.skempa.model.object.ObjectsManagerImpl;
import pl.skempa.model.object.rawdata.Way;

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
        camera.setPosition(new Vector3(139.9f,35.66f,0f));
    }
        //wsg84

    @Override
    public void update() {
        objectsManager.update(camera.getPosition());
    }


    @Override
    public Matrix4 getCameraMatrix() {
        return camera.getMatrix();
    }

    @Override
    public Map<Long, Way> getObjects() {
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
