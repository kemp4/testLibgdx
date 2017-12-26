package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ShortArray;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import com.badlogic.gdx.graphics.VertexAttributes.Usage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.skempa.model.topography.HgtReader;

/**
 * Created by szymk on 11/25/2017.
 */
// todo refactor class name or move to another class
public class OsmBaseObject {
    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
    public static final int MAX_TRIS = 1;
    public static final int MAX_VERTS = MAX_TRIS * 3;
    public static final String BUILDING = "building";
    public static final String WAY = "highway";
    private static final float BUILDING_HEIGHT =0.03f ;
    private static final int MAX_NODES = 2001;
    private static final String TREE = "tree";
    private static final String NATURAL = "natural";
    private static final String STREETLAMP = "StreetLamp";//todo create or find some lamp model

    private ModelsReader modelsReader = new ModelsReader();
    private Map<String,Mesh> models = new HashMap<String,Mesh>();
    private List<WorldObject> objects = new LinkedList<WorldObject>();
    //TODO lot of refactor
    private float[] vertices;
    private int offset; //todo change to long.

    public Mesh twoDimMeshFromWays(OsmRawDataSet dataSet) {

        vertices = new float[10000000];
        offset = 0;
        Map<Long, Way> ways = dataSet.getWays();
        for (Map.Entry<Long, Way> wayEntry : ways.entrySet()) {
            Way way = wayEntry.getValue();
            // TODO maybe there is better way to check tags
            boolean isBuilding = false;
            boolean isWay = false;

            for (Tag tag : way.getTags()) {
                if (tag.getKey().equals(BUILDING)) {
                    isBuilding = true;
                }
                if (tag.getKey().equals(WAY)) {
                    isWay = true;
                }
            }
            if (isBuilding) {
                int i = 0;
                float[] vert = new float[MAX_NODES*2];
                List<WayNode> wayNodes = way.getWayNodes();
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodes().get(wayNode.getNodeId());
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                }

                EarClippingTriangulator triangulator = new EarClippingTriangulator();
                ShortArray indices = triangulator.computeTriangles(vert, 0, (i));
                for (int index = 0; index < indices.size; index++) {
                    vertices[offset++] = vert[indices.get(index) * 2];
                    vertices[offset++] = vert[indices.get(index) * 2 + 1];
                    addColor(new Color(0.3f, 0.1f, 0.5f, 0.7f));
                }

            }
            if (isWay) {
                int i = 0;
                float[] vert = new float[50000];
                List<WayNode> wayNodes = way.getWayNodes();

                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodeByWayNode(wayNode);
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                }
                for (int j = 0; j <= i - 4; j += 2) {
                    float pointAx = vert[j];
                    float pointAy = vert[1 + j];
                    float pointBx = vert[2 + j];
                    float pointBy = vert[3 + j];
                    Vector2 pointA = new Vector2(pointAx, pointAy);
                    Vector2 pointB = new Vector2(pointBx, pointBy);

                    Vector2 vectorR = new Vector2(pointB).sub(pointA);
                    vectorR = vectorR.nor();
                    vectorR = vectorR.scl(0.002f);                // r - way width
                    Color color = new Color(0.7f, 0.7f, 0.7f, 1f);
                    addVector2(new Vector2(pointA).add(vectorR.rotate(90)));
                    addColor(color);
                    addVector2(new Vector2(pointA).add(vectorR.rotate(180)));
                    addColor(color);
                    addVector2(new Vector2(pointB).add(vectorR));
                    addColor(color);
                    addVector2(new Vector2(pointB).add(vectorR));
                    addColor(color);
                    addVector2(new Vector2(pointB).add(vectorR.rotate(180)));
                    addColor(color);
                    addVector2(new Vector2(pointA).add(vectorR));
                    addColor(color);
                }
            }
        }

        float[] result = new float[offset];
        for (int j = 0; j < offset; j++) {
            result[j] = vertices[j];
        }
        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(result);
        return mesh;
    }


    public Scene threeDimMeshFromWays(OsmRawDataSet dataSet) {
        readModels();
        readHgt();
        vertices = new float[100000000];
        offset = 0;
        Map<Long, Way> ways = dataSet.getWays();
        for (Map.Entry<Long, Way> wayEntry : ways.entrySet()) {
            Way way = wayEntry.getValue();
            // TODO maybe there is better way to check tags
            boolean isBuilding = false;
            boolean isWay = false;
            boolean isTree = false;
            boolean isLamp = false;
            for (Tag tag : way.getTags()) {
                if (tag.getKey().equals(BUILDING)) {
                    isBuilding = true;
                }else if (tag.getKey().equals(WAY)) {
                    isWay = true;
                }else if (tag.getKey().equals(NATURAL)){
                    isTree = true;
                }
            }
            if (isBuilding) {
                int i = 0;
                float[] vert = new float[MAX_NODES*2];
                List<WayNode> wayNodes = way.getWayNodes();
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodes().get(wayNode.getNodeId());
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                    if (i>=4){
                        generateWall(vert,i);
                    }
                }
                EarClippingTriangulator triangulator = new EarClippingTriangulator();
                ShortArray indices = triangulator.computeTriangles(vert, 0, (i));
                for (int index = 0; index < indices.size; index++) {
                    vertices[offset++] = vert[indices.get(index) * 2];
                    vertices[offset++] = vert[indices.get(index) * 2 + 1];
                    vertices[offset++] = BUILDING_HEIGHT;
                    addColor(new Color(0.3f, 0.1f, 0.5f, 1.0f));
                    addVector3(new Vector3(0f,0f,1f));
                }
            }else if (isTree){
                WayNode wayNode = way.getWayNodes().get(0);
                Node node = dataSet.getNodes().get(wayNode.getNodeId());
                Vector2 positionInWorld = normalizePosition(node, dataSet.getBound());
                Vector3 translation = new Vector3(positionInWorld,0f);//
                Matrix4 matrix = new Matrix4();
                matrix.translate(translation);
                matrix.scale(0.01f,0.01f,0.01f);
                matrix.rotate(1,0,0,90);

                objects.add(new WorldObject(models.get(TREE),matrix));
                // todo generating trees only for tree tag values
            }else if(isLamp){
                //todo fix checking lamp tag
            }
        }

        float[] result = new float[offset];
        for (int j = 0; j < offset; j++) {
            result[j] = vertices[j];
        }
        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        mesh.setVertices(result);
        return new Scene(mesh,objects);
    }
    private void readHgt(){
        HgtReader hgtReader = new HgtReader();
        try {
            hgtReader.readHgt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readModels() {

        try {
            models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/tree.obj").read()));
           // models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/StreetLamp.obj").read()));
        } catch (IOException e) {
            //todo handle error
            e.printStackTrace();
        }
    }

    private void generateWall(float[] vert, int i) {
        float pointAx=vert[i-4];
        float pointAy=vert[i-3];
        float pointBx=vert[i-2];
        float pointBy=vert[i-1];

        //generating normal
        Vector2 pointA = new Vector2(pointAx, pointAy);
        Vector2 pointB = new Vector2(pointBx, pointBy);

        Vector3 normal = new Vector3((new Vector2(pointB).sub(pointA)).rotate90(1),0);

        normal = normal.nor();

        //p ointA
        addVector3(new Vector3(vert[i-4],vert[i-3],BUILDING_HEIGHT)); // add vertice position
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal
//B
        addVector3(new Vector3(vert[i-2],vert[i-1],BUILDING_HEIGHT));//pos
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal
//C
        addVector3(new Vector3(vert[i-4],vert[i-3],0));//pos
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal

        addVector3(new Vector3(vert[i-4],vert[i-3],0)); // add vertice position
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal
//D
        addVector3(new Vector3(vert[i-2],vert[i-1],0));//pos
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal

        addVector3(new Vector3(vert[i-2],vert[i-1],BUILDING_HEIGHT));//pos
        addColor(new Color(0.9f, 0.3f, 0.1f, 1.0f));
        addVector3(normal);//add normal
    }

    private static final int COLUMNS = 3601;
    private float[] terrainVertices;
    private int terrainOffset=0;

    private Mesh generateTerrainMesh(short[][] hgtTerrain,Bound bound){

        int beginX = (int)(COLUMNS*(Math.round(bound.getLeft())-bound.getLeft()));
        int endX = (int)(COLUMNS*(Math.round(bound.getRight())-bound.getRight()));
        int beginY = (int)(COLUMNS*(Math.round(bound.getBottom())-bound.getBottom()));
        int endY = (int)(COLUMNS*(Math.round(bound.getTop())-bound.getTop()));

        for (int i = beginX ; i <= endX ; i++){
            for (int j = beginY ; j <= endY ; j++){
                //generateTerrainSquare(new Bound(hgtTerrain[i][j]));
            }
        }
        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        //mesh.setVertices(result);
        return mesh;
    }



    private void addColor(Color color) {
        vertices[offset++] = color.r;    //Color(r, g, b, a)
        vertices[offset++] = color.g;
        vertices[offset++] = color.b;
        vertices[offset++] = color.a;
    }
    private void addVector3(Vector3 normal){
        vertices[offset++] = normal.x;
        vertices[offset++] = normal.y;
        vertices[offset++] = normal.z;
    }
    private void addVector2(Vector2 point) {
        vertices[offset++] = point.x;
        vertices[offset++] = point.y;
    }

    //// TODO: 11/27/2017 refactor name
    private static Vector2 normalizePosition(Node node, Bound bound) {
        double x = (node.getLongitude() - bound.getLeft()) * 100;
        double y = (node.getLatitude() - bound.getBottom()) * 100;
        return new Vector2((float) x, (float) y);
    }
}
