import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/listings")
public class ListingsServlet {
    @Context
    private ServletContext context;

    private Request request = new Request();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMyList() {
        System.out.println("l1");
        List<Listing> listings = request.getAllListings();
        System.out.println("l2");
        Gson gson = new Gson();
        System.out.println("l3");
        String json = gson.toJson(listings);
        System.out.println("l4");
        System.out.println(json);
        return Response.ok(json).build();
    }
}
