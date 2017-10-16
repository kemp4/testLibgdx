package pl.skempa.libgdxtest.test;

import org.junit.Test;

import java.io.IOException;

import pl.skempa.Building;
import pl.skempa.model.DegreePosition;
import pl.skempa.object.ObjectsDataAPIWrapper;
import pl.skempa.object.OpenStreetMapAPIWrapper;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mymon on 2017-10-08.
 */

public class openStreetMapApiTest {
    @Test
    public void oneEqualsOne() throws IOException {
        ObjectsDataAPIWrapper apiWrapper= new OpenStreetMapAPIWrapper();
        apiWrapper.getObjects(new DegreePosition(18,50));
    }

}
