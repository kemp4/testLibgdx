package pl.skempa.libgdxtest.test;

<<<<<<< HEAD
<<<<<<< HEAD
import com.badlogic.gdx.Gdx;

=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

<<<<<<< HEAD
<<<<<<< HEAD
import pl.skempa.model.object.rawdata.OsmRawDataSet;
import pl.skempa.util.PbfReader;
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
import pl.skempa.util.XmlUtil;
import pl.skempa.util.XmlUtilBySax;
import pl.skempa.util.XmlUtilImpl;

/**
 * Created by Mymon on 2017-10-08.
 */

public class xmlUtilTest {


<<<<<<< HEAD
<<<<<<< HEAD
=======



>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======



>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
    /**
     * test for profiler                    Size        a       timeByGdxXml    timeBySax
     * bbox=19.05%2C50.20%2C19.07%2C50.22   1,5KB       (0.02)  0.5sec
     * bbox=19.05%2C50.20%2C19.08%2C50.23   6,6KB       (0.03)  1.4sec
     * bbox=19.05%2C50.20%2C19.09%2C50.24   8,8KB       (0.04)  1.4sec
     * bbox=19.05%2C50.20%2C19.10%2C50.25   13,6KB      (0.05)  2,5sec          1,1sec
     * bbox=19.05%2C50.20%2C19.11%2C50.26   faild       (0.06)
<<<<<<< HEAD
<<<<<<< HEAD
     * <p>
     * bbox=139.6565%2C35.6712%2C139.6765%2C35.6712(tokio)  (0.2)
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
     */
    @Ignore("just for profiler")
    @Test
    public void xmlSpeedParsingTest() throws IOException {
<<<<<<< HEAD
<<<<<<< HEAD
        // XmlUtil should be singleton?
        XmlUtil xmlUtil = new XmlUtilBySax();
        long startTime = System.currentTimeMillis();
        InputStream is = new FileInputStream("C:\\Users\\szymk\\Documents\\work\\libgdx\\testLibgdx\\android\\assets\\mapFiles\\tokioBig.osm");
        System.out.println("creating input stream time" + (System.currentTimeMillis() - startTime));
        xmlUtil.readXml(is);
        System.out.println("total time" + (System.currentTimeMillis() - startTime));
    }

//    @Ignore("just for profiler")
//    @Test
//    public void osmPbfSpeedParsingTime() throws IOException{
//        PbfReader pbfReader = new PbfReader();
//        OsmRawDataSet dataSet = pbfReader.parsePbf(Gdx.files.internal("mapFiles/tokio.pbf").read());
//        dataSet.getWays().keySet().toArray().toString();
//
//    }
}
=======
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
       // XmlUtil should be singleton?
        XmlUtil xmlUtil = new XmlUtilBySax();
        long startTime = System.currentTimeMillis();
        InputStream is=new FileInputStream("C:\\Users\\szymk\\Documents\\work\\libgdx\\testLibgdx\\android\\assets\\mapFiles\\forProfile05.osm");
        System.out.println("creating input stream time"+(System.currentTimeMillis()-startTime));
        xmlUtil.readXml(is);
        System.out.println("total time"+(System.currentTimeMillis()-startTime));
    }

}
<<<<<<< HEAD
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
=======
>>>>>>> f9f7cb055ed76375e46e8e87003c2529942cfa9c
