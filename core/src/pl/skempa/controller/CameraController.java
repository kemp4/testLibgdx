package pl.skempa.controller;

/**
 * Created by skempa on 16.10.2017.
 */

public interface CameraController {

    void zoomCamera(int amount);

    void moveCamera(int i, int deltaY);
}
