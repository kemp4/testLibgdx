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
import java.util.ArrayList;
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
    public static final String BUILDING = "building";
    public static final String WAY = "highway";
    private static final float BUILDING_HEIGHT =0.03f ;
    private static final int MAX_NODES = 2001;
    private static final String TREE = "tree";
    private static final String NATURAL = "natural";
    private static final String STREETLAMP = "StreetLamp";//todo create or find some lamp model
    private static final Color ROOF_COLOR = new Color(0.3f, 0.1f, 0.5f, 1.0f);
    private static final Vector3 ROOF_NORMAL = new Vector3(0,0,1);


    public Scene threeDimMeshFromWays(OsmRawDataSet dataSet) throws IOException {
        Map<String,Mesh> models = readModels();
        List<WorldObject> objects = new LinkedList<WorldObject>();
        Vector3[][] htgPositions = generateTerrainVerticesPositions(readHgt(),dataSet.getBound());
        Mesh terrain = generateTerrainMesh(htgPositions);
        int testIt =0 ;
        List<Float> vertices = new ArrayList<>(3388608);
        Map<Long, Way> ways = dataSet.getWays();
        for (Map.Entry<Long, Way> wayEntry : ways.entrySet()) {
            testIt ++;
            if (testIt%10000==0){System.out.println(testIt);}//TODO remove
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
                float minHeight= 100f;
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodes().get(wayNode.getNodeId());
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                    float pointHeight = calculateHeight(new Vector2(positionInMesh.x, positionInMesh.y), htgPositions);
                    minHeight = ( ( pointHeight < minHeight ) ? pointHeight : minHeight );
                }
                for (int j=4;j<=i;j+=2){
                    generateWall(vert,j,vertices,minHeight);
                }
                EarClippingTriangulator triangulator = new EarClippingTriangulator();
                ShortArray indices = triangulator.computeTriangles(vert, 0, (i));

                for (int index = 0; index < indices.size; index++) {
                    addVertex(new Vector3(vert[indices.get(index) * 2],vert[indices.get(index) * 2 + 1],minHeight+BUILDING_HEIGHT), ROOF_COLOR,ROOF_NORMAL,vertices);
                }
            }else if (isTree){
                WayNode wayNode = way.getWayNodes().get(0);
                Node node = dataSet.getNodes().get(wayNode.getNodeId());
                Vector2 positionInWorld = normalizePosition(node, dataSet.getBound());
                Vector3 translation = new Vector3(positionInWorld,calculateHeight(positionInWorld,htgPositions));
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

        float[] result = new float[vertices.size()];
        for (int j = 0; j < vertices.size(); j++) {
            result[j] = vertices.get(j);
        }
        Mesh buildingsMesh;
        buildingsMesh = new Mesh(true, vertices.size(), 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        buildingsMesh.setVertices(result);

        return new Scene(buildingsMesh,terrain,objects);
    }

    private float calculateHeight(Vector2 position, Vector3[][] htgPositions) {
        float indexXf = (position.x*htgPositions.length/10.f);
        float indexYf = (position.y*htgPositions.length/10.f);
        float localX = indexXf - (int)indexXf;
        float localY = indexYf - (int)indexYf;

        int indexX = (int)indexXf;
        int indexY = (int)indexYf;

        float heightA;
        float heightB = htgPositions[indexX+1][indexY].z;
        float heightC = htgPositions[indexX][indexY+1].z;

        if (localX>1-localY){ //dolny trojkat terenu
            heightA = htgPositions[indexX][indexY].z;
        }else{              // gorny trojkat terenu
            heightA = htgPositions[indexX+1][indexY+1].z;
            localX=1-localX;
            localY=1-localY;
        }
        return heightA+(heightC-heightA)*localY+(heightB-heightA)*localX;
    }

    private short[][] readHgt() throws IOException {
        HgtReader hgtReader = new HgtReader();
        return hgtReader.readHgt();
    }
    private Map<String, Mesh> readModels() {
        Map<String, Mesh>  models = new HashMap<>();
        ModelsReader modelsReader = new ModelsReader();
        try {
            models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/tree.obj").read()));
           // models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/StreetLamp.obj").read()));
        } catch (IOException e) {
            //todo handle error
            e.printStackTrace();
        }
        return models;
    }

    private void generateWall(float[] vert, int i,List<Float> vertices,float height) {
        float pointAx=vert[i-4];
        float pointAy=vert[i-3];
        float pointBx=vert[i-2];
        float pointBy=vert[i-1];

        //generating normal
        Vector2 pointA = new Vector2(pointAx, pointAy);
        Vector2 pointB = new Vector2(pointBx, pointBy);
        Vector3 normal = new Vector3((new Vector2(pointB).sub(pointA)).rotate90(1),0);
        Color wallColor = new Color(0.9f,0.3f,0.1f,1.0f);

        normal = normal.nor();

        addVertex(new Vector3(pointA,height+BUILDING_HEIGHT),wallColor,normal,vertices);
        addVertex(new Vector3(pointB,height+BUILDING_HEIGHT),wallColor,normal,vertices);
        addVertex(new Vector3(pointA,height),wallColor,normal,vertices);
        addVertex(new Vector3(pointA,height),wallColor,normal,vertices);
        addVertex(new Vector3(pointB,height),wallColor,normal,vertices);
        addVertex(new Vector3(pointB,height+BUILDING_HEIGHT),wallColor,normal,vertices);
    }

    private void addVertex(Vector3 position,Color color,Vector3 normal,List<Float>vertices){
        addVector3(position,vertices); // add vertice position
        addColor(color,vertices);
        addVector3(normal,vertices);//add normal
    }

    private static final int COLUMNS = 3601;

    private Mesh generateTerrainMesh( Vector3[][] positionsArray){

        List<Float> vertices = new ArrayList<>(1000000);
        Color terrainColor = new Color(0.1f,0.8f,0.0f,1f);
        Color testColor = new Color(0.1f,0.1f,0.4f,0.5f);
        for (int i = 0 ; i < positionsArray.length-1 ; i++){
            for (int j = 0 ; j < positionsArray[i].length-1 ; j++){
                if (i<3&&j<3) {//TODO
                    Vector3 normal = calculateNormal(positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j + 1]);
                    addVertex(positionsArray[i][j], terrainColor, normal, vertices);
                    addVertex(positionsArray[i][j + 1], terrainColor, normal, vertices);
                    addVertex(positionsArray[i + 1][j], terrainColor, normal, vertices);
                    normal = calculateNormal(positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j]);
                    normal.scl(-1f);
                    addVertex(positionsArray[i + 1][j + 1], terrainColor, normal, vertices);
                    addVertex(positionsArray[i][j + 1], terrainColor, normal, vertices);
                    addVertex(positionsArray[i + 1][j], terrainColor, normal, vertices);

                }else {
                    Vector3 normal = calculateNormal(positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j + 1]);
                    addVertex(positionsArray[i][j], testColor, normal, vertices);
                    addVertex(positionsArray[i][j + 1], testColor, normal, vertices);
                    addVertex(positionsArray[i + 1][j], testColor, normal, vertices);
                    normal = calculateNormal(positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j]);
                    normal.scl(-1f);
                    addVertex(positionsArray[i + 1][j + 1], testColor, normal, vertices);
                    addVertex(positionsArray[i][j + 1], testColor, normal, vertices);
                    addVertex(positionsArray[i + 1][j], testColor, normal, vertices);

                }
            }
        }
        float[] result = new float[vertices.size()];
        for (int j = 0; j < vertices.size(); j++) {
            result[j] = vertices.get(j);
        }
        Mesh mesh;
        mesh = new Mesh(true, vertices.size(), 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        mesh.setVertices(result);
        return mesh;
    }

    private Vector3 calculateNormal(Vector3 p1, Vector3 p2, Vector3 p3) {
        Vector3 u= new Vector3(p2).sub(p1);
        Vector3 v= new Vector3(p3).sub(p1);
        Vector3 normal = new Vector3();
        normal.x = u.y*v.z - u.z*v.y;
        normal.y = u.z*v.x - u.x*v.z;
        normal.z = u.x*v.y - u.y*v.x;
        return normal;
    }

    private Vector3[][] generateTerrainVerticesPositions(short[][] hgtArray, Bound bound) {
        float ll= Math.round(bound.getLeft());
        //TODO if - than end=begin
        int endX = getEndX(bound);
        int beginX = getBeginX(bound);
        int beginY = getBeginY(bound);
        int endY = getEndY(bound);

        Vector3 result[][] = new Vector3[endX-beginX+1][];

        for(int i = 0 ; i <endX-beginX+1;i++ ){
            result[i]= new Vector3[endY-beginY+1];
        }

        for (int i = 0 ; i <= endX-beginX ; i++){
            for (int j = 0 ; j <= endY-beginY ; j++){
                float posX = generateTerrainVertexCoord(beginX+i,beginX,endX);
                float posY = generateTerrainVertexCoord(beginY+j,beginY,endY);
                float posZ = generateTerrainVertexHeight(hgtArray[beginX+i][beginY+j]);
                result[i][j] = new Vector3(posX,posY,posZ);
            }
        }
        return result;
    }

    private int getEndY(Bound bound) {
        return (int)Math.abs(COLUMNS*(bound.getTop()-Math.round(bound.getTop())));
    }

    private int getBeginY(Bound bound) {
        return getBeginY(COLUMNS * (bound.getBottom() - Math.round(bound.getBottom())));
    }

    private int getBeginY(double v) {
        return (int) Math.abs(v);
    }

    private int getBeginX(Bound bound) {
        return (int)Math.abs(COLUMNS*(bound.getRight()-Math.round(bound.getRight())));
    }

    private int getEndX(Bound bound) {
        return (int) Math.abs((COLUMNS * (bound.getLeft() - Math.round(bound.getLeft()))));
    }

    private float generateTerrainVertexHeight(short h) {
    return (float)(((h)/60.f)-15f);
    }
    private float generateTerrainVertexCoord(int x,int begin,int end) {
        return ((float)(x-begin))/(float)(end-begin)*10.f;
    }
    private void addColor(Color color,List<Float> vertices) {
        vertices.add(color.r);    //Color(r, g, b, a)
        vertices.add(color.g);
        vertices.add(color.b);
        vertices.add(color.a);
    }
    private void addVector3(Vector3 normal,List<Float> vertices){
        vertices.add(normal.x);
        vertices.add(normal.y);
        vertices.add(normal.z);
    }
    private void addVector2(Vector2 point,List<Float> vertices) {
        vertices.add(point.x);
        vertices.add(point.y);
    }

    //// TODO: 11/27/2017 refactor name
    private static Vector2 normalizePosition(Node node, Bound bound) {
        double x = (node.getLongitude() - bound.getLeft()) * 100;
        double y = (node.getLatitude() - bound.getBottom()) * 100;
        return new Vector2((float) x, (float) y);
    }

    public Mesh twoDimMeshFromWays(OsmRawDataSet dataSet) {
        List<Float> vertices = new ArrayList<>(9000000);
        int offset = 0;
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
                    addVector2(new Vector2(vert[indices.get(index) * 2],vert[indices.get(index) * 2 + 1]),vertices);
                    addColor(new Color(0.3f, 0.1f, 0.5f, 0.7f),vertices);
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
                    addVector2(new Vector2(pointA).add(vectorR.rotate(90)),vertices);
                    addColor(color,vertices);
                    addVector2(new Vector2(pointA).add(vectorR.rotate(180)),vertices);
                    addColor(color,vertices);
                    addVector2(new Vector2(pointB).add(vectorR),vertices);
                    addColor(color,vertices);
                    addVector2(new Vector2(pointB).add(vectorR),vertices);
                    addColor(color,vertices);
                    addVector2(new Vector2(pointB).add(vectorR.rotate(180)),vertices);
                    addColor(color,vertices);
                    addVector2(new Vector2(pointA).add(vectorR),vertices);
                    addColor(color,vertices);
                }
            }
        }
        float[] result = new float[vertices.size()];
        for (int j = 0; j < vertices.size(); j++) {
            result[j] = vertices.get(j);
        }
        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(result);
        return mesh;
    }
}
