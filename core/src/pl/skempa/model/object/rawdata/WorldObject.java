package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by szymk on 12/24/2017.
 */

public class WorldObject {
    private Mesh mesh;
    private Matrix4 modelMatrix;

    public WorldObject(Mesh mesh, Matrix4 matrix) {
        this.mesh=mesh;
        this.modelMatrix=matrix;
    }

    public Matrix4 getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(Matrix4 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
