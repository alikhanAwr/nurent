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

    private List<String> list = new CopyOnWriteArrayList<String>();


    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        File f = new File(context.getRealPath("login.html"));
        return new FileInputStream(f);
    }



}
