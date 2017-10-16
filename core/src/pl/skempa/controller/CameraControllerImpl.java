package pl.skempa.controller;

import pl.skempa.object.MapCamera;
import pl.skempa.object.ObjectsManager;
import pl.skempa.render.ObjectsRenderer;

/**
 * Created by skempa on 16.10.2017.
 */

public class CameraControllerImpl implements CameraController {
    private ObjectsManager objectsManager;
    private MapCamera camera;

    public CameraControllerImpl(ObjectsManager objectsManager, MapCamera camera) {

        this.objectsManager = objectsManager;
        this.camera = camera;
    }

    @Override
    public void zoomCamera(int amount) {
        camera.zoomCamera(amount);
    }

    @Override
    public void moveCamera(int deltaX, int deltaY) {
        camera.moveCamera(deltaX,deltaY,0);
    }
}
