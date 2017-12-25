package pl.skempa.model.camera;

<<<<<<< HEAD
import com.badlogic.gdx.graphics.Camera;
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
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
<<<<<<< HEAD

    Camera getLibgdxCamera();
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
}
