package pl.skempa.object;

import java.io.IOException;
import java.util.List;

import pl.skempa.Building;
import pl.skempa.model.DegreePosition;


/**
 * Created by Mymon on 2017-10-16.
 */

public interface ObjectsDataAPIWrapper {
    List<Building> getObjects(DegreePosition position) throws IOException;
}
