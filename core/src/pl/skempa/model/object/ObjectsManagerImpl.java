package pl.skempa.model.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.skempa.model.apiwrappers.ApiWrapperException;
import pl.skempa.model.apiwrappers.OpenStreetMapAPIWrapper;
import pl.skempa.util.*;

/**
 * Created by Mymon on 2017-10-08.
 */

public class ObjectsManagerImpl implements ObjectsManager {

    private List<Building> buildings = new LinkedList<Building>();

    pl.skempa.model.apiwrappers.ObjectsDataAPIWrapper openStreetMapApiWrapper ;


    private Vector3 position;


    @Override
    public void init() {
        openStreetMapApiWrapper = new OpenStreetMapAPIWrapper();
        position =new Vector3(18.0f,50.0f,0f);
        callApi();
    }

    @Override
    public void update(Vector3 position) {
        float deltaX = Math.abs(this.position.x -(position.x));
        float deltaY = Math.abs(this.position.y -(position.y));
        if (deltaX>=0.02f||deltaY>=0.02f){
            this.position = new Vector3(position);
            callApi();
        }
    }

    private void callApi() {
        try {
            buildings = openStreetMapApiWrapper.getObjects(position);
        } catch (ApiWrapperException e) {
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
