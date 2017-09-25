package pl.skempa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class LibgdxTest extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Camera camera;
	ShapeRenderer shapeRenderer;
	List<Building> buildings;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(0.01f, 0.02f);
		camera.translate(38.99f, 125.75f, 0);
		shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
		 File initialFile = new File("mapFiles/mapPhenian.osm");
		    try {
				InputStream targetStream = new FileInputStream(initialFile);
				buildings = new XmlUtil().readXml(targetStream); 
				
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
		 shapeRenderer.setColor(1, 1, 0, 1);
		 //shapeRenderer.line(18.988f,50.21f,18.989f,50.211f);
		 drawBuildings();
		 shapeRenderer.end();
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
		batch.dispose();
		img.dispose();
	}
}
