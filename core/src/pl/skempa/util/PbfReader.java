package pl.skempa.util;
import org.openstreetmap.osmosis.core.container.v0_6.BoundContainer;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.*;
import org.openstreetmap.osmosis.core.task.v0_6.*;

import java.io.InputStream;
import java.util.Map;

import pl.skempa.model.object.rawdata.OsmRawDataSet;

/**
 * Created by szymk on 11/25/2017.
 */

public class PbfReader {



    public OsmRawDataSet parsePbf(InputStream input) {

        OsmRawDataSet result = new OsmRawDataSet();
        Sink sinkImplementation = new Sink() {
            @Override
            public void close() {}
            @Override
            public void complete() {}
            @Override
            public void initialize(Map<String, Object> metaData){}

            public void process(EntityContainer entityContainer) {
                Entity entity = entityContainer.getEntity();
                if (entity instanceof Node) {
                    result.getNodes().put(entity.getId(),(Node)entity);
                } else if (entity instanceof Way) {
                    result.getWays().put(entity.getId(),(Way)entity);
                } else if (entity instanceof Relation) {
                    result.getRelations().put(entity.getId(),(Relation)entity);
                } else if (entity instanceof Bound){
                    result.setBound((Bound)entity);

                }
            }
        };
        RunnableSource reader;
        reader = new crosby.binary.osmosis.OsmosisReader(input);
        reader.setSink(sinkImplementation);
        Thread readerThread = new Thread(reader);
        readerThread.start();
        while (readerThread.isAlive()) {
            try {
                readerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
