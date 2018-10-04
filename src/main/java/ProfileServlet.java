import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Path("/profile")
public class ProfileServlet {
    @Context
    private ServletContext context;

    @GET
    @Produces({MediaType.TEXT_HTML})
    public InputStream getMyList() throws FileNotFoundException {
        return context.getResourceAsStream("profile.html");
    }
}
