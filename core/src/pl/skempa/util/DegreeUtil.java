package pl.skempa.util;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Mymon on 2017-10-23.
 */

public class DegreeUtil {

    /**
     * Method for creating openStreetMapApi bounding box param string from two Vectors
     * @param position left bottom corner of boundingBox
     * @param positionMax right top cornen of boundingBox
     * @return string which we can use as OpenStreeMapApi
     */
    public static String asApiBBoxParam(Vector3 position,Vector3 positionMax) {
        return new StringBuilder()
                 .append(asApiParam(position))
                 .append(",")
                 .append(asApiParam(positionMax))
                 .toString();
    }
    private static String asApiParam(Vector3 position) {
        return new StringBuilder()
                .append(position.x)
                .append(",")
                .append(position.y)
                .toString();
    }

}
