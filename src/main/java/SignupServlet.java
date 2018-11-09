import com.google.gson.Gson;
import javafx.util.Pair;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/signup")
public class SignupServlet {

    @Context
    private ServletContext context;

    private Request request = new Request();

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getSignupPage() {
        return context.getResourceAsStream("signup.html");
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(AccountSignup account) {
        String username = account.getUsername();
        String password = account.getPassword();
        String email = account.getEmail();
        String firstName = account.getName();
        String lastName = account.getSurname();
        String phone = account.getPhone();

        //TODO: Validate the given sign up inforamtion
        try {
            request.addNewUser(username, password, firstName, lastName, phone);
            String token = request.generateToken(username);
            return Response.ok(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
