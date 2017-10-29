package pl.skempa.util;

import com.badlogic.gdx.math.Vector2;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.skempa.model.object.Building;

/**
 * Created by skempa on 29.09.2017.
 */

public interface XmlUtil {
    /**
     * Method for reading data from file to map objects
     * @param input input data with data about map
     * @return List of object parsed from input
     * @throws IOException
     */
    List<Building> readXml(InputStream input) throws IOException;

}
