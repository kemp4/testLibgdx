package pl.skempa.model.object.rawdata;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by szymk on 12/20/2017.
 */

public class ModelsReader {

    float[] vertices;
    int offset;
    public Mesh loadMesh(InputStream inputStream) throws IOException {
        vertices = new float[100000];
        offset = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<Vector3> verticesPositions = new ArrayList<Vector3>();
        List<Vector3> verticesNormals = new ArrayList<Vector3>();
        Color color=new Color(0,0,0,0);
        Map<String, Mtl> materials=null;
        while((line = bufferedReader.readLine()) != null) {
            String[] words = line.split(" ");
            String firstWord = words[0];
            if(firstWord.equals("#")){
                //ignore
            }else if(firstWord.equals("o")){
                // new object ignore too
            }else if(firstWord.equals("v")){
                verticesPositions.add(new Vector3(Float.parseFloat(words[1]),Float.parseFloat(words[2]),Float.parseFloat(words[3])));
            }else if(firstWord.equals("vt")){
                //ignore not gonna to use textures
            }else if(firstWord.equals("vn")){
                verticesNormals.add(new Vector3(Float.parseFloat(words[1]),Float.parseFloat(words[2]),Float.parseFloat(words[3])));
            }else if(firstWord.equals("f")){
                for(int i=1;i<=3;i++) {
                    addVector3(verticesPositions.get(getVertexPositionIndex(words[i])));
                    addColor(color);
                    addVector3(verticesNormals.get(getVertexNormalIndex(words[i])));
                }
            }else if(firstWord.equals("usemtl")){
                //color= new Color(0.15f,0.5f,0.0f,1f);
                color = new Color(materials.get(words[1]).getKd().x,materials.get(words[1]).getKd().y,materials.get(words[1]).getKd().z,1.0f);
            }else if(firstWord.equals("mtllib")){
                materials = readMtl(words[1]); // todo  material jako wyjsciowy parametr gdyby istniala mozliwosc wiekszej ilosci mtllibow
            }

        }
        float[] result = new float[offset];
        for (int j = 0; j < offset; j++) {
            result[j] = vertices[j];
        }



        Mesh mesh;
        mesh = new Mesh(true, offset, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color"),
                new VertexAttribute(VertexAttributes.Usage.Normal,3 , "a_normal")
        );
        mesh.setVertices(result);
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
    private Map<String,Mtl> readMtl(String word) throws IOException {
        InputStream inputStream =  Gdx.files.internal("models/"+word).read();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Map<String,Mtl> materials = new HashMap<String,Mtl>();
        Mtl mtl=null;
        while((line = bufferedReader.readLine()) != null) {
            String[] words = line.split(" ");
            String firstWord = words[0];
            if (firstWord.equals("")&&mtl!=null){
                materials.put(mtl.getName(),mtl);
            }else {
                if (firstWord.equals("#")) {
                    //ignore
                } else if (firstWord.equals("newmtl")) {
                    mtl = new Mtl();
                    mtl.setName(words[1]);
                } else if (firstWord.equals("Kd")) {
                    mtl.setKd(new Vector3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3])));
                } else if (firstWord.equals("Ks")) {
                    mtl.setKs(new Vector3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3])));
                } else if (firstWord.equals("Ka")) {
                    mtl.setKa(new Vector3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3])));
                } else if (firstWord.equals("Ke")) {
                    mtl.setKe(new Vector3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3])));
                } else if (firstWord.equals("Ns")) {
                    mtl.setNs(Float.parseFloat(words[1]));
                } else if (firstWord.equals("Ni")) {
                    mtl.setNi(Float.parseFloat(words[1]));
                } else if (firstWord.equals("d")) {
                    mtl.setD(Float.parseFloat(words[1]));
                } else if (firstWord.equals("illum")) {
                    mtl.setIllum(Integer.parseInt(words[1]));
                }
            }
        }
        return materials;
    }

    private int getVertexNormalIndex(String word) {
        String[] result = word.split("/");
        return Integer.parseInt(result[2])-1;
    }

    private int getVertexPositionIndex(String word) {
        String[] result = word.split("/");
        return Integer.parseInt(result[0])-1;
    }
}
