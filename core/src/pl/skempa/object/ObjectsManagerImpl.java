package pl.skempa.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pl.skempa.Building;
import pl.skempa.XmlUtilImpl;
import pl.skempa.model.DegreePosition;

/**
 * Created by Mymon on 2017-10-08.
 */

public class ObjectsManagerImpl implements ObjectsManager {


    private Mesh mesh;
    private Texture texture ;
    private List<Building> buildings = new LinkedList<Building>();

    @Override
    public void init() {

        //readBuildings();
        callApi();
    }

    private void callApi() {
        ObjectsDataAPIWrapper openStreetMapApiWrapper = new OpenStreetMapAPIWrapper();
        try {
            buildings = openStreetMapApiWrapper.getObjects(new DegreePosition(18.0f,50.0f));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error with calling open Street Map API");
        }
    }
//        private void test3dOpengl() {
//            mesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
//            mesh.setVertices(new float[]
//                    {-0.5f, -0.5f, 0, 1, 1, 1, 1, 0, 1,
//                            0.5f, -0.5f, 0, 1, 1, 1, 1, 1, 1,
//                            0.5f, 0.5f, 0, 1, 1, 1, 1, 1, 0,
//                            -0.5f, 0.5f, 0, 1, 1, 1, 1, 0, 0});
//
//            mesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
//            texture = new Texture(Gdx.files.internal("textures/tekstur.png"));
//        }

    private void readBuildings() {

        buildings=new ArrayList<Building>();
        FileHandle xmlMap = Gdx.files.internal("mapFiles/mapOchojec.osm");
        try {
            buildings = new XmlUtilImpl().readXml(xmlMap.read());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public List<Building> getObjects() {
        return buildings;
    }



}
