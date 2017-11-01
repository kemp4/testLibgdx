package pl.skempa.model.object.rawdata;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mymon on 2017-11-01.
 */

public class RawDataSet {



    private Map<Long, Node> nodes;
    private Map<Long, Way> ways;
    private HashMap<Long,Relation> relations;

    public RawDataSet(Map<Long, Node> nodes, Map<Long, Way> ways) {
        this.nodes=nodes;
        this.ways=ways;
    }

    public Map<Long, Node> getNodes() {
        return nodes;
    }
    public Map<Long, Way> getWays() {
        return ways;
    }
    public Map<Long, Relation> getRelations() {
        return relations;
    }

}
