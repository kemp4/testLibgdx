package pl.skempa.model;

import com.badlogic.gdx.math.Matrix4;

import java.util.List;
import java.util.Map;

import pl.skempa.model.object.Building;
import pl.skempa.model.object.rawdata.Way;

/**
 * Created by Mymon on 2017-10-22.
 */

public interface Model {
    void init();
    void update();
    Matrix4 getCameraMatrix();
    Map<Long, Way> getObjects();

    void moveCamera(int deltaX, int deltaY, int i);
    void zoomCamera(int amount);


}
