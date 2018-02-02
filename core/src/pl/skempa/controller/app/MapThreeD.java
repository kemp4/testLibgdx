package pl.skempa.controller.app;


//import pl.skempa.controller.CameraController;
//import pl.skempa.controller.CameraControllerImpl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;

import pl.skempa.controller.input.MyInputProcessor;
import pl.skempa.model.Model;
import pl.skempa.model.MyModel;
import pl.skempa.view.MyView;
import pl.skempa.view.View;

/**
 * class for testing libgdx features
 */
public class Main extends ApplicationAdapter implements Controller{

	private static final float VELOCITY = 0.1f;
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
		checkKeys();
		model.update();
		view.render();

	}

	private void checkKeys() {
		Vector3 trans = new Vector3(0,0,0);
		if (Gdx.input.isKeyPressed (Input.Keys.LEFT)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(VELOCITY).rotate(Vector3.Z,90);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.RIGHT)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(-VELOCITY).rotate(Vector3.Z,90);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.UP)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(VELOCITY);
		}
		if (Gdx.input.isKeyPressed (Input.Keys.DOWN)){
			Vector3 front = new Vector3(model.getCamera().direction);
			trans = front.scl(-VELOCITY);
		}
		model.getCamera().translate(trans);
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