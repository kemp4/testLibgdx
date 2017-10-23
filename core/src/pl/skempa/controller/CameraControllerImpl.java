package pl.skempa.controller;

import pl.skempa.model.Model;

/**
 * Created by skempa on 16.10.2017.
 */

public class CameraControllerImpl implements CameraController {

    Model model;

    public CameraControllerImpl(Model model) {
        this.model=model;
    }

    @Override
    public void zoomCamera(int amount) {

        model.zoomCamera(amount);
    }

    @Override
    public void moveCamera(int deltaX, int deltaY)
    {
        model.moveCamera(deltaX,deltaY,0);
    }
}
