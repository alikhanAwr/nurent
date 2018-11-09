import com.google.gson.Gson;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.RescaleOp;
import java.io.InputStream;
import java.util.List;

@Path("/moderator")
public class ModeratorServlet {

    @Context
    private ServletContext context;

    private Request request = new Request();

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getModeratorPage() {
        return context.getResourceAsStream("moderator.html");
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginModerator(Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            request.checkNameAndPasswordForModerator(username, password);
            String token = request.generateTokenForModerator(username);
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("listings")
    @Produces({MediaType.TEXT_HTML})
    public InputStream getListingsPage() {
        return context.getResourceAsStream("moderatorListings.html");
    }

    @GET
    @Path("listings/getPendingListings")
    public Response getPendingListings(@Context HttpHeaders headers) {

        if (authenticate(headers)) {
            List<Listing> listings  = request.getListingsUnderModerationForModerator(getToken(headers));
            Gson gson = new Gson();
            String json = gson.toJson(listings);
            return Response.ok(json).build();

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    @POST
    @Path("listings/approve")
    public Response approveListing(@Context HttpHeaders headers, @QueryParam("id") String listingId) {

        if(authenticate(headers)) {
            request.approveListingForModerator(listingId, getToken(headers));
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("listings/refuse")
    public Response refuseListing(@Context HttpHeaders headers, @QueryParam("id") String listingId, @QueryParam("comment") String comment) {

        if(authenticate(headers)) {
            request.notApproveListingForModerator(listingId, getToken(headers), comment);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private boolean authenticate(HttpHeaders headers) {

        List<String> auth = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

        System.out.println(auth);

        if (auth == null || auth.size() == 0) {
            return false;
        }

        String header = auth.get(0);

        if (header == null || !header.toLowerCase().startsWith("bearer ")) {
            return false;
        }

        String token = header.substring("Bearer".length()).trim();
        return request.checkTokenForModerator(token);
    }

    @GET
    @Path("logout")
    public Response logout (@Context HttpHeaders headers) {
        if (authenticate(headers)) {
            request.deleteTokenForModerator(getToken(headers));
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private String getToken(HttpHeaders headers) {
        List<String> auth = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        return auth.get(0).substring("Bearer".length()).trim();
    }

}
