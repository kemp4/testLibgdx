package pl.skempa.model.object;



import com.badlogic.gdx.math.Vector3;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import pl.skempa.model.object.rawdata.Way;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsManager {

    void init();
    void update(Vector3 position);
    Map<Long, Way> getObjects();

}
