

import javafx.util.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

class Request {


    public Pair<Boolean,String> addNewUser_name_and_password_only(String name, String password){
        connector cnnt = new connector();
        String toReturn = null;
        Pair<Boolean,String> ret = null;
        try {

            String query1 = "SELECT * FROM Accounts WHERE name = \"" + name +"\";";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if(rs.next()){
                System.out.println("User with such name already exists");
                toReturn = "User with such name already exists";
                ret = new Pair<>(false,toReturn);
            } else {
                query1 = "INSERT INTO Accounts(name, password) " +
                        "VALUES(\""+name+"\",\""+generateHash(password)+"\");";
                st.executeUpdate(query1);
                System.out.println("NewUser Added Successfully");
                toReturn = "NewUser Added Successfully";
                ret = new Pair<>(true,toReturn);
            }
        } catch (Exception ex) {
            System.out.println("Exception in addNewUser: "+ex.getMessage());
        } finally {
            return ret;
        }
    }
    //only for minimal-viable demonstration

    public String addNewUser(String name, String password, String country, String city, String email, String phone, String info) {

        connector cnnt = new connector();
        String toReturn = null;
        try {

            String query1 = "SELECT * FROM Accounts WHERE name = \"" + name +"\";";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if(rs.next()){
                System.out.println("User with such name already exists");
                toReturn = "User with such name already exists";
                return "User with such name already exists";
            }
            query1 = "SELECT * FROM Accounts WHERE name = \"" + email +"\";";
            rs = st.executeQuery(query1);
            if(rs.next()){
                System.out.println("User with such email already exists");
                toReturn = "User with such email already exists";
                return "User with such email already exists";
            }
            query1 = "SELECT * FROM Accounts WHERE phone = \"" + phone +"\";";
            rs = st.executeQuery(query1);
            if(rs.next()){
                System.out.println("User with such phone number already exists");
                toReturn = "User with such phone number already exists";
                return "User with such phone number already exists";
            }
            query1 = "INSERT INTO Accounts(name, password, country, city, name, phone, info) " +
                    "VALUES(\""+name+"\",\""+generateHash(password)+"\",\""+country+"\",\""+city+"\",\""+email+"\",\""+phone+"\",\""+info+"\");";
            st.executeUpdate(query1);
            System.out.println("NewUser Added Successfully");
            toReturn = "NewUser Added Successfully";
            return "NewUser Added Successfully";
        } catch (Exception ex) {
            System.out.println("Exception in addNewUser: "+ex.getMessage());
        } finally {
            return toReturn;
        }
    }

    public Pair<Boolean,String> checkNameAndPassword(String name, String password){
        String toReturn = null;
        connector cnnt = new connector();
        Connection conn = cnnt.getConnection();
        Pair<Boolean,String> ret = null;
        try {
            String query2 = "SELECT password FROM Accounts WHERE name = \""+name+"\";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query2);
            boolean next = rs.next();
            if(!next){
                ret = new Pair<>(false,"Invalid name");
            }
            if(next){
                String pass = rs.getString("password");
                if(pass.equals(generateHash(password))){
                    ret = new Pair<>(true,"Correct name and password");
                } else {
                    ret = new Pair<>(false,"Invalid password");
                }
            }

        } catch (Exception ex) {
            System.out.println("Exception in checkNameAndPassword: "+ex.getMessage());
        } finally {
            return ret;
        }
    }

    public List<Listing> getAllListings(){
        System.out.println("2");
        List<Listing> list = new LinkedList();
        connector cnnt = new connector();
        try {
            String query1 = "SELECT * FROM Listings;";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while(rs.next()) {
               Listing listing = new Listing(rs.getString("email"),
                                            rs.getString("title"),
                                            rs.getString("city"),
                                            rs.getString("building"),
                                            rs.getInt("num_of_rooms"),
                                            rs.getString("description"),
                                            rs.getInt("price"),
                                            rs.getString("postdate"),
                                            rs.getString("contact_info"));
               ((LinkedList<Listing>) list).addLast(listing);
            }
        } catch (Exception ex) {
            System.out.println("Exception in getAllListings: "+ex.getMessage());
        } finally {
            return list;
        }
    }

    //possible sort_types: by_num_of_rooms , by_city_name , by_price ; posiible sort_order: asc , desc (or)  ASC , DESC ; if sort_order == null -> asc will be used
    public List<Listing> getListingsByParameters(String city, String minprice, String maxprice, String min_num_of_rooms, String max_num_of_rooms, String sort_by, String order_by){
        System.out.println("3");
        boolean and = false;
        List<Listing> list = new LinkedList();
        connector cnnt = new connector();
        try {
            String query1 = "SELECT * FROM Listings";
            if(city!=null||minprice!=null||maxprice!=null||min_num_of_rooms!=null||max_num_of_rooms!=null){
                query1 += " WHERE";
            }
            if(city!=null){
                and = true;
                query1 += " city = '"+city+"'";
            }
            if(minprice!=null){
                if(and){
                    query1 += " AND";
                }
                and = true;
                query1 += " price >= "+minprice+"";
            }
            if(maxprice!=null){
                if(and){
                    query1 += " AND";
                }
                and = true;
                query1 += " price <= "+maxprice+"";
            }
            if(min_num_of_rooms!=null){
                if(and){
                    query1 += " AND";
                }
                and = true;
                query1 += " num_of_rooms >= "+min_num_of_rooms+"";
            }
            if(max_num_of_rooms!=null){
                if(and){
                    query1 += " AND";
                }
                and = true;
                query1 += " num_of_rooms <= "+max_num_of_rooms+"";
            }
            if(sort_by!=null){
                if(sort_by.equals("by_num_of_rooms")){
                    if(order_by == null || order_by.equals("asc") || order_by.equals("ASC")){
                        query1 += " ORDER BY num_of_rooms ASC";
                    }else if(order_by.equals("desc") || order_by.equals("DESC")){
                        query1 += " ORDER BY num_of_rooms DESC";
                    }
                }
                if(sort_by.equals("by_city_name")){
                    if(order_by == null || order_by.equals("asc") || order_by.equals("ASC")){
                        query1 += " ORDER BY city ASC";
                    }else if(order_by.equals("desc") || order_by.equals("DESC")){
                        query1 += " ORDER BY city DESC";
                    }
                }
                if(sort_by.equals("by_price")) {
                    if (order_by == null || order_by.equals("asc") || order_by.equals("ASC")) {
                        query1 += " ORDER BY price ASC";
                    } else if (order_by.equals("desc") || order_by.equals("DESC")) {
                        query1 += " ORDER BY price DESC";
                    }
                }
            }

            query1 += ";";
            System.out.println(query1);
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while(rs.next()) {
                Listing listing = new Listing(rs.getString("email"),
                        rs.getString("title"),
                        rs.getString("city"),
                        rs.getString("building"),
                        rs.getInt("num_of_rooms"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getString("postdate"),
                        rs.getString("contact_info"));
                ((LinkedList<Listing>) list).addLast(listing);
            }
        } catch (Exception ex) {
            System.out.println("Exception in getAllListings: "+ex.getMessage());
        } finally {
            return list;
        }
    }

    public static String generateHash(String input) {
        //taken from https://dzone.com/articles/storing-passwords-java-web for now
        StringBuilder hash = new StringBuilder();
        final String SALT = "bitlabnurent";
        input = SALT + input;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

}
