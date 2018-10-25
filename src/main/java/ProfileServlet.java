import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@Path("/profile")
public class ProfileServlet {
    @Context
    private ServletContext context;

    private Request request = new Request();

//    List<Listing> listings = new LinkedList<Listing>(
//            Arrays.asList(new Listing(1, "a@a.com", "title", "Astana", "fas", 3, "dfafd", 231, "fasdfsad", "fsdfsad"),
//                    new Listing(2, "a@a.com", "title", "Astana", "fas", 3, "dfafd", 231, "fasdfsad", "fsdfsad"),
//                    new Listing(3, "a@a.com", "title", "Astana", "fas", 3, "dfafd", 231, "fasdfsad", "fsdfsad"))
//    );


    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        return context.getResourceAsStream("profile.html");
    }

    @GET
    @Path("getlistings")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getListings(@Context HttpHeaders headers) {

        List<String> auth = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

        System.out.println(auth);

        if (auth == null || auth.size() == 0) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!isCorrectAuthHeader(auth.get(0))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = auth.get(0).substring("Bearer".length()).trim();

        if (request.checkToken(token)) {
            List<Listing> listings = request.getListingsForUser(token);
            Gson gson = new Gson();
            String json = gson.toJson(listings);
            return Response.ok(json).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
    @Path("deletelisting")
    public Response deleteListing(@Context HttpHeaders headers, @QueryParam("id") String listingId) {
        List<String> auth = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

        System.out.println(auth);

        if (auth == null || auth.size() == 0) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!isCorrectAuthHeader(auth.get(0))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = auth.get(0).substring("Bearer".length()).trim();

        if (request.checkToken(token)) {
            request.deleteListing(listingId, token);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    private boolean isCorrectAuthHeader(String header) {
        return header != null && header.toLowerCase().startsWith("bearer" + " ");
    }
}
