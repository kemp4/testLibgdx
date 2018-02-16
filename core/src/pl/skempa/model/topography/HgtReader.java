package pl.skempa.model.topography;

import com.badlogic.gdx.Gdx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

import pl.skempa.controller.app.Settings;
import pl.skempa.util.PbfReader;

/**
 * Created by Mymon on 2017-12-25.
 */

public class HgtReader {
private final int COLUMNS = 3601;
    public short[][] readHgt(Settings settgins) throws IOException {
        short[][] hgtArray = new short[COLUMNS][];
        for (int j = 0; j < 3601; j++) {
            hgtArray[j] = new short[COLUMNS];
        }
        if(settgins.choosenHgt.equals("flat")){
            for(int i=0;i<COLUMNS;i++) {
                for(int j=0;j<COLUMNS;j++){
                    hgtArray[i][j] = 2000;
                }
            }
        }else {


            FileChannel fc = new FileInputStream(Gdx.files.internal("hgtFiles/" + settgins.choosenHgt + ".hgt").file()).getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
            while (bb.remaining() > 0) fc.read(bb);
            fc.close();
            bb.flip();
            ShortBuffer sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();


            long i = 0;
            while (sb.hasRemaining()) {
                short test = sb.get();
                //min = (min<test)?min:test;
                //max = (max>test)?max:test;

                hgtArray[(int) (i % COLUMNS)][(int) (i / COLUMNS)] = test;

                i++;
            }
        }
        return hgtArray;
    }
}
