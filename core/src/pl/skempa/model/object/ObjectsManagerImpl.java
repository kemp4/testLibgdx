package pl.skempa.model.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.skempa.model.apiwrappers.OpenStreetMapAPIWrapper;
import pl.skempa.util.XmlUtilImpl;

/**
 * Created by Mymon on 2017-10-08.
 */

public class ObjectsManagerImpl implements ObjectsManager {

    private List<Building> buildings = new LinkedList<Building>();

    private DegreePosition position;

    @Override
    public void init() {
        position =new DegreePosition(18.0f,50.0f);
        callApi();
    }

    @Override
    public void update(DegreePosition position) {

            }

    private void callApi() {
        pl.skempa.model.apiwrappers.ObjectsDataAPIWrapper openStreetMapApiWrapper = new OpenStreetMapAPIWrapper();
        try {
            buildings = openStreetMapApiWrapper.getObjects(position);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error with calling open Street Map API");
        }
    }

    @Deprecated
    private void readBuildings() {
        buildings=new ArrayList<Building>();
        FileHandle xmlMap = Gdx.files.internal("mapFiles/mapOchojec.osm");
        try {
            buildings = new XmlUtilImpl().readXml(xmlMap.read());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public List<Building> getObjects() {
        return buildings;
    }



}
