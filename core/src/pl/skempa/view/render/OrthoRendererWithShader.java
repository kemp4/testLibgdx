package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.LinkedList;
import java.util.List;

import pl.skempa.model.Model;
import pl.skempa.model.object.Building;

/**
 * Created by Mymon on 2017-10-30.
 */

public class OrthoRendererWithShader implements ObjectsRenderer {

    Mesh mesh;
    ShaderProgram shader;

<<<<<<< HEAD
    //The index position
    private boolean firts = true;
=======
    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
    public static final int MAX_TRIS = 2;
    public static final int MAX_VERTS = MAX_TRIS * 3;

    private float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];
    //The index position
    private int idx = 0;
    boolean firts = true;
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    @Override
    public void renderObjects(Model model) {
        if (firts){
            create();
<<<<<<< HEAD
=======

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
            firts=false;
            prepareData(model.getMesh());
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
<<<<<<< HEAD
=======
        //this will push the triangles into the batch

>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
        flush(model.getCameraMatrix());
    }

    private void prepareData(Mesh mesh) {
        this.mesh=mesh;
    }

    public void create() {
        shader = createMeshShader();

    }

    void flush(Matrix4 cameraMatrix) {

        Gdx.gl.glDepthMask(false);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();
        shader.setUniformMatrix("u_projTrans", cameraMatrix);
        mesh.render(shader, GL20.GL_TRIANGLES, 0, mesh.getMaxVertices());
        shader.end();
        Gdx.gl.glDepthMask(true);
    }

    private ShaderProgram createMeshShader() {
        FileHandle handleVertex = Gdx.files.internal("shaders/vertexshader.glsl");
        FileHandle handleFragment = Gdx.files.internal("shaders/fragmentshader.glsl");
        String vertexShader= handleVertex.readString();
        String fragmentShader = handleFragment.readString();
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        String log = shader.getLog();
        if (!shader.isCompiled())
            throw new GdxRuntimeException(log);
        if (log!=null && log.length()!=0)
            System.out.println("Shader Log: "+log);
        return shader;
    }

}
