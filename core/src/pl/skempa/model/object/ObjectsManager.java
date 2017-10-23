package pl.skempa.model.object;



import java.util.List;

/**
 * Created by Mymon on 2017-10-08.
 */

public interface ObjectsManager {

    void init();
    void update(DegreePosition position);
    List<Building> getObjects();

}
