package pl.skempa.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import pl.skempa.model.Model;

//import pl.skempa.controller.CameraController;

/**
 * Created by skempa on 29.09.2017.
 */

public class MyInputProcessor implements InputProcessor,GestureDetector.GestureListener  {

    //private Controller controller;
    private Model model;
    private final static float skala = 0.02f;

    public MyInputProcessor(Model model) {
        this.model = model;
    }

    // public MyInputProcessor(Controller controller) {
   //     this.controller = controller;
   // }

    @Override
    public boolean keyDown(int keycode){
        if (keycode == Input.Keys.LEFT){
            model.rotateCamera(Vector3.Z, -10);
        }
        if (keycode == Input.Keys.RIGHT){
            model.rotateCamera(Vector3.Z, 10);
        }
        if (keycode == Input.Keys.UP){
            model.rotateCamera(Vector3.X, 10);
        }
        if (keycode == Input.Keys.DOWN){
            model.rotateCamera(Vector3.X, -10);
        }
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
        model.moveCamera(-Gdx.input.getDeltaX(),Gdx.input.getDeltaY(),0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        model.zoomCamera(amount);
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
        model.zoomCamera(resizeAmount);
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
