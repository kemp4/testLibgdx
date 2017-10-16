package pl.skempa.object;

import com.badlogic.gdx.graphics.Camera;

import java.util.List;

import pl.skempa.Building;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsManager {

    void init();

    List<Building> getObjects();

    void update(Camera camera);
}
