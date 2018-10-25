import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Response getListByParameters(@QueryParam("city") String city,
                                        @QueryParam("minprice") String minprice,
                                        @QueryParam("maxprice") String maxprice,
                                        @QueryParam("min_num_of_rooms") String min_num_of_rooms,
                                        @QueryParam("max_num_of_rooms") String max_num_of_rooms,
                                        @QueryParam("sort_by") String sort_by,
                                        @QueryParam("order_by") String order_by) {
        System.out.println("l1");
        List<Listing> listings = request.getListingsByParameters(city, minprice, maxprice, min_num_of_rooms, max_num_of_rooms, sort_by, order_by);
        System.out.println("l2");
        Gson gson = new Gson();
        System.out.println("l3");
        String json = gson.toJson(listings);
        System.out.println("l4");
        System.out.println(json);
        return Response.ok(json).build();
    }

}