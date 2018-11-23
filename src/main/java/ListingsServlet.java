import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
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
                                        @QueryParam("min_number_of_rooms") String min_num_of_rooms,
                                        @QueryParam("max_number_of_rooms") String max_num_of_rooms
                                        ) {


        System.out.println(city);
        System.out.println(minprice);
        System.out.println(maxprice);
        System.out.println(min_num_of_rooms);
        System.out.println(max_num_of_rooms);
        if(city.equals(("City"))){
            city = null;
        }
        if(minprice.equals("")){
            minprice = null;
        }
        if(maxprice.equals((""))){
            maxprice = null;
        }
        if(min_num_of_rooms.equals("")){
            min_num_of_rooms = null;
        }
        if(max_num_of_rooms.equals("")){
            max_num_of_rooms = null;
        }
        String sort_by = null;
        String order_by = null;
        List<Listing> listings = request.getListingsByParameters(city, minprice, maxprice, min_num_of_rooms, max_num_of_rooms, sort_by, order_by);

        Gson gson = new Gson();
        String json = gson.toJson(listings);
        return Response.ok(json).build();
    }

}