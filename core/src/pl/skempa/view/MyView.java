package pl.skempa.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import pl.skempa.model.Model;
import pl.skempa.controller.input.MyInputProcessor;
import pl.skempa.view.render.ObjectsRenderer;
import pl.skempa.view.render.OrthoRenderer;
import pl.skempa.controller.app.Controller;

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
        renderer = new OrthoRenderer();
    }

    @Override
    public void render() {
        renderer.renderObjects(model);
    }
}
