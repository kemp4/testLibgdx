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
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.LinkedList;
import java.util.List;

import pl.skempa.model.Model;
import pl.skempa.model.object.Building;

/**
 * Created by Mymon on 2017-10-30.
 */

public class OrthoRendererByOpenGL implements ObjectsRenderer {

;
//    List<Mesh> meshes;
//

//    private void generateMeshes(List<Building> objects) {
//        shader = createMeshShader();
//        meshes= new LinkedList<Mesh>();
//        for (Building object : objects){
//            Mesh mesh = new Mesh(true, MAX_VERTS, 0,
//                    new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
//                    new VertexAttribute(Usage.ColorPacked, COLOR_COMPONENTS, "a_color"));
//        }
//    }

    Mesh mesh;
    OrthographicCamera cam;
    ShaderProgram shader;

    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
    public static final int MAX_TRIS = 1;
    public static final int MAX_VERTS = MAX_TRIS * 3;

    private float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];
    //The index position
    private int idx = 0;
    boolean firts = true;
    @Override
    public void renderObjects(Model model) {
        if (firts){
            create();
            firts=false;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //this will push the triangles into the batch
        drawTriangle(10, 10, 40, 40, new Color(0.5f,0f,0f,0.5f));
        drawTriangle(12, 13, 70, 40, new Color(0f,0f,0.5f,0.5f));
        flush();
    }

    public void create() {
        mesh = new Mesh(true, MAX_VERTS, 0,
                new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        shader = createMeshShader();
        cam = new OrthographicCamera();
    }

    void flush() {
        mesh.setVertices(verts);
        Gdx.gl.glDepthMask(false);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        int vertexCount = (idx/NUM_COMPONENTS);
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.begin();
        shader.setUniformMatrix("u_projTrans", cam.combined);
        mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);
        shader.end();
        Gdx.gl.glDepthMask(true);
        idx = 0;
    }

    void drawTriangle(float x, float y, float width, float height, Color color) {
        if (idx==verts.length)
            flush();

        verts[idx++] = x; 			//Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color.r; 	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        //  vertex
        verts[idx++] = x; 			//Position(x, y)
        verts[idx++] = y + height;
        verts[idx++] = color.r; 	//Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
        //bottom right vertex
        verts[idx++] = x + width;	 //Position(x, y)
        verts[idx++] = y;
        verts[idx++] = color.r;		 //Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;
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
