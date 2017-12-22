package pl.skempa.model.object;

import com.badlogic.gdx.graphics.Mesh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by szymk on 12/20/2017.
 */

public class ModelsReader {
    public Mesh loadMesh(InputStreamReader inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(inputStream);
        String line;
        while((line = bufferedReader.readLine()) != null) {
            String[] words = line.split(" ");
            String firstWord = words[0];
            if(firstWord.equals("#")){

            }else if(firstWord.equals("o")){

            }else if(firstWord.equals("v")){

            }else if(firstWord.equals("vt")){

            }else if(firstWord.equals("vn")){

            }else if(firstWord.equals("f")){

            }

        }
        return null;
    }
}
