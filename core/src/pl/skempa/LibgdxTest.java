package pl.skempa;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibgdxTest extends ApplicationAdapter {
	Camera camera;
	ShapeRenderer shapeRenderer;
	List<Building> buildings;

	@Override
	public void create () {
		camera = new OrthographicCamera(0.01f, 0.005f);
		camera.translate(18.988f, 50.210f, 0);
		shapeRenderer = new ShapeRenderer();
		buildings=new ArrayList<Building>();
		FileHandle xmlMap = Gdx.files.internal("mapFiles/mapOchojec.osm");
		    try {
				buildings = new XmlUtil().readXml(xmlMap.read());

				// TODO learn how to use JUnit with gradle
				System.out.println("done");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		 shapeRenderer.setProjectionMatrix(camera.combined);
		 shapeRenderer.begin(ShapeType.Line);
		 shapeRenderer.setColor(1, 0, 0, 1);
//		 shapeRenderer.line(18.988f,50.21f,18.989f,50.211f);
		 drawBuildings();
		 shapeRenderer.end();
	}
	
	private void drawBuildings() {

//		for (int i=0;i<buildings.size();i++){
//			drawBuilding(buildings.get(i));
//
//		}
		for (Building building : buildings) {
			drawBuilding(building);
		}
//		buildings.forEach(this::drawBuilding);
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
}
