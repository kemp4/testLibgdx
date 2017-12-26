package pl.skempa.model.topography;

import com.badlogic.gdx.Gdx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Mymon on 2017-12-25.
 */

public class HgtReader {
private final int COLUMNS = 3601;
    public short[][] readHgt() throws IOException {
        FileChannel fc = new FileInputStream( Gdx.files.internal("hgtFiles/N34W118.hgt").file()).getChannel();
        ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
        while (bb.remaining() > 0) fc.read(bb);
            fc.close();
            bb.flip();
        // choose the right endianness
            ShortBuffer sb = bb.order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
//        short min,max;
//        min= sb.get(0);
//        max = sb.get(0);
        short[][] hgtArray = new short[COLUMNS][];
        for (int j = 0 ; j < 3601 ;j++){
            hgtArray[j]=new short[COLUMNS];
        }
        long i=0;
        while(sb.hasRemaining()) {
            short test = sb.get();
            //min = (min<test)?min:test;
            //max = (max>test)?max:test;
            hgtArray[(int)(i%COLUMNS)][(int)(i/COLUMNS)]= test;
            i++;
        }
        return hgtArray;
    }
}
