package pl.skempa.object;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Mymon on 2017-10-16.
 */

public interface MapCamera {
    Vector3 getPosition();
    void zoomCamera(float amount);
    void moveCamera(float deltaX, float deltaY, float deltaZ);
    void moveCamera(Vector3 vector3);
    Matrix4 getMatrix();

    void setPosition(Vector3 vector3);
}
