import javafx.util.Pair;

import java.util.List;

public class test {
    public static <listing> void main(String [] args){
        Request request = new Request();
       // String s = request.addNewUser_name_and_password_only("Jony112", "Jony112");
        Pair<Boolean,String> y = request.checkNameAndPassword("Jony113","Jony113");
        System.out.println(y);

    }
}
