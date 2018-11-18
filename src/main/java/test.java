import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class test {
    public static <listing> void main(String [] args){
        Request request = new Request();
        try {
            request.addNewModerator("moderator4","moderator4");
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
