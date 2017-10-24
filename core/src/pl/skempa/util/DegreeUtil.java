package pl.skempa.util;

import com.badlogic.gdx.math.Vector3;

import pl.skempa.model.object.*;

/**
 * Created by Mymon on 2017-10-23.
 */

public class DegreeUtil {
    public static String asApiParam(Vector3 position) {
        DegreePosition degreePosition = new DegreePosition(position);
        return degreePosition.asApiParam();
    }
    public static String asApiBBoxParam(Vector3 position,Vector3 positionMax) {
        DegreePosition degreePosition = new DegreePosition(position);
        DegreePosition degreeMaxPosition = new DegreePosition(positionMax);
        return new StringBuilder()
                 .append(degreePosition
                 .asApiParam())
                 .append(",")
                 .append(degreeMaxPosition.asApiParam())
                 .toString();
    }

}
