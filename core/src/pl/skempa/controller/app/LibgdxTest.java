package pl.skempa.controller.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;

//import pl.skempa.controller.CameraController;
//import pl.skempa.controller.CameraControllerImpl;
import java.io.InputStream;

import pl.skempa.controller.input.MyInputProcessor;
import pl.skempa.model.Model;
import pl.skempa.model.MyModel;
import pl.skempa.view.MyView;
import pl.skempa.view.View;

/**
 * class for testing libgdx features
 */
public class LibgdxTest extends ApplicationAdapter implements Controller{

	View view;
	Model model;

	//CameraController cameraController;

	@Override
	public void create () {
		model = new MyModel();
		model.init();
		//cameraController = new CameraControllerImpl(model);
		view = new MyView(this,model);
		view.init();
		initInput();
	}

	private void initInput() {
		MyInputProcessor inputProcessor = new MyInputProcessor(model);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureDetector gestureDetector = new GestureDetector(inputProcessor);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}


	@Override
	public void render () {
		model.update();
		view.render();
	}
	@Override
	public void resize (int width, int height) {

	}


	@Override
	public void dispose () {

	}

//	@Override
//	public CameraController getCameraController() {
//		return cameraController;
//	}
}