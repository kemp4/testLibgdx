package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Mesh;

import java.util.List;
import java.util.Map;

/**
 * Created by szymk on 12/24/2017.
 */

public class Scene {
    private List<WorldObject> objects;
    private Mesh generatedMesh;

    public Scene(Mesh mesh, List<WorldObject> objects) {
        this.generatedMesh = mesh;
        this.objects = objects;
    }

    public List<WorldObject> getObjects() {
        return objects;
    }

    public Mesh getGeneratedMesh() {
        return generatedMesh;
    }

}
