package pl.skempa.model.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Mymon on 2017-10-16.
 */
public class MyMapCamera implements MapCamera {
    Camera camera;
    private static final float resizeCameraSpeed = 0.03f;
    private static final float moveCameraSpeed = 0.002f;
    public MyMapCamera(){
        camera = new OrthographicCamera(10f, 10f);

    }

    @Override
    public Vector3 getPosition() {
        return camera.position;
    }

    @Override
    public void zoomCamera(float amount) {
        float intAmount = (int)amount;
        camera.viewportHeight*= 1+intAmount* resizeCameraSpeed;
        camera.viewportWidth*= 1+intAmount* resizeCameraSpeed;
    }

    @Override
    public void moveCamera(float deltaX, float deltaY, float deltaZ) {
            camera.translate(deltaX*camera.viewportWidth*moveCameraSpeed,deltaY*camera.viewportHeight*moveCameraSpeed,0.0f);
    }

    @Override
    public void moveCamera(Vector3 vector) {
        moveCamera(vector.x,vector.y,vector.z);
    }

    @Override
    public Matrix4 getMatrix() {
        camera.update();
        return camera.combined;
    }

    @Override
    public void setPosition(Vector3 vector3) {
        camera.translate(vector3);
    }


}
