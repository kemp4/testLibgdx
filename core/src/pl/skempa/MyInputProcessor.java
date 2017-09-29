package pl.skempa;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by skempa on 29.09.2017.
 */

public class MyInputProcessor implements InputProcessor {
    LibgdxTest libgdxApp;
    boolean touched=false;
    Vector2 oldMousePos=null;

    public MyInputProcessor(LibgdxTest libgdxTest) {
        this.libgdxApp=libgdxTest;
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
        libgdxApp.moveCamera(-screenX+oldMousePos.x,screenY-oldMousePos.y);
        oldMousePos=new Vector2(screenX,screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        oldMousePos=new Vector2(screenX,screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        libgdxApp.resizeCamera(amount);
        return true;
    }
}
