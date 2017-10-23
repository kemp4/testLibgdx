package pl.skempa.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import pl.skempa.model.Model;
import pl.skempa.shader.ShaderWrapper;

/**
 * Created by Mymon on 2017-10-08.
 */

public class PerspRenderer implements ObjectsRenderer {
    private Camera camera;
    private ShaderWrapper shaderWrapper;



    @Override
    public void renderObjects(Model camera) {

        ShaderProgram shader = shaderWrapper.getShaderProgram();
        this.camera.update();
        //texture.bind();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shader.begin();
        shader.setUniformMatrix("u_projTrans", this.camera.combined);
        shader.setUniformi("u_texture", 0);
      //  mesh.render(shader, GL20.GL_TRIANGLES);
        shader.end();
    }


}
