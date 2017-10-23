package pl.skempa.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import pl.skempa.model.Model;
import pl.skempa.view.input.MyInputProcessor;
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
        initInput();
    }

    private void initInput() {
        MyInputProcessor inputProcessor = new MyInputProcessor(controller.getCameraController());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        GestureDetector gestureDetector = new GestureDetector(inputProcessor);
        inputMultiplexer.addProcessor(gestureDetector);
        inputMultiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render() {
        renderer.renderObjects(model);
    }
}
