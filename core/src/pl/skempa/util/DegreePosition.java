package pl.skempa.util;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by Mymon on 2017-10-16.
 */

public  class DegreePosition {
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

    public DegreePosition(Vector3 position) {
        this(position.x,position.y);
    }

    private int extractMinute(float coordinate) {
        return (int)((coordinate-extractDegree(coordinate))*60.f);
    }

    private int extractDegree(float coordinate) {
        return (int)coordinate;
    }

    private StringBuilder asParam(int degree, int minute){
        return(new StringBuilder().append(degree).append(".").append(minute));
    }

    public String asApiParam() {
        return new StringBuilder()
                .append(latAsParam())
                .append(",")
                .append(lonAsParam())
                .toString();
    }

    private StringBuilder latAsParam() {
        return asParam(latDegree,latMinute);
    }
    private StringBuilder lonAsParam() {
        return asParam(lonDegree,lonMinute);
    }

    public int latMinuteDistance(DegreePosition position) {
        return Math.abs(60*(this.latDegree-position.latDegree)+this.latMinute-position.latMinute);
    }
}
