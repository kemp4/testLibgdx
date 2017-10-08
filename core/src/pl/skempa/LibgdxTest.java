package pl.skempa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * class for testing libgdx features
 */
public class LibgdxTest extends ApplicationAdapter {
	private static final float resizeCameraSpeed = 0.03f;
	private static final float moveCameraSpeed = 0.002f;
	Camera camera;
	ShapeRenderer shapeRenderer;
	List<Building> buildings;

	@Override
	public void create () {
		camera = new OrthographicCamera(0.01f, 0.005f);

//		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		camera.position.set(10f, 10f, 10f);
//		camera.lookAt(0,0,0);
//		camera.near = 1f;
//		camera.far = 300f;
//		camera.update();

		initInput();


		shapeRenderer = new ShapeRenderer();
		buildings=new ArrayList<Building>();
		FileHandle xmlMap = Gdx.files.internal("mapFiles/mapOchojec.osm");
		    try {
				buildings = new XmlUtilImpl().readXml(xmlMap.read());
				Vector2 cameraPosition = new XmlUtilImpl().getCameraPos(xmlMap.read());
				camera.translate(cameraPosition.x, cameraPosition.y, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private void initInput() {
		// TODO przemmyslec to jak modyfikowac stan z inp proc
		MyInputProcessor inputProcessor = new MyInputProcessor(this);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		GestureDetector gestureDetector = new GestureDetector(inputProcessor);
		inputMultiplexer.addProcessor(gestureDetector);
		inputMultiplexer.addProcessor(inputProcessor);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		 shapeRenderer.setProjectionMatrix(camera.combined);
		 shapeRenderer.begin(ShapeType.Line);
		 shapeRenderer.setColor(1, 1, 0, 1);
		 drawBuildings();
		 shapeRenderer.end();
	}
	@Override
	public void resize (int width, int height) {

	}
	private void drawBuildings() {

		for (Building building : buildings) {
			drawBuilding(building);
		}
	}

	private void drawBuilding(Building building) {
		List<Vector3> points = building.getWallPoints();
		Iterator<Vector3> pointsIterator = points.iterator();
		Vector3 previousPoint = pointsIterator.next();
	      while(pointsIterator.hasNext()) {
	    	  Vector3 actualPoint = pointsIterator.next();
	    	  shapeRenderer.line(previousPoint,actualPoint);
	    	  previousPoint=actualPoint;
	      }
	}

	@Override
	public void dispose () {

	}

	public void resizeCamera(int amount) {
		camera.viewportHeight*= 1+amount* resizeCameraSpeed;
		camera.viewportWidth*= 1+amount* resizeCameraSpeed;
	}


	public void moveCamera(float screenX, float screenY) {
		camera.translate(screenX*camera.viewportWidth*moveCameraSpeed,screenY*camera.viewportHeight*moveCameraSpeed,0.0f);

	}
}
