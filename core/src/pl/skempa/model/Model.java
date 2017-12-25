package pl.skempa.model;

<<<<<<< HEAD
import com.badlogic.gdx.graphics.Camera;
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;

import java.util.List;
import java.util.Map;

import pl.skempa.model.object.Building;
<<<<<<< HEAD
import pl.skempa.model.object.rawdata.OsmRawDataSet;
import pl.skempa.model.object.rawdata.Scene;
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
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
    Mesh getMesh();
<<<<<<< HEAD
    OsmRawDataSet getOsmRawDataSet();

    Scene getThreeDimScene();

    Camera getCamera();
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
}
