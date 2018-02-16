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
import java.util.Random;

import pl.skempa.controller.app.Settings;
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
    private static final String STREETLAMP = "street_lamp";
    private static final String ELECTRITY_TOWER = "power";
    private static final Color ROOF_COLOR = new Color(0.3f, 0.1f, 0.5f, 1.0f);
    private static final Vector3 ROOF_NORMAL = new Vector3(0,0,1);
    private static final float WAY_WIDTH = 0.008f;
    private static final float TOWER_HEIGHT = 0.10f ;
    private static final String RIVER = "river";
    private static final float STREET_UP = 0.008f;


    public Scene threeDimWorldFromDatas(OsmRawDataSet dataSet, Settings settings) throws IOException {
        Random generator = new Random();
        Map<String,Mesh> models = readModels();
        List<WorldObject> objects = new LinkedList<WorldObject>();
        List<WorldObject> powerTowers = new LinkedList<WorldObject>();
        Vector3[][] hgtPositions = generateTerrainVerticesPositions(readHgt(settings),dataSet.getBound());
        Mesh terrain = generateTerrainMesh(hgtPositions);
        List<Float> buildingVertices = new ArrayList<>(1388608);
        List<Float> streetsVertices = new ArrayList<>(1388608);
        List<Float> bezierVertices = new ArrayList<>(588608);


        Map<Long, Node> nodes = dataSet.getNodes();
        for (Map.Entry<Long, Node> nodeEntry : nodes.entrySet()) {
            Node node = nodeEntry.getValue();
            boolean isLamp = false;
            boolean isTree = false;
            for (Tag tag : node.getTags()) {
                if (tag.getValue().equals(TREE)){
                    isTree = true;
                }else if (tag.getValue().equals(STREETLAMP)){
                    isLamp = true;
                }
            }

            if(isTree){
                Vector2 positionInWorld = normalizePosition(node, dataSet.getBound());
                Vector3 translation = new Vector3(positionInWorld,calculateHeight(positionInWorld,hgtPositions));
                Matrix4 matrix = new Matrix4();
                matrix.translate(translation);
                matrix.scale(0.01f,0.01f,0.01f);
                matrix.rotate(1,0,0,90);
                objects.add(new WorldObject(models.get(TREE),matrix));
            }else if(isLamp){
                Vector2 positionInWorld = normalizePosition(node, dataSet.getBound());
                Vector3 translation = new Vector3(positionInWorld,calculateHeight(positionInWorld,hgtPositions));
                Matrix4 matrix = new Matrix4();
                matrix.translate(translation);
                matrix.scale(0.0001f,0.0001f,0.0002f);
                matrix.rotate(1,0,0,90);
                objects.add(new WorldObject(models.get(STREETLAMP),matrix));
            }
        }

        Map<Long, Way> ways = dataSet.getWays();
        for (Map.Entry<Long, Way> wayEntry : ways.entrySet()) {
            Way way = wayEntry.getValue();
            // TODO maybe there is better way to check tags
            boolean isBuilding = false;
            boolean isWay = false;
            boolean isTower = false;
            boolean isRiver = false;
            for (Tag tag : way.getTags()) {
                if (tag.getKey().equals(BUILDING)) {
                    isBuilding = true;
                }else if (tag.getKey().equals(WAY)) {
                    isWay = true;
                }else if (tag.getKey().equals(ELECTRITY_TOWER)){
                    isTower = true;
                }else if (tag.getKey().equals(RIVER)){
                    isRiver = true;
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
                    float pointHeight = calculateHeight(new Vector2(positionInMesh.x, positionInMesh.y), hgtPositions);
                    minHeight = ( ( pointHeight < minHeight ) ? pointHeight : minHeight );
                }
                float buildingHeight = minHeight+ 0.03f+generator.nextFloat()/15.f;
                for (int j=4;j<=i;j+=2){
                    generateWall(vert,j,buildingVertices,buildingHeight,minHeight);
                }
                EarClippingTriangulator triangulator = new EarClippingTriangulator();
                ShortArray indices = triangulator.computeTriangles(vert, 0, (i));

                for (int index = 0; index < indices.size; index++) {
                    addVertex(new Vector3(vert[indices.get(index) * 2],vert[indices.get(index) * 2 + 1],buildingHeight), ROOF_COLOR,ROOF_NORMAL,buildingVertices);
                }
            }else if(isWay){

                    List<WayNode> wayNodes = way.getWayNodes();
                    Vector3 previousPoint = null;
                    List<Vector3> vert = new ArrayList<>(wayNodes.size());
                    for (WayNode wayNode : wayNodes) {
                        Node node = dataSet.getNodeByWayNode(wayNode);
                        Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                        Vector3 point = new Vector3(positionInMesh, STREET_UP + calculateHeight(positionInMesh, hgtPositions));
                        if(previousPoint!=null){
                            //todo if length is enough display street name
                            List<Vector3> additionalPoints = generateAdditionalPoints(hgtPositions, previousPoint, positionInMesh, point);
                            vert.addAll(additionalPoints);
                        }
                        vert.add(point);
                        previousPoint=point;
                    }
                    Color color = new Color(0.2f, 0.2f, 0.2f, 1f);
                    addStripes(streetsVertices,vert,WAY_WIDTH,color);

            }else if(isRiver) {
                List<WayNode> wayNodes = way.getWayNodes();
                Vector3 previousPoint = null;
                List<Vector3> vert = new ArrayList<>(wayNodes.size());
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodeByWayNode(wayNode);
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    Vector3 point = new Vector3(positionInMesh, STREET_UP + calculateHeight(positionInMesh, hgtPositions));
                    if(previousPoint!=null){
                        //todo if length is enough display street name
                        List<Vector3> additionalPoints = generateAdditionalPoints(hgtPositions, previousPoint, positionInMesh, point);
                        vert.addAll(additionalPoints);
                    }
                    vert.add(point);
                    previousPoint=point;
                }
                Color color = new Color(0.1f, 0.2f, 0.8f, 1f);
                addStripes(streetsVertices,vert,WAY_WIDTH,color);
            }else if(isTower){
                int i = 0;
                float[] vert = new float[50000];
                List<WayNode> wayNodes = way.getWayNodes();

                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodeByWayNode(wayNode);
                    Vector2 positionInMesh = normalizePosition(node, dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                }
                Vector3 pointA1Last = null;
                Vector3 pointA2Last = null;
                for (int j = 0; j <= i - 4; j += 2) {
                    float pointAx = vert[j];
                    float pointAy = vert[1 + j];
                    float pointBx = vert[2 + j];
                    float pointBy = vert[3 + j];

                    Vector2 pointA = new Vector2(pointAx, pointAy);
                    Vector2 pointB = new Vector2(pointBx, pointBy);

                    float pointAHeight =  calculateHeight(pointA,hgtPositions);
                    float pointBHeight = calculateHeight(pointB,hgtPositions);

                    Vector3 translation = new Vector3(pointA,pointAHeight);
                    Matrix4 matrix = new Matrix4();
                    matrix.translate(translation);
                   // matrix.scale(0.01f,0.01f,0.01f);
                    matrix.scale(0.01f,0.01f,0.01f);
                    matrix.rotate(1,0,0,90);
                    float angle = (float)((180/Math.PI)*(Math.atan2(pointB.y-pointA.y,pointB.x-pointA.x)));
                    matrix.rotate(0,1,0,angle+90f);
                    powerTowers.add(new WorldObject(models.get(ELECTRITY_TOWER),matrix));

                    if (j<=i-4) {
                        Color color = new Color(0.9f, 0.1f, 0.1f, 1f);
                        Vector3 pointQ = new Vector3(new Vector2(pointA), pointAHeight + TOWER_HEIGHT);
                        Vector3 pointW = new Vector3(new Vector2(pointB), pointBHeight + TOWER_HEIGHT);
                        Vector2 leftVector = calcLeftVector(0.035f, pointQ, pointW);
                        Vector3 pointA1 = new Vector3(pointQ.x - leftVector.x, pointQ.y - leftVector.y, pointQ.z);
                        //Vector3 pointB1 = new Vector3 ( pointW.x-leftVector.x,pointW.y-leftVector.y,pointW.z);
                        Vector3 pointA2 = new Vector3(pointQ.x + leftVector.x, pointQ.y + leftVector.y, pointQ.z);
                        //Vector3 pointB2 = new Vector3 ( pointW.x+leftVector.x,pointW.y+leftVector.y,pointW.z);
                        float height = -0.06f;

                        if (pointA1Last != null && pointA2Last != null) {
                            addBezier(bezierVertices, pointA1, pointA1Last, height, color);
                            addBezier(bezierVertices, pointA2, pointA2Last, height, color);
                        }
                        pointA1Last = pointA1;
                        pointA2Last = pointA2;
                        //
                    }
                }

                }

        }

        float[] buildingsVertArray = new float[buildingVertices.size()];
        for (int j = 0; j < buildingVertices.size(); j++) {
            buildingsVertArray[j] = buildingVertices.get(j);
        }
        float[] bezierVertArray = new float[bezierVertices.size()];
        for (int j = 0; j < bezierVertices.size(); j++) {
            bezierVertArray[j] = bezierVertices.get(j);
        }
        float[] streetVertArray = new float[streetsVertices.size()];
        for (int j = 0; j < streetsVertices.size(); j++) {
            streetVertArray[j] = streetsVertices.get(j);
        }
        Mesh buildingsMesh = new Mesh(true, buildingVertices.size(), 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
            buildingsMesh.setVertices(buildingsVertArray);

        Mesh beziersMesh = new Mesh(true, bezierVertices.size(), 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        beziersMesh.setVertices(bezierVertArray);

        Mesh streetsMesh = new Mesh(true, streetsVertices.size(), 0,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(Usage.Normal,3 , "a_normal"));
        streetsMesh.setVertices(streetVertArray);
        return new Scene(buildingsMesh,terrain,streetsMesh,beziersMesh,powerTowers,objects);
    }

    private List<Vector3> generateAdditionalPoints(Vector3[][] hgtPositions, Vector3 previousPoint, Vector2 positionInMesh, Vector3 point) {
        List<Vector3> additionalPoints = new LinkedList<>();
        Vector2 hgtStep = calculateStep(hgtPositions);
        Vector2i previousIndexes = calculateHgtSquareIndex(new Vector2(previousPoint.x,previousPoint.y),hgtPositions);
        Vector2i indexes = calculateHgtSquareIndex(positionInMesh,hgtPositions);
        Vector3 sub = new Vector3(previousPoint).sub(point);
        if (sub.x>0.0001f) {
            for (int i = indexes.x + 1; i <= previousIndexes.x; i++) {
                generateAdditionalPointX(hgtPositions, point, additionalPoints, hgtStep, sub, i);
            }
        }else if (sub.x<-0.0001f){
            for(int i = previousIndexes.x+1; i <= indexes.x;i++){
                generateAdditionalPointX(hgtPositions, point, additionalPoints, hgtStep, sub, i);
            }
        }
        if (sub.y>0.0001f) {
            for (int i = indexes.y + 1; i <= previousIndexes.y; i++) {
                generateAdditionalPointY(hgtPositions, point, additionalPoints, hgtStep, sub, i);
            }
        }else if (sub.y<-0.0001f) {
            for (int i = previousIndexes.y + 1; i <= indexes.y; i++) {
                generateAdditionalPointY(hgtPositions, point, additionalPoints, hgtStep, sub, i);
            }
        }
        if (sub.x>0.0001f) {
            additionalPoints.sort((vectorA, vectorB) -> (vectorA.x > vectorB.x) ? -1 : 1);
        } else if (sub.x<-0.0001f) {
            additionalPoints.sort((vectorA, vectorB) -> (vectorA.x > vectorB.x) ? 1 : -1);
        }else if (sub.y>0.0001f) {
            additionalPoints.sort((vectorA, vectorB) -> (vectorA.x > vectorB.x) ? -1 : 1);
        }else if (sub.y<-0.0001f) {
            additionalPoints.sort((vectorA, vectorB) -> (vectorA.x > vectorB.x) ? 1 : -1);
        }
        return additionalPoints;
    }

    private void generateAdditionalPointX(Vector3[][] hgtPositions, Vector3 point, List<Vector3> additionalPoints, Vector2 hgtStep, Vector3 sub, int i) {
        float x = i * hgtStep.x;
        float y = getAdditionalY(point, sub, x);
        float z = STREET_UP+calculateHeight(new Vector2(x,y), hgtPositions);//getAdditionalZ(point, sub, x);
        additionalPoints.add(new Vector3(x, y, z));
    }

    private void generateAdditionalPointY(Vector3[][] hgtPositions, Vector3 point, List<Vector3> additionalPoints, Vector2 hgtStep, Vector3 sub, int i) {
        float y = i * hgtStep.y;
        float x = getAdditionalX(point, sub, y);
        float z = STREET_UP+calculateHeight(new Vector2(x,y), hgtPositions);//getAdditionalZ(point, sub, x);

        additionalPoints.add(new Vector3(x, y, z));
    }

    private float getAdditionalX(Vector3 point, Vector3 sub, float y) {
        float x;
        float a = (sub.x/sub.y);
        float b = point.x-a*point.y;
        x = a*y+b;
        return x;
    }

    private float getAdditionalZ(Vector3 point, Vector3 sub, float x) {
        float z;
        float a = (sub.z/sub.x);
        float b = point.z-a*point.x;
        z = a*x+b;
        return z;
    }

    private float getAdditionalY(Vector3 point, Vector3 sub, float x) {
        float y;
        float a = (sub.y/sub.x);
        float b = point.y-a*point.x;
        y = a*x+b;
        return y;
    }

    private Vector2i calculateHgtSquareIndex(Vector2 position,Vector3[][] hgtPositions) {
        int indexX = (int)(position.x*(hgtPositions.length-1)/10.f);
        int indexY = (int)(position.y*(hgtPositions[0].length-1)/10.f);
        return new Vector2i(indexX,indexY);
    }

    private Vector2 calculateStep(Vector3[][] hgtPositions) {
        return new Vector2(hgtPositions[1][0].x-hgtPositions[0][0].x,hgtPositions[0][1].y-hgtPositions[0][0].y);
    }

    void addStrip(List<Float> vertices,Vector3 pointA,Vector3 pointB,float width,Color color){
        Vector3 vectorR = new Vector3(pointB).sub(pointA);
        vectorR = vectorR.nor();
        vectorR = vectorR.scl(width);                // r - way width //todo fix rotations fix przerwy
        Vector3 pointQ = new Vector3 (new Vector3(pointA).add(vectorR.rotate(Vector3.Z,90)));
        Vector3 pointW = new Vector3 (new Vector3(pointA).add(vectorR.rotate(Vector3.Z,180)));
        Vector3 pointE = new Vector3 (new Vector3(pointB).add(vectorR));
        Vector3 pointR = new Vector3 (new Vector3(pointB).add(vectorR.rotate(Vector3.Z,180)));
        addTriangle(vertices,pointQ,pointW,pointE,calculateNormal(pointQ,pointW,pointE),color);
        addTriangle(vertices,pointQ,pointR,pointE,calculateNormal(pointE,pointR,pointQ),color);
    }

    void addSquare(List<Float> vertices,Vector3 pointA,Vector3 pointB,Vector3 pointC,Vector3 pointD,Color color){
        addTriangle(vertices,pointA,pointB,pointC,calculateNormal(pointA,pointB,pointC),color);
        addTriangle(vertices,pointA,pointD,pointC,calculateNormal(pointC,pointD,pointA),color);
    }

    private void addBezier(List<Float> vertices, Vector3 controlPointA, Vector3 controlPointC, float height, Color color) {
        Vector3 pointCenter = new Vector3((controlPointA.x+controlPointC.x)/2.f,(controlPointA.y+controlPointC.y)/2f,(controlPointA.z+controlPointC.z)/2f+height);
        List<Vector3> controlPoints = new LinkedList<>();
        controlPoints.add(controlPointA);
        controlPoints.add(pointCenter);
        controlPoints.add(controlPointC);
        List<Vector3> resultPoints = generateBezierPoints(controlPoints,100);

        addStripes(vertices,resultPoints,0.004f,color);
    }

    private void addStripes(List<Float> vertices, List<Vector3> resultPoints,float width, Color color) {
//        Vector3 pointA = resultPoints.get(0);
//        Vector3 pointB = resultPoints.get(1);
//        Vector2 vectorR = new Vector2(pointB.x,pointB.y).sub(new Vector2(pointA.x,pointA.y));
//        vectorR = vectorR.nor();
//        vectorR = vectorR.scl(width);                // r - way width //todo fix rotations fix przerwy
//        Vector2 VectorLeft = new Vector2(vectorR).rotate90(1);
//        Vector2 VectorRight = new Vector2(vectorR).rotate90(-1);

//        Vector3 pointQ = new Vector3 (new Vector3(resultPoints.get(0)).add(vectorR.rotate(Vector3.Z,90)));
//        Vector3 pointW = new Vector3 (new Vector3(resultPoints.get(0)).add(vectorR.rotate(Vector3.Z,180)));


        for(int i=1;i<resultPoints.size();i++){

            Vector3 pointA = resultPoints.get(i-1);
            Vector3 pointB = resultPoints.get(i);
            Vector2 vectorLeft= calcLeftVector(width, pointA, pointB);
            //Vector2 VectorRight = new Vector2(vectorR).rotate90(-1);
            Vector3 pointQ = new Vector3(pointA.x+vectorLeft.x,pointA.y+vectorLeft.y,pointA.z);
            Vector3 pointW = new Vector3(pointA.x-vectorLeft.x,pointA.y-vectorLeft.y,pointA.z);
            Vector3 pointE = new Vector3(pointB.x-vectorLeft.x,pointB.y-vectorLeft.y,pointB.z);
            Vector3 pointR = new Vector3(pointB.x+vectorLeft.x,pointB.y+vectorLeft.y,pointB.z);
            addSquare(vertices,pointQ,pointW,pointE,pointR,color);
//              addStrip(vertices,resultPoints.get(i-1),resultPoints.get(i),width,color);
//            Vector3 pointE = new Vector3 (new Vector3(resultPoints.get(i)).add(vectorR));
//            Vector3 pointR = new Vector3 (new Vector3(resultPoints.get(i)).add(vectorR.rotate(Vector3.Z,180)));
//            addTriangle(vertices,pointQ,pointW,pointE,calculateNormal(pointQ,pointW,pointE),color);
//            addTriangle(vertices,pointQ,pointR,pointE,calculateNormal(pointE,pointR,pointQ),color);
        }
    }

    private Vector2 calcLeftVector(float width, Vector3 pointA, Vector3 pointB) {
        Vector2 vectorR = new Vector2(pointB.x,pointB.y).sub(new Vector2(pointA.x,pointA.y));
        vectorR = vectorR.nor();
        vectorR = vectorR.scl(width);                // r - way width //todo fix rotations fix przerwy
        return new Vector2(vectorR).rotate90(1);
    }

//    private List<Vector3> generateBezierPoints(Vector3 pointQ, Vector3 pointCenter, Vector3 pointW,int pointsNumber) {
//    }

    private float calculateBernsteinFactor(int n, int i, double t){
        return  (float)(newton(n,i)*Math.pow(t,i)*Math.pow((1-t),(n-i)));
    }

    private List<Vector3> generateBezierPoints(List<Vector3> controlPoints,int resultPointsNumber) {
        List<Vector3> result = new ArrayList();
        int n = controlPoints.size()-1;
        for (float t=0.f;t<=1.0f;t+=1.f/resultPointsNumber){
            Vector3 point = new Vector3();
            for (int i=0;i<=n;i++){
                //for(Vector3 controlPoint : controlPoints){
                Vector3 controlPoint = controlPoints.get(i);
                float bernsteinFactor = calculateBernsteinFactor(n, i, t);
                point.x+=controlPoint.x*bernsteinFactor;
                point.y+=controlPoint.y*bernsteinFactor;
                point.z+=controlPoint.z*bernsteinFactor;
                //}
            }
            result.add(point);
        }
//        for (int i=0;i<7;i++) {
//            System.out.println("factorial("+i+")"+ factorial(i));
//            System.out.println("newton("+i+"|0)"+ newton(i,2));
//        }
        return result;
    }

    private long factorial(int n){
        int result=1;
        for(int i=1;i<=n;i++){
            result*=i;
        }
        return result;
    }
    private long newton(int n,int k){
        return factorial(n)/(factorial(k)*factorial(n-k));
    }

    private float calculateHeight(Vector2 position, Vector3[][] htgPositions) {
        float indexXf = (position.x*(htgPositions.length-1)/10.f);
        float indexYf = (position.y*(htgPositions[0].length-1)/10.f);

        indexXf = (indexXf<htgPositions.length-1.0001f) ?indexXf:htgPositions.length-1.0001f;
        indexYf = (indexYf<htgPositions[0].length-1.0001f) ?indexYf:htgPositions[0].length-1.0001f;

        float localX = indexXf - (int)indexXf;
        float localY = indexYf - (int)indexYf;

        int indexX = (int)indexXf;
        int indexY = (int)indexYf;

        float heightA;
        float heightB = htgPositions[indexX+1][indexY].z;
        float heightC = htgPositions[indexX][indexY+1].z;

        if(localX>1-localY) {
            heightA = htgPositions[indexX + 1][indexY + 1].z;
            return calculateBarrycentric(new Vector3(1, 1, heightA), new Vector3(1, 0, heightB), new Vector3(0, 1, heightC), new Vector2(localX, localY));
        }else {
            heightA = htgPositions[indexX][indexY].z;
            return calculateBarrycentric(new Vector3(0, 0, heightA), new Vector3(1, 0, heightB), new Vector3(0, 1, heightC), new Vector2(localX, localY));
        }
 }

public static float calculateBarrycentric(Vector3 point1,
            Vector3 point2, Vector3 point3, Vector2 position) {
    float det = (point2.y - point3.y) * (point1.x - point3.x) -
            (point2.x - point3.x) * (point1.y - point3.y);
    float lambda1 = ((point2.y - point3.y) * (position.x - point3.x) -
            (point2.x - point3.x) * (position.y - point3.y)) / det;
    float lambda2 = ((point3.y - point1.y) * (position.x - point3.x) -
            (point3.x - point1.x) * (position.y - point3.y)) / det;
    float lambda3 = 1.0f - lambda1 - lambda2;
    return lambda1 * point1.z + lambda2 * point2.z + lambda3 * point3.z;
}



    private short[][] readHgt(Settings settings) throws IOException {
        HgtReader hgtReader = new HgtReader();
        return hgtReader.readHgt(settings);
    }
    private Map<String, Mesh> readModels() {
        Map<String, Mesh>  models = new HashMap<>();
        ModelsReader modelsReader = new ModelsReader();
        try {
            models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/tree.obj").read()));
            models.put(ELECTRITY_TOWER,modelsReader.loadMesh(Gdx.files.internal("models/towerPower.obj").read()));
            models.put(STREETLAMP,modelsReader.loadMesh(Gdx.files.internal("models/street_lamp.obj").read()));
           // models.put(TREE,modelsReader.loadMesh(Gdx.files.internal("models/StreetLamp.obj").read()));
        } catch (IOException e) {
            //todo handle error
            e.printStackTrace();
        }
        return models;
    }

    private void generateWall(float[] vert, int i, List<Float> vertices, float roofHeight,float minHeight) {
        float pointAx=vert[i-4];
        float pointAy=vert[i-3];
        float pointBx=vert[i-2];
        float pointBy=vert[i-1];

        //generating normal
        Vector2 pointAa = new Vector2(pointAx, pointAy);
        Vector2 pointBb = new Vector2(pointBx, pointBy);

        Vector3 pointA = new Vector3(pointAa,roofHeight);
        Vector3 pointB = new Vector3(pointAa,minHeight);
        Vector3 pointC = new Vector3(pointBb,minHeight);
        Vector3 pointD = new Vector3(pointBb,roofHeight);

        Vector3 normal = new Vector3((new Vector2(pointBb).sub(pointAa)).rotate90(1),0);
        Color wallColor = new Color(0.9f,0.3f,0.1f,1.0f);

        normal = normal.nor();
        addTriangle(vertices, pointA, pointB, pointC, normal, wallColor);
        addTriangle(vertices, pointC, pointD, pointA, normal, wallColor);
    }

    private void addTriangle(List<Float> vertices, Vector3 pointA, Vector3 pointB, Vector3 pointC, Vector3 normal, Color wallColor) {
//        Color c1 = new Color(1f,0f,0f,1f);
//        Color c2 = new Color(0f,1f,0f,1f);
//        Color c3 = new Color(0f,0f,1f,1f);
        addVertex(pointA,wallColor,normal,vertices);
        addVertex(pointB,wallColor,normal,vertices);
        addVertex(pointC,wallColor,normal,vertices);
//        addVertex(pointA,c1,normal,vertices);
//        addVertex(pointB,c2,normal,vertices);
//        addVertex(pointC,c3,normal,vertices);
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
        Color testColor = new Color(0.1f,0.4f,0.0f,1f);
        for (int i = 0 ; i < positionsArray.length-1 ; i++){
            for (int j = 0 ; j < positionsArray[i].length-1 ; j++){
//                if (i<3&&j<3) {//TODO
//                    Vector3 normal = calculateNormal(positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j + 1]);
//                    addTriangle(vertices, positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j], normal, terrainColor);
//                    normal = calculateNormal(positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j]);
//                    normal.scl(-1f);
//                    addTriangle(vertices, positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j], normal, terrainColor);
//
//                }else {
                    Vector3 normal = calculateNormal(positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j + 1]);
                    addTriangle(vertices, positionsArray[i][j], positionsArray[i][j + 1], positionsArray[i + 1][j], normal, testColor);
                    normal = calculateNormal(positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j]);
                    normal.scl(-1f);
                    addTriangle(vertices, positionsArray[i + 1][j + 1], positionsArray[i][j + 1], positionsArray[i + 1][j], normal, testColor);

//                }
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
                float posZ = generateTerrainVertexHeight(hgtArray[beginX+i][beginY+j]);//0.1f+0.1f*(i/10)+0.1f*(j/10);//
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

        double x = (node.getLongitude() - bound.getLeft())/(bound.getRight()-bound.getLeft()) * 10;
        double y = (node.getLatitude() - bound.getBottom())/(bound.getTop()-bound.getBottom()) * 10;
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
                    vectorR = vectorR.scl(0.005f);                // r - way width
                    Color color = new Color(0.3f, 0.3f, 0.3f, 1f);
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
    private class Vector2i {
        public int x;
        public int y;
        Vector2i(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
}
