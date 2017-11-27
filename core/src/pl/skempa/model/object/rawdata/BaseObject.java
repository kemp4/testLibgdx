package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

import java.util.List;
import java.util.Map;


import pl.skempa.model.object.rawdata.RawDataSet;

/**
 * Created by Mymon on 2017-11-01.
 */



public class BaseObject {

    public static final int POSITION_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;
    public static final int MAX_TRIS = 1;
    public static final int MAX_VERTS = MAX_TRIS * 3;
    //TODO lot of refactor
    public static Mesh fromWays(Map<Long, Way> ways){

        float[] vertices= new float[250000];
        int offset=0;
        for (Map.Entry<Long,Way> wayEntry : ways.entrySet()){

            Way way= wayEntry.getValue();
            boolean isBuilding = !(null==way.getTags().get("building"));
            if(isBuilding) {
                int i = 0;
                float[] vert = new float[50000];
                List<Node> nodes = way.getNodes();
                for (Node node : nodes) {
                    vert[i++] = node.getLon();
                    vert[i++] = node.getLat();
                }
//                vert[i++]=vert[0];
//                vert[i++]=vert[1];
                ShortArray indices;
                DelaunayTriangulator triangulator= new DelaunayTriangulator();
                indices = triangulator.computeTriangles(vert,0,i,true);
                //EarClippingTriangulator triangulator2 = new EarClippingTriangulator();
                 //indices = triangulator2.computeTriangles(vert, 0, (i));
                for (int index = 0; index < indices.size; index++) {
                    vertices[offset++] = vert[indices.get(index) * 2];
                    vertices[offset++] = vert[indices.get(index) * 2 + 1];
                    vertices[offset++] = 0.3f;    //Color(r, g, b, a)
                    vertices[offset++] = 0.1f;
                    vertices[offset++] = 0.5f;
                    vertices[offset++] = 0.7f;
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


}
