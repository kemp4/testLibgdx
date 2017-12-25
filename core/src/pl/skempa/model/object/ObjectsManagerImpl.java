package pl.skempa.model.object;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import pl.skempa.model.apiwrappers.ApiWrapperException;
import pl.skempa.model.apiwrappers.OpenStreetMapAPIWrapper;
import pl.skempa.model.object.rawdata.BaseObject;
import pl.skempa.model.object.rawdata.Way;

/**
 * Created by Mymon on 2017-10-08.
 */

public class ObjectsManagerImpl implements ObjectsManager {

    private Map<Long, Way> ways ;
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    pl.skempa.model.apiwrappers.ObjectsDataAPIWrapper openStreetMapApiWrapper ;
    Mesh mesh;

    private Vector3 position;


    @Override
    public void init() {
        openStreetMapApiWrapper = new OpenStreetMapAPIWrapper();
        position =new Vector3(0.0f,0.0f,0f);
    }

    @Override
    public void update(Vector3 position) {
<<<<<<< HEAD
<<<<<<< HEAD
//        float deltaX = Math.abs(this.position.x -(position.x));
 //       float deltaY = Math.abs(this.position.y -(position.y));
        //TODO uncomment this section
//        if (deltaX>=0.02f||deltaY>=0.02f){
//            this.position = new Vector3(position);
//            callApi();
//        }
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
        float deltaX = Math.abs(this.position.x -(position.x));
        float deltaY = Math.abs(this.position.y -(position.y));
        if (deltaX>=0.02f||deltaY>=0.02f){
            this.position = new Vector3(position);
            callApi();
        }
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
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
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    @Override
    public Mesh getMesh() {
        createMesh();
        return mesh;
    }

    private void createMesh() {
        mesh= BaseObject.fromWays(ways);
    }


}
