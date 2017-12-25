package pl.skempa.model;

<<<<<<< HEAD
<<<<<<< HEAD
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import pl.skempa.model.camera.MapCamera;
import pl.skempa.model.camera.MyMapCamera;
<<<<<<< HEAD
<<<<<<< HEAD
import pl.skempa.model.camera.MyPerspCamera;
import pl.skempa.model.object.ObjectsManager;
import pl.skempa.model.object.ObjectsManagerImpl;
import pl.skempa.model.object.rawdata.OsmBaseObject;
import pl.skempa.model.object.rawdata.OsmRawDataSet;
import pl.skempa.model.object.rawdata.Scene;
import pl.skempa.model.object.rawdata.Way;
import pl.skempa.util.PbfReader;
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import pl.skempa.model.object.ObjectsManager;
import pl.skempa.model.object.ObjectsManagerImpl;
import pl.skempa.model.object.rawdata.BaseObject;
import pl.skempa.model.object.rawdata.Way;
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c

/**
 * Created by Mymon on 2017-10-22.
 */

public class MyModel implements Model {
    //TODO refactor name later
    private MapCamera camera;

    private ObjectsManager objectsManager;



    @Override
    public void init() {
        objectsManager = new ObjectsManagerImpl();
        objectsManager.init();
<<<<<<< HEAD
<<<<<<< HEAD
        //camera = new MyMapCamera();
        camera = new MyPerspCamera();

        camera.setPosition(new Vector3(0f, 0f, 4f));


=======
        camera = new MyMapCamera();
        //camera.setPosition(new Vector3(0f,0f,0f));
        camera.setPosition(new Vector3(139.9f,35.66f,0f));
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
        camera = new MyMapCamera();
        //camera.setPosition(new Vector3(0f,0f,0f));
        camera.setPosition(new Vector3(139.9f,35.66f,0f));
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    }
        //wsg84

    @Override
    public void update() {
        objectsManager.update(camera.getPosition());
    }


    @Override
    public Matrix4 getCameraMatrix() {
        return camera.getMatrix();
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    }

    @Override
    public Map<Long, Way> getObjects() {
        return objectsManager.getObjects();
    }

    @Override
    public void moveCamera(int deltaX, int deltaY, int i) {
        camera.moveCamera(deltaX,deltaY,0);
    }

    @Override
    public void zoomCamera(int amount) {
        camera.zoomCamera(amount);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    private OsmRawDataSet dataSet =null;


    @Override
    public Mesh getMesh() {

        // Code for tests //// TODO: 11/25/2017 move/refactor
        PbfReader pbfReader = new PbfReader();


        try {
            dataSet = pbfReader.parsePbf(Gdx.files.internal("mapFiles/kato.pbf").read());
            //dataSet.getWays().keySet().toArray().toString();
            return new OsmBaseObject().twoDimMeshFromWays(dataSet);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return objectsManager.getMesh();//if above method failed
    }

    @Override
    public OsmRawDataSet getOsmRawDataSet() {
        return dataSet;
    }

    @Override
    public Scene getThreeDimScene() {
        PbfReader pbfReader = new PbfReader();
        dataSet = pbfReader.parsePbf(Gdx.files.internal("mapFiles/kato.pbf").read());
        return new OsmBaseObject().threeDimMeshFromWays(dataSet);
    }

    @Override
    public Camera getCamera() {
        return camera.getLibgdxCamera();

=======
    @Override
    public Mesh getMesh() {
        return objectsManager.getMesh();
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
    @Override
    public Mesh getMesh() {
        return objectsManager.getMesh();
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    }

}
