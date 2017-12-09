package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.GdxRuntimeException;

import pl.skempa.model.Model;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;
import static com.badlogic.gdx.graphics.GL20.GL_FALSE;

/**
 * Created by szymk on 12/8/2017.
 */

public class PerspRenderer implements ObjectsRenderer {
    Mesh mesh;
    ShaderProgram shader;
    private boolean firts = true;

    @Override
    public void renderObjects(Model model) {
        if (firts) {
            create();
            firts = false;
            prepareData(model.getThreeDimMesh());
           // prepareData(model.getThreeDimMesh());
        }
//        Camera testCamera = new PerspectiveCamera(67,2,2);
//        testCamera.near = 0.3f;
//        testCamera.far = 60f;
//        testCamera.translate(0,5f,3f);
//        testCamera.lookAt(0,0,0);
//        testCamera.update();
        flush(model.getCameraMatrix());
    }

    private void prepareData(Mesh mesh) {
        this.mesh = mesh;
    }

    public void create() {
        shader = createMeshShader();
        // OsmBaseObject osmBaseObject = new OsmBaseObject();
        // mesh = osmBaseObject.threeDimMeshFromWays(null);

    }



    void flush(Matrix4 cameraMatrix) {
//        Mesh mesh;
//        float vertices[] = {
//                -0.5f, -0.5f, 0.0f,
//                0.5f, -0.5f, 0.0f,
//                0.0f, 0.5f, 0.0f
//        };
//        mesh = new Mesh(true, 9, 0,
//                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
//        mesh.setVertices(vertices);


        Gdx.gl.glEnable(GL_DEPTH_TEST);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 0.1f);


        shader.begin();
        shader.setUniformMatrix("combined", cameraMatrix);

//        shader.setUniformf("lightColor", 0.9f, 0.9f, 0.9f);


        mesh.render(shader, GL20.GL_TRIANGLES, 0, mesh.getMaxVertices());
        shader.end();
        //Gdx.gl.glDepthMask(true);
    }

    private ShaderProgram createMeshShader() {
        FileHandle handleVertex = Gdx.files.internal("shaders/SGEvertexshader.glsl");
        FileHandle handleFragment = Gdx.files.internal("shaders/SGEfragmentshader.glsl");
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
