package pl.skempa.object;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import pl.skempa.Building;
import pl.skempa.XmlUtil;
import pl.skempa.XmlUtilImpl;
import pl.skempa.model.DegreePosition;

/**
 * Created by Mymon on 2017-10-16.
 */

public class OpenStreetMapAPIWrapper implements ObjectsDataAPIWrapper {
    private static final String boundingBoxMapURI = "http://api.openstreetmap.org/api/0.6/map";


    @Override
    public List<Building> getObjects(DegreePosition position) {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(boundingBoxMapURI);
            ClientResponse response = webResource.queryParam("bbox","18.0,50.0,18.01,50.01")
                    .get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            InputStream inputStream = response.getEntityInputStream();
            XmlUtil xmlUtil= new XmlUtilImpl();
        try {
            return xmlUtil.readXml(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("response reading faild");
        }
    }
}
