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
public class LoginServlet {

    @Context
    private ServletContext context;

    private Request request = new Request();

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getLoginPage(){
        return context.getResourceAsStream("login.html");
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {

        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            request.checkNameAndPassword(username, password);

            String token = request.generateToken(username);

            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }
}