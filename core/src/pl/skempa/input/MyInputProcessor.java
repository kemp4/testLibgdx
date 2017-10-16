package pl.skempa.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import pl.skempa.controller.CameraController;

/**
 * Created by skempa on 29.09.2017.
 */

public class MyInputProcessor implements InputProcessor,GestureDetector.GestureListener  {

    //boolean touched=false;
    private final static float skala = 0.02f;
    private CameraController cameraController;



    public MyInputProcessor(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cameraController.moveCamera(-Gdx.input.getDeltaX(),Gdx.input.getDeltaY());
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        cameraController.zoomCamera(amount);
        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        float distanceDelta = distance-initialDistance;
        int resizeAmount = (int)(distanceDelta * skala);
        cameraController.zoomCamera(resizeAmount);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
