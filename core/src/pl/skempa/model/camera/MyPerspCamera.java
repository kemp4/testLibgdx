package pl.skempa.model.camera;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by szymk on 12/8/2017.
 */

public class MyPerspCamera implements MapCamera {
    Camera camera;
    private static final float resizeCameraSpeed = 0.05f;
    private static final float moveCameraSpeed = 0.002f;

    public MyPerspCamera(){
        camera = new PerspectiveCamera(67,5,5);
        camera.far=50f;
        camera.near = 0.05f;
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
        camera.translate(0,0,intAmount*camera.position.z*resizeCameraSpeed);
    }

    @Override
    public void moveCamera(float deltaX, float deltaY, float deltaZ) {
        camera.translate(deltaX*camera.viewportWidth*moveCameraSpeed,deltaY*camera.viewportHeight*moveCameraSpeed,deltaZ);
    }

    @Override
    public void moveCamera(Vector3 vector3) {
        camera.translate(vector3);
    }

    @Override
    public Matrix4 getMatrix() {
        camera.update();
        return camera.combined;
    }
// todo smt no yes
    @Override
    public void setPosition(Vector3 position) {
        Vector3 targetPos = new Vector3(position);
        targetPos.sub(camera.position);
        camera.translate(targetPos);
        camera.lookAt(0f,3f,0f);
    }

    @Override
    public Camera getLibgdxCamera() {
        return camera;
    }
}
