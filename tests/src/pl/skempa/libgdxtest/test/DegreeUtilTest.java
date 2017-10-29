package pl.skempa.libgdxtest.test;

import com.badlogic.gdx.math.Vector3;

import org.junit.Before;
import org.junit.Test;

import pl.skempa.util.DegreeUtil;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mymon on 2017-10-16.
 */

public class DegreeUtilTest {
    private Vector3 vectorMin;
    private Vector3 vectorMax;

    @Before
    public void before()
    {
        vectorMin=new Vector3(18.02f,59.13f,0f);
        vectorMax=new Vector3(18.0212f,59.25f,0f);
    }

    @Test
    public void generatinOSMApiParamStringTest() {
        String resultByUtil = DegreeUtil.asApiBBoxParam(vectorMin,vectorMax);
        String resultExcepted = "18.02,59.13,18.0212,59.25";
        assertEquals(resultByUtil, resultExcepted);
    }
}
