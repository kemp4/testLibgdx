package pl.skempa.model;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

import pl.skempa.model.object.Building;

/**
 * Created by Mymon on 2017-10-22.
 */

public class TestModel implements Model {
    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public Matrix4 getCameraMatrix() {
        return new OrthographicCamera(0.1f,0.1f).combined;
    }

    @Override
    public List<Building> getObjects() {
        ArrayList<Building> result = new ArrayList<Building>();
        Building building = new Building();
        List<Vector3> testPoints= new ArrayList<Vector3>();
        testPoints.add(new Vector3(0.f,0.f,0.f));
        testPoints.add(new Vector3(0.f,0.05f,0.f));
        testPoints.add(new Vector3(0.05f,0.00f,0.f));
        building.setWallPoints(testPoints);
        result.add(building);
        return result;
    }

    @Override
    public void moveCamera(int deltaX, int deltaY, int i) {

    }

    @Override
    public void zoomCamera(int amount) {

    }
}
