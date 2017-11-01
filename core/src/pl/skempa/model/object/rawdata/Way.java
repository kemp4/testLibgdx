package pl.skempa.model.object.rawdata;

import java.util.HashMap;
import java.util.List;

/**
 * Created by szymk on 10/29/2017.
 */

public class Way {
    private long id;
    private List<Node> nodes;
    private HashMap<String,String> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }


}
