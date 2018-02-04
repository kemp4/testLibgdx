package pl.skempa.controller.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import pl.skempa.model.Model;

//import pl.skempa.controller.CameraController;

/**
 * Created by skempa on 29.09.2017.
 */

public class MyInputProcessor implements InputProcessor,GestureDetector.GestureListener  {

    private static final float VELOCITY = 5f;
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
        if (keycode== (Input.Keys.ESCAPE)){
            	model.getSettings().displayGUI=!model.getSettings().displayGUI;

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

    int oldScreenX;
    int oldScreenY;

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        int offsetX = oldScreenX-screenX;
        int offsetY = oldScreenY-screenY;
        oldScreenX = screenX;
        oldScreenY = screenY;

        Camera camera = model.getCamera();
        if(offsetX!=0){
            Vector3 temp = new Vector3(camera.up);
            camera.rotate(temp,offsetX);
        }
        if(offsetY!=0){
            Vector3 temp = new Vector3(camera.direction);
            temp.rotate(camera.up,90);
            camera.rotate(temp,-offsetY);
        }
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
