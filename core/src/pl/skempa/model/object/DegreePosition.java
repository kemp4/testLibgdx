package pl.skempa.model.object;

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

    private StringBuilder asParam(int degree, int minute){
        return(new StringBuilder().append(degree).append(",").append(minute));
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
}
