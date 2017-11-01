package pl.skempa.model.apiwrappers;


import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.math.Vector3;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import pl.skempa.model.object.rawdata.RawDataSet;
import pl.skempa.util.DegreeUtil;
import pl.skempa.util.XmlUtil;
import pl.skempa.util.XmlUtilBySax;

/**
 * Created by Mymon on 2017-10-16.
 */

public class OpenStreetMapAPIWrapper implements ObjectsDataAPIWrapper {
    private static final String boundingBoxMapURI = "http://api.openstreetmap.org/api/0.6/map";


    @Override
    public RawDataSet getObjects(Vector3 position) throws ApiWrapperException {
            long startTime = System.currentTimeMillis();
            Client client = Client.create();
            WebResource webResource = client
                    .resource(boundingBoxMapURI);
            Vector3 maxPosition = new Vector3(position);
            String params = DegreeUtil.asApiBBoxParam(position,maxPosition.add(0.004f,0.004f,0.0f));
            ClientResponse response = webResource.queryParam("bbox",params)
                    .get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new ApiWrapperException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            InputStream inputStream = response.getEntityInputStream();
            long miedzyczas = System.currentTimeMillis()-startTime;
            System.out.println("calling time" + miedzyczas);
            XmlUtil xmlUtil= new XmlUtilBySax();

            try {
              return xmlUtil.readXml(inputStream);
            } catch (IOException e) {
               e.printStackTrace();
              throw new RuntimeException("response reading faild");
            }finally {
                System.out.println("total parsing and calling time "+(System.currentTimeMillis()-startTime));
            }
    }
}
