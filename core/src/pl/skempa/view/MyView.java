package pl.skempa.view;

import pl.skempa.model.Model;
import pl.skempa.view.render.ObjectsRenderer;
import pl.skempa.controller.app.Controller;
import pl.skempa.view.render.OrthoRendererWithShader;
import pl.skempa.view.render.PerspRenderer;
import pl.skempa.view.render.SimpleOrthoRenderer;

/**
 * Created by Mymon on 2017-10-22.
 */

public class MyView implements View {

    private Controller controller;
    private Model model;

    private ObjectsRenderer renderer;

    public MyView (Controller controller,Model model){
        this.controller = controller;
        this.model = model;
    }

    public void init() {
        renderer = new PerspRenderer();
        //renderer = new OrthoRendererWithShader();
        //renderer = new SimpleOrthoRenderer();
    }

    @Override
    public void render() {
        renderer.renderObjects(model);
    }
}
