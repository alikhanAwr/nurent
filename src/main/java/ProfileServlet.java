import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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


    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyProfile() throws FileNotFoundException {
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

    @POST
    @Path("hidelisting")
    public Response hideListing(@Context HttpHeaders headers, @QueryParam("id") String listingId) {
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
            request.hideListing(listingId, token);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("create")
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        System.out.println("GET");
        return context.getResourceAsStream("addListing.html");
    }


    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addListing(@Context HttpHeaders headers, ListingPost listing) {
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
            request.addListing(listing.getTitle(), listing.getCity(), listing.getBuilding(), listing.getNum_of_rooms(),
                    listing.getDescription(), listing.getNum_of_rooms(), listing.getContact_info(), token);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

//        System.out.println(listing.getBuilding());
//        System.out.println("POST");
//        return Response.ok().build();
    }

    @GET
    @Path("logout")
    public Response logout (@Context HttpHeaders headers, ListingPost listing) {
        List<String> auth = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("logout");
        System.out.println(auth);

        if (auth == null || auth.size() == 0) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!isCorrectAuthHeader(auth.get(0))) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = auth.get(0).substring("Bearer".length()).trim();

        request.deleteToken(token);
        return Response.ok().build();

    }


    private boolean isCorrectAuthHeader(String header) {
        return header != null && header.toLowerCase().startsWith("bearer" + " ");
    }
}