package pl.skempa.model.object.rawdata;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by szymk on 11/25/2017.
 */

public class OsmRawDataSet {
    private Map<Long,Node> nodes;
    private Map<Long,Way> ways;
    private Map<Long,Relation> relations;
    private Bound bound;
    public OsmRawDataSet(){
        nodes = new HashMap<Long, Node>();
        ways=new HashMap<Long,Way>();
        relations = new HashMap<Long,Relation>();
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
    public void setBound(Bound bound) {
        this.bound = bound;
    }
    public Bound getBound() {
        return bound;
    }
    public Node getNodeByWayNode(WayNode wayNode) {
        return nodes.get(wayNode.getNodeId());
    }
}