package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

import pl.skempa.model.Model;
import pl.skempa.model.object.rawdata.Scene;
import pl.skempa.model.object.rawdata.WorldObject;

import static com.badlogic.gdx.graphics.GL30.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL30.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL30.GL_DEPTH_TEST;

/**
 * Created by szymk on 12/8/2017.
 */

public class PerspRenderer implements ObjectsRenderer {

    Scene scene = null;
    private boolean firts = true;
    private ShaderProgram objectShader;
    private ShaderProgram generatedObjectsShader;

    @Override
    public void renderObjects(Model model) {
        if (firts) {
            create();
            firts = false;
            this.scene = model.getThreeDimScene();
        }
        flush(model.getCameraMatrix());
    }


    public void create() {
        objectShader = createObjectsShader();
        generatedObjectsShader = createGeneratedObjectsShader();
    }


    void flush(Matrix4 cameraMatrix) {

        Gdx.gl.glEnable(GL_DEPTH_TEST);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0.1f);

        objectShader.begin();
        objectShader.setUniformMatrix("combined", cameraMatrix);
        for (WorldObject object : scene.getObjects()) {
            objectShader.setUniformMatrix("model", object.getModelMatrix());
            object.getMesh().render(objectShader, GL30.GL_TRIANGLES, 0, object.getMesh().getMaxVertices());
        }
        objectShader.end();
        generatedObjectsShader.begin();
        generatedObjectsShader.setUniformMatrix("combined", cameraMatrix);
        scene.getGeneratedMesh().render(generatedObjectsShader, GL30.GL_TRIANGLES, 0, scene.getGeneratedMesh().getMaxVertices());
        generatedObjectsShader.end();
    }

    private ShaderProgram createGeneratedObjectsShader() {
        FileHandle handleVertex = Gdx.files.internal("shaders/SGEvertexshader.glsl");
        FileHandle handleFragment = Gdx.files.internal("shaders/SGEfragmentshader.glsl");
        return getShaderProgram(handleVertex, handleFragment);
    }

    private ShaderProgram createObjectsShader() {
        FileHandle handleVertex = Gdx.files.internal("shaders/modelSGEvertexshader.glsl");
        FileHandle handleFragment = Gdx.files.internal("shaders/SGEfragmentshader.glsl");
        return getShaderProgram(handleVertex, handleFragment);
    }

    private ShaderProgram getShaderProgram(FileHandle handleVertex, FileHandle handleFragment) {
        String vertexShader = handleVertex.readString();
        String fragmentShader = handleFragment.readString();
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        String log = shader.getLog();
        if (!shader.isCompiled())
            throw new GdxRuntimeException(log);
        if (log != null && log.length() != 0)
            System.out.println("Shader Log: " + log);
        return shader;
    }
}