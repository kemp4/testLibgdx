package pl.skempa.model.object;



import com.badlogic.gdx.math.Vector3;

import java.util.List;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsManager {

    void init();
    void update(Vector3 position);
    List<Building> getObjects();

}
