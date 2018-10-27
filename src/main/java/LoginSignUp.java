import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

@Path("/login")
public class LoginSignUp {

    @Context
    private ServletContext context;

    private Request request = new Request();

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        return context.getResourceAsStream("login.html");
    }


    @POST
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();

//        Pair<Boolean, String> result = request.checkNameAndPassword(email, password);
////        Pair<Boolean, String> result = new Pair<>(false, "Incorrect name or password");
//        LoginResponse r = new LoginResponse(result.getKey(), result.getValue());
//        String token = issueToken(email);
//
//        return Response.ok(token).build();
//
//
//        Gson gson = new Gson();
//        String json = gson.toJson(r, LoginResponse.class);
//        System.out.println(json);
//        if (!result.getKey()) {
//            return Response.status(Response.Status.UNAUTHORIZED).entity(json).build();
//        } else {
//            return Response.ok(json).build();
//        }

        try {

            request.checkNameAndPassword(email, password);

            String token = request.generateToken(email);

            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }



    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(Credentials credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();

        Pair<Boolean, String> result = request.addNewUser_name_and_password_only(email, password);
//        Pair<Boolean, String> result = new Pair<>(true, "NewUser added Successfully");
        LoginResponse r = new LoginResponse(result.getKey(), result.getValue());
        Gson gson = new Gson();
        String json = gson.toJson(r, LoginResponse.class);
        System.out.println(json);
        if (!result.getKey()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(json).build();
        } else {
            return Response.ok(json).build();
        }
    }

}