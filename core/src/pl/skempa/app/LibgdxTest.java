package pl.skempa.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import pl.skempa.controller.Controller;
import pl.skempa.controller.ControllerImpl;
import pl.skempa.input.MyInputProcessor;
import pl.skempa.object.ObjectsManager;
import pl.skempa.object.ObjectsManagerImpl;
import pl.skempa.render.ObjectsRenderer;
import pl.skempa.render.OrthoRenderer;

/**
 * class for testing libgdx features
 */
public class LibgdxTest extends ApplicationAdapter {

	ObjectsRenderer renderer;
	ObjectsManager objectsManager;
	Controller controller;

	@Override
	public void create () {

		objectsManager = new ObjectsManagerImpl();
		objectsManager.init();
		renderer = new OrthoRenderer(objectsManager);
		controller = new ControllerImpl(objectsManager,renderer);
		initInput();
	}

	private void initInput() {
		// TODO przemmyslec to jak modyfikowac stan z inp proc
		MyInputProcessor inputProcessor = new MyInputProcessor(controller);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureDetector gestureDetector = new GestureDetector(inputProcessor);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render () {
		renderer.renderObjects();
	}
	@Override
	public void resize (int width, int height) {

	}


	@Override
	public void dispose () {

	}
}