package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import java.util.List;
import java.util.Map;

/**
 * Created by szymk on 11/25/2017.
 */

public class OsmBaseObject {
    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
    public static final int MAX_TRIS = 1;
    public static final int MAX_VERTS = MAX_TRIS * 3;
    public static final String BUILDING = "building";
    public static final String WAY = "way";
    //TODO lot of refactor
    private float[] vertices;
    private int offset;
    public Mesh fromWays(OsmRawDataSet dataSet){

        vertices= new float[10000000];
        offset=0;
        Map<Long,Way> ways = dataSet.getWays();
        for (Map.Entry<Long,Way> wayEntry : ways.entrySet()){
            Way way= wayEntry.getValue();
           // Tag tag = new Tag("building","yes");
            boolean isBuilding = false;
            boolean isWay = false;
            for( Tag tag : way.getTags()){
                if(tag.getKey().equals(BUILDING)){
                    isBuilding = true;
                }
                if(tag.getKey().equals(WAY)){
                        isWay = true;
                }
            }
            if(isBuilding) {
                int i = 0;
                float[] vert = new float[50000];
                List<WayNode> wayNodes= way.getWayNodes();
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodes().get(wayNode.getNodeId());
                    Vector2 positionInMesh = normalizePosition(node,dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                }
                ShortArray indices;
                EarClippingTriangulator triangulator2 = new EarClippingTriangulator();
                indices = triangulator2.computeTriangles(vert, 0, (i));
                for (int index = 0; index < indices.size; index++) {
                    vertices[offset++] = vert[indices.get(index) * 2];
                    vertices[offset++] = vert[indices.get(index) * 2 + 1];
                    vertices[offset++] = 0.3f;    //Color(r, g, b, a)
                    vertices[offset++] = 0.1f;
                    vertices[offset++] = 0.5f;
                    vertices[offset++] = 0.7f;
                }
            }
            if (isWay){
                int i = 0;
                float[] vert = new float[50000];
                List<WayNode> wayNodes= way.getWayNodes();
                //float lastNode=dataSet.getNodes().;
                for (WayNode wayNode : wayNodes) {
                    Node node = dataSet.getNodes().get(wayNode.getNodeId());
                    Vector2 positionInMesh = normalizePosition(node,dataSet.getBound());
                    vert[i++] = positionInMesh.x;
                    vert[i++] = positionInMesh.y;
                }
                int j =0;
                while( j<(i/2)-1 ){
                    float pointAx = vert[j++];
                    float pointAy = vert[j++];
                    float pointBx = vert[j+1];
                    float pointBy = vert[j+2];
                    Vector2 pointA = new Vector2(pointAx,pointAy);
                    Vector2 pointB = new Vector2(pointBx,pointBy);
                    Vector2 vectorR = pointB.sub(pointA);
                    vectorR = vectorR.nor();
                    vectorR = vectorR.scl(0.1f);                // r
                    Color color = new Color(0.3f,0.1f,0.6f,0.9f);
                    addPoint(pointA.add(vectorR.rotate(90)));
                    addColor(color);
                    addPoint(pointA.add(vectorR.rotate(-90)));
                    addColor(color);
                    addPoint(pointB.add(vectorR.rotate(-90)));
                    addColor(color);
                    addPoint(pointB.add(vectorR.rotate(90)));
                    addColor(color);
                    addPoint(pointB.add(vectorR.rotate(-90)));
                    addColor(color);
                    addPoint(pointA.add(vectorR.rotate(-90)));
                }
            }
        }

        float[] result = new float[offset];
        for(int j = 0 ;j<offset;j++){
            result[j]=vertices[j];
        }
        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        mesh.setVertices(result);
        return mesh;
    }

    private void addColor(Color color) {
        vertices[offset++] = color.r;    //Color(r, g, b, a)
        vertices[offset++] = color.g;
        vertices[offset++] = color.b;
        vertices[offset++] = color.a;
    }

    private void addPoint(Vector2 point) {
        vertices[offset++] = point.x;
        vertices[offset++] = point.y;
    }



    //// TODO: 11/27/2017 refactor name
    private static Vector2 normalizePosition(Node node, Bound bound) {
        double x =  (node.getLongitude() - bound.getLeft())*100;
        double y =  (node.getLatitude() - bound.getBottom())*100;
        return new Vector2((float)x,(float)y);
    }
}
