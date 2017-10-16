package pl.skempa.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

import pl.skempa.controller.CameraController;
import pl.skempa.controller.CameraControllerImpl;
import pl.skempa.input.MyInputProcessor;
import pl.skempa.object.MapCamera;
import pl.skempa.object.MyMapCamera;
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
	CameraController cameraController;

	@Override
	public void create () {

		objectsManager = new ObjectsManagerImpl();
		objectsManager.init();
		renderer = new OrthoRenderer(objectsManager);
		MapCamera mapCamera = new MyMapCamera();
		cameraController = new CameraControllerImpl(objectsManager,mapCamera);
		initInput();
	}

	private void initInput() {
		// TODO przemmyslec to jak modyfikowac stan z inp proc
		MyInputProcessor inputProcessor = new MyInputProcessor(cameraController);
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