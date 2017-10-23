package pl.skempa.model;

import com.badlogic.gdx.math.Matrix4;

import java.util.List;

import pl.skempa.model.object.Building;

/**
 * Created by Mymon on 2017-10-22.
 */

public interface Model {
    void init();
    void update();
    Matrix4 getCameraMatrix();
    List<Building> getObjects();

    void moveCamera(int deltaX, int deltaY, int i);
    void zoomCamera(int amount);

}
