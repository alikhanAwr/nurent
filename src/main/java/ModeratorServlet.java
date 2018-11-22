import com.google.gson.*;
import netscape.javascript.JSObject;

import javax.json.Json;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.RescaleOp;
import java.io.InputStream;
import java.util.LinkedList;
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
    @Path("logs")
    @Produces({MediaType.TEXT_HTML})
    public InputStream getLogsPage() {
        return context.getResourceAsStream("logs.html");
    }

    @GET
    @Path("logs/request")
    public Response getLogs(@Context HttpHeaders headers, @QueryParam("username") String username,
                        @QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate,
                        @QueryParam("logins") boolean logins, @QueryParam("listings") boolean listings, @QueryParam("id") String id,
                        @QueryParam("move") String move) {


//        System.out.println(username);
//        System.out.println(startDate);
//        System.out.println(endDate);
//        System.out.println(logins);
//        System.out.println(listings);


        if(authenticate(headers)) {
            return getLogsResponse(headers, id, username, move, startDate, endDate, logins, listings);

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    private String getJsonToSend(List<LogRec> logs, boolean up, boolean down) {

        Gson gson = new Gson();
        String json = gson.toJson(logs);

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(json);

        JsonObject result = new JsonObject();
        result.add("logs", jsonArray);
        result.addProperty("up", up);
        result.addProperty("down", down);

        json = gson.toJson(result);
        return json;
    }


    private Response getLogsResponse(HttpHeaders headers, String id, String username, String move, String startDate,
                                     String endDate, boolean logins, boolean listings) {
        //TODO: check whether the supplied parameters are of corretct type (id is int, logins, listings are boolean, and so on)
        int idInt = 0;
        if (id != null) {
            try {
                idInt = Integer.parseInt(id);
            } catch (Exception e) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        boolean userBool = (username != null);


        List<LogRec> logs;
        List<LogRec> toSend;
        int L = 25;
        String jsonToSend;

        //Retrieve the logs
        logs = request.getLogsByParameter(getToken(headers), userBool, username, logins, listings);
        System.out.println(logs);

        if (move == null || id == null) {

            int size = logs.size();
            if (size > L) {
                toSend = logs.subList(0, L);
                jsonToSend = getJsonToSend(toSend, false, true);
            } else {
                toSend = logs.subList(0, size);
                jsonToSend = getJsonToSend(toSend, false, false);
            }

        } else if (move.equals("up")){
            int index;
            boolean found = false;
            int size = logs.size();

            boolean up;
            boolean down;

            for (index = 0; index < size; index++) {
                if (logs.get(index).getId() == idInt) {
                    found = true;
                    break;
                } else if (logs.get(index).getId() < idInt ) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }

            if (found) {

                if (index < L) {

                    down = size - L > 0;

                    if (size > L) {
                        toSend = logs.subList(0, L);
                    } else {
                        toSend = logs.subList(0, size);
                    }

                    jsonToSend = getJsonToSend(toSend, false, down);

                } else {

                    int start_index = index - L;
                    toSend = logs.subList(start_index, index);

                    up = index - L > 0;
                    down = index - 1 < size;

                    jsonToSend = getJsonToSend(toSend, up, down);

                }

            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        } else if (move.equals("down")) {
            int index;
            boolean found = false;
            int size = logs.size();

            boolean up;
            boolean down;

            for (index = 0; index < size; index++) {
                if (logs.get(index).getId() == idInt) {
                    found = true;
                    break;
                } else if (logs.get(index).getId() < idInt ) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }

            if (found) {

                if (size - index - 1 < L) {

                    up = true;
                    down = false;

                    toSend = logs.subList(index + 1, size);
                    jsonToSend = getJsonToSend(toSend, true, down);

                } else {
                    up = true;
                    down = false;

                    down = size - (L + index + 1) > 0;
                    toSend = logs.subList(index + 1, index + 1 + L);
                    jsonToSend = getJsonToSend(toSend, up, down);

                }

            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.ok(jsonToSend).build();
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
