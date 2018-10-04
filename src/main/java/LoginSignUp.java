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

@Path("/login")
public class LoginSignUp {

    @Context
    private ServletContext context;

    private Request request;

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        return context.getResourceAsStream("login.html");
    }

    @POST
    @Path("signin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();




    }

    @POST
    @Path("signup")
    public String signUp() {
        return "signed up";
    }




}
