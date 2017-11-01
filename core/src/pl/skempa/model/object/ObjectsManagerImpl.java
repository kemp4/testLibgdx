package pl.skempa.model.object;

import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import pl.skempa.model.apiwrappers.ApiWrapperException;
import pl.skempa.model.apiwrappers.OpenStreetMapAPIWrapper;
import pl.skempa.model.object.rawdata.Way;

/**
 * Created by Mymon on 2017-10-08.
 */

public class ObjectsManagerImpl implements ObjectsManager {

    private Map<Long, Way> ways ;

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
            ways = openStreetMapApiWrapper.getObjects(position).getWays();
        } catch (ApiWrapperException e) {
            e.printStackTrace();
            throw new RuntimeException("error with calling open Street Map API");
        }
    }

    @Override
    public Map<Long, Way> getObjects() {
        return ways;
    }



}
