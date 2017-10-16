package pl.skempa.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.skempa.Building;
import pl.skempa.object.ObjectsManager;
import pl.skempa.object.ObjectsManagerImpl;
import pl.skempa.render.ObjectsRenderer;
import pl.skempa.render.OrthoRenderer;
import pl.skempa.shader.ShaderWrapper;
import pl.skempa.XmlUtilImpl;

/**
 * class for testing libgdx features
 */
public class LibgdxTest extends ApplicationAdapter {

	private static final float resizeCameraSpeed = 0.03f;
	private static final float moveCameraSpeed = 0.002f;
	private ObjectsManager objectsManager;
	private ObjectsRenderer objectsRenderer;
	private Camera camera;
	ShaderWrapper shader;



	@Override
	public void create () {
		initCamera();

		initInput();
		//initShaders();
		initObjectmanager();
		initRenderer();
		//TODO temporary test

	}


	private void initCamera() {
		//initPerspCamera();
		initOrthoCamera();
	}

	private void initInput() {
		// TODO przemmyslec to jak modyfikowac stan z inp proc
		pl.skempa.input.MyInputProcessor inputProcessor = new pl.skempa.input.MyInputProcessor(this);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureDetector gestureDetector = new GestureDetector(inputProcessor);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	private void initShaders() {
		shader = new ShaderWrapper();
	}

	private void initObjectmanager() {
		objectsManager = new ObjectsManagerImpl();
	}

	private void initRenderer() {
		objectsRenderer = new OrthoRenderer(objectsManager);
	}


// TODO to trzeba raczej gdzies wydzielic
	private void initPerspCamera() {
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0,0,0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
	}

	private void initOrthoCamera() {
		camera = new OrthographicCamera(0.01f, 0.005f);

	}

	public void moveCamera(float screenX, float screenY) {
		camera.translate(screenX*camera.viewportWidth*moveCameraSpeed,screenY*camera.viewportHeight*moveCameraSpeed,0.0f);
	}
// //
	@Override
	public void render () {
		objectsManager.update(camera);
		objectsRenderer.renderObjects(camera);
	}

	@Override
	public void resize (int width, int height) {
		float aspectRatio = (float) width / (float) height;
		camera = new OrthographicCamera(2f * aspectRatio, 2f);
	}

	@Override
	public void dispose () {

	}

	public void resizeCamera(int amount) {
		camera.viewportHeight*= 1+amount* resizeCameraSpeed;
		camera.viewportWidth*= 1+amount* resizeCameraSpeed;
	}
}
