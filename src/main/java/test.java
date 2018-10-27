import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class test {
    public static <listing> void main(String [] args){
        Request request = new Request();
        List<Listing> list = request.getListingsByParameters(null,"2000",null,null,null,"by_city_name","DESC");
        System.out.println(list.size());
        System.out.println(list);
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);
        request.deleteToken("0c6c5806-6534-4521-a969-bda4644c2f91");

    }
}
