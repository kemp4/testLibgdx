package pl.skempa.object;



import java.util.List;

import pl.skempa.Building;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsManager {

    void init();

    List<Building> getObjects();

}
