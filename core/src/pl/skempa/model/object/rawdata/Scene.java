package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Mesh;

import java.util.List;

/**
 * Created by szymk on 12/24/2017.
 */

public class Scene {

    private Mesh buildingMesh;
    private List<WorldObject> objects;
    private Mesh terrainMesh;

    public Scene(Mesh buildingsMesh, Mesh terrain, List<WorldObject> objects) {
        this.terrainMesh = terrain;
        this.objects = objects;
        this.buildingMesh = buildingsMesh;
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

}
