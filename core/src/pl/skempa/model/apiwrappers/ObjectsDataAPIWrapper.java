package pl.skempa.model.apiwrappers;

import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.List;

import pl.skempa.model.object.Building;
import pl.skempa.model.object.rawdata.RawDataSet;


/**
 * Created by Mymon on 2017-10-16.
 */

public interface ObjectsDataAPIWrapper {
    RawDataSet getObjects(Vector3 position) throws ApiWrapperException;
}
