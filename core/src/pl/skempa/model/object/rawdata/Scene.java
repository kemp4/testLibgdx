package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.List;

/**
 * Created by szymk on 12/24/2017.
 */

public class Scene {

    private Mesh buildingMesh;
    private List<WorldObject> objects;
    private List<WorldObject> powerTowers;
    private Mesh terrainMesh;
    private Mesh streetsMesh;
    private Mesh bezierMesh;



    public Scene(Mesh buildingsMesh, Mesh terrain,Mesh streets,Mesh beziers,List<WorldObject> powerTowers, List<WorldObject> objects) {
        this.terrainMesh = terrain;
        this.objects = objects;
        this.buildingMesh = buildingsMesh;
        this.powerTowers = powerTowers;
        this.streetsMesh = streets;
        this.bezierMesh = beziers;
    }
    public List<WorldObject> getObjects() {

        return objects;
    }

    public Mesh getTerrainMesh() {
        return terrainMesh;
    }

    public Mesh getBuildingMesh() {
        return buildingMesh;
    }

    public void setBuildingMesh(Mesh buildingMesh) {
        this.buildingMesh = buildingMesh;
    }

    public Mesh getStreetsMesh() {
        return streetsMesh;
    }

    public Mesh getBezierMesh() {
        return bezierMesh;
    }

    public List<WorldObject> getPowerTowers() {
        return powerTowers;
    }
}
