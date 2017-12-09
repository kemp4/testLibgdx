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

    //The index position
    private boolean firts = true;
    @Override
    public void renderObjects(Model model) {
        if (firts){
            create();
            firts=false;
            prepareData(model.getMesh());
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
