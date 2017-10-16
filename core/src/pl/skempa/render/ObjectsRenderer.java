package pl.skempa.render;

import com.badlogic.gdx.graphics.Camera;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsRenderer {
    void init();
    void renderObjects();

    void zoomCamera(float amount);

    void moveCamera(float deltaX, float deltaY, float deltaZ);
}
