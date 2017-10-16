package pl.skempa.controller;

import pl.skempa.object.ObjectsManager;
import pl.skempa.render.ObjectsRenderer;

/**
 * Created by skempa on 16.10.2017.
 */

public class ControllerImpl implements Controller {
    private ObjectsManager objectsManager;
    private ObjectsRenderer renderer;

    public ControllerImpl(ObjectsManager objectsManager, ObjectsRenderer renderer) {

        this.objectsManager = objectsManager;
        this.renderer = renderer;
    }

    @Override
    public void zoomCamera(int amount) {
        renderer.zoomCamera(amount);
    }

    @Override
    public void moveCamera(int deltaX, int deltaY) {
        renderer.moveCamera(deltaX,deltaY,0);
    }
}
