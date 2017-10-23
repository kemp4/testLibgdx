package pl.skempa.model.apiwrappers;

import java.io.IOException;
import java.util.List;

import pl.skempa.model.object.Building;
import pl.skempa.model.object.DegreePosition;


/**
 * Created by Mymon on 2017-10-16.
 */

public interface ObjectsDataAPIWrapper {
    List<Building> getObjects(DegreePosition position) throws IOException;
}
