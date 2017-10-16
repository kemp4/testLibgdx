package pl.skempa.model;

/**
 * Created by Mymon on 2017-10-16.
 */

public class DegreePosition {
    private int latDegree;
    private int latMinute;
    private int lonDegree;
    private int lonMinute;

    public DegreePosition(float lat, float lon) {
        latDegree = extractDegree(lat);
        lonDegree = extractDegree(lon);
        latMinute = extractMinute(lat);
        lonMinute = extractMinute(lon);
    }

    private int extractMinute(float coordinate) {
        return (int)((coordinate-extractDegree(coordinate))*60.f);
    }

    private int extractDegree(float coordinate) {
        return (int)coordinate;
    }

}
