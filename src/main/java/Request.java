import javafx.util.Pair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

class Request {

    //only for minimal-viable demonstration
    @Deprecated
    public Pair<Boolean, String> addNewUser_name_and_password_only(String name, String password) {
        connector cnnt = new connector();
        String toReturn = null;
        Pair<Boolean, String> ret = null;
        try {

            String query1 = "SELECT * FROM Accounts WHERE username = \"" + name + "\";";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                System.out.println("User with such name already exists");
                toReturn = "User with such name already exists";
                ret = new Pair<>(false, toReturn);
            } else {
                query1 = "INSERT INTO Accounts(username, password) " +
                        "VALUES(\"" + name + "\",\"" + generateHash(password) + "\");";
                st.executeUpdate(query1);
                System.out.println("NewUser Added Successfully");
                toReturn = "NewUser Added Successfully";
                ret = new Pair<>(true, toReturn);
            }
        } catch (Exception ex) {
            System.out.println("Exception in addNewUser: " + ex.getMessage());
        } finally {
            return ret;
        }
    }

    public String generateToken(String username) {
        String uuid = UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        connector cnnt = new connector();
        String toReturn = null;
        try {
            String query1 = "UPDATE Accounts SET token = '" + uuid + "' WHERE username = '" + username + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            st.executeUpdate(query1);
        } catch (Exception ex) {
            System.out.println("Exception in generateToken(): " + ex.getMessage());
        } finally {
            return uuid;
        }
    }

    public String generateTokenForModerator(String username) {
        String uuid = UUID.randomUUID().toString();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        connector cnnt = new connector();
        String toReturn = null;
        try {
            String query1 = "UPDATE Moderators SET token = '" + uuid + "' WHERE username = '" + username + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            st.executeUpdate(query1);
        } catch (Exception ex) {
            System.out.println("Exception in generateToken(): " + ex.getMessage());
        } finally {
            return uuid;
        }
    }

    public boolean checkToken(String token) {
        connector cnnt = new connector();
        boolean toReturn = false;
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                toReturn = true;
                return true;
            } else {
                toReturn = false;
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Exception in checkToken() " + ex.getMessage());
        } finally {
            return toReturn;
        }
    }

    public boolean checkTokenForModerator(String token) {
        connector cnnt = new connector();
        boolean toReturn = false;
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                toReturn = true;
                return true;
            } else {
                toReturn = false;
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Exception in checkTokenForModerator() " + ex.getMessage());
        } finally {
            return toReturn;
        }
    }

    public void deleteToken(String token_to_delete) {
        connector cnnt = new connector();
        String toReturn = null;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        String username = "";
        try {
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            String query0 = "SELECT username FROM Accounts WHERE token = '" + token_to_delete + "';";
            ResultSet rs = st.executeQuery(query0);
            if (rs.next()) {
                username = rs.getString("username");
            }
            String query1 = "UPDATE Accounts SET token = NULL WHERE token = '" + token_to_delete + "';";
            st.executeUpdate(query1);
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + username + "','Log Out','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in deleteToken(): " + ex.getMessage());
        }
    }

    public void deleteTokenForModerator(String token_to_delete) {
        connector cnnt = new connector();
        String toReturn = null;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        String username = "";
        try {
            String query1 = "UPDATE Moderators SET token = NULL WHERE token = '" + token_to_delete + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            st.executeUpdate(query1);
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + username + "','Log Out(Moderator)','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in deleteTokenForModerator(): " + ex.getMessage());
        }
    }

    public void addNewUser(String username, String password, String name, String surname, String phone) throws Exception {
        connector cnnt = new connector();
        String toReturn = null;
        try {
            String query1 = "SELECT * FROM Accounts WHERE username = \"" + username + "\";";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                throw new Exception("User with such username already exists");
            }
            query1 = "SELECT * FROM Accounts WHERE phone = \"" + phone + "\";";
            rs = st.executeQuery(query1);
            if (rs.next()) {
                throw new Exception("User with such phone number already exists");
            }

            query1 = "INSERT INTO Accounts(username, password, name, surname, phone) " +
                    "VALUES(\"" + username + "\",\"" + generateHash(password) + "\",\"" + name + "\",\"" + surname + "\",\"" + phone + "\");";
            st.executeUpdate(query1);
        } catch (Exception ex) {
            System.out.println("Exception in addNewUser: " + ex.getMessage());
            throw new Exception("Exception in addNewUser:" + ex.getMessage());
        }
    }

    public void addNewModerator(String username, String password) throws Exception {
        connector cnnt = new connector();
        String toReturn = null;
        try {
            String query1 = "SELECT * FROM Moderators WHERE username = \"" + username + "\";";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                throw new Exception("Moderator with such username already exists");
            }
            query1 = "INSERT INTO Moderators(username, password) " +
                    "VALUES(\"" + username + "\",\"" + generateHash(password) + "\");";
            st.executeUpdate(query1);
        } catch (Exception ex) {
            System.out.println("Exception in addNewModerator: " + ex.getMessage());
            throw new Exception("Exception in addNewModerator:" + ex.getMessage());
        }
    }

    public void addListing(String title, String city, String building, String num_of_rooms, String description, String price, String contact_info, String token) {
        connector cnnt = new connector();
        String toReturn = null;
        String username = "";
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            System.out.println(query1);
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
                query1 = "INSERT INTO Listings(username, title, city, building, num_of_rooms, description, price, contact_info , postdate , status) " +
                        "VALUES('" + username + "','" + title + "','" + city + "','" + building + "'," + num_of_rooms + ",'" + description + "'," + price + ",'" + contact_info + "','" + timeStamp + "','under moderation');";
                System.out.println(query1);
                st.executeUpdate(query1);
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','" + username + "','Add Listing','Success','" + title + "');";
                st.executeUpdate(query2);
            }
        } catch (Exception ex) {
            try {
                System.out.println("Exception in addListing() " + ex.getMessage());
                Connection conn = cnnt.getConnection();
                Statement st = conn.createStatement();
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','" + username + "','Add Listing','Failure','" + ex.getMessage() + "');";
                st.executeUpdate(query2);
            } catch (Exception ex2) {
                System.out.println("Exception in addListing() 2" + ex.getMessage());
            }
        }
    }

    public void checkNameAndPassword(String name, String password) throws Exception {
        String toReturn = null;
        connector cnnt = new connector();
        Connection conn = cnnt.getConnection();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        try {
            String query1 = "SELECT password FROM Accounts WHERE username = \"" + name + "\";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            boolean next = rs.next();
            if (!next) {
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','not logged in: " + name + "','Log In','Failure','Invalid username');";
                st.executeUpdate(query2);
                throw new Exception("Invalid username");
            }
            String pass = rs.getString("password");
            if (!pass.equals(generateHash(password))) {
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','not logged in: " + name + " ','Log In','Failure','Invalid password');";
                st.executeUpdate(query2);
                throw new Exception("Invalid password");
            }
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + name + "','Log In','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in checkNameAndPassword: " + ex.getMessage());
            throw new Exception("Exception in checkNameAndPassword:" + ex.getMessage());
        }
    }

    public void checkNameAndPasswordForModerator(String name, String password) throws Exception {
        String toReturn = null;
        connector cnnt = new connector();
        Connection conn = cnnt.getConnection();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        try {
            String query1 = "SELECT password FROM Moderators WHERE username = \"" + name + "\";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            boolean next = rs.next();
            if (!next) {
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','not logged in: " + name + "','Log In(Moderator)','Failure','Invalid username');";
                st.executeUpdate(query2);
                throw new Exception("Invalid username");
            }
            String pass = rs.getString("password");
            if (!pass.equals(generateHash(password))) {
                String query2 = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
                        "VALUES('" + timeStamp + "','not logged in: " + name + " ','Log In(Moderator)','Failure','Invalid password');";
                st.executeUpdate(query2);
                throw new Exception("Invalid password");
            }
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + name + "','Log In(Moderator)','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in checkNameAndPasswordForModerator: " + ex.getMessage());
            throw new Exception("Exception in checkNameAndPasswordForModerator:" + ex.getMessage());
        }
    }

    //old method for sprint 1
    @Deprecated
    public List<Listing> getAllListings() {
        List<Listing> list = new LinkedList();
        connector cnnt = new connector();
        try {
            String query1 = "SELECT * FROM Listings;";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                Listing listing = new Listing(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("city"),
                        rs.getString("building"),
                        rs.getInt("num_of_rooms"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getString("postdate"),
                        rs.getString("contact_info"),
                        rs.getString("status"),
                        rs.getString("comment"));
                ((LinkedList<Listing>) list).addLast(listing);
            }
        } catch (Exception ex) {
            System.out.println("Exception in getAllListings: " + ex.getMessage());
        } finally {
            return list;
        }
    }

    // possible sort_types: by_num_of_rooms , by_city_name , by_price ;
    // posiible sort_order: asc , desc (or)  ASC , DESC ;
    // if sort_order == null -> asc will be used
    public List<Listing> getListingsByParameters(String city, String minprice, String maxprice,
                                                 String min_num_of_rooms, String max_num_of_rooms,
                                                 String sort_by, String order_by) {
        boolean and = false;
        List<Listing> list = new LinkedList();
        connector cnnt = new connector();
        try {
            String query1 = "SELECT * FROM Listings";
            query1 += " WHERE status = 'visible'";
            and = true;
            if (city != null) {
                if (and) {
                    query1 += " AND";
                }
                and = true;
                query1 += " city = '" + city + "'";
            }
            if (minprice != null) {
                if (and) {
                    query1 += " AND";
                }
                and = true;
                query1 += " price >= " + minprice + "";
            }
            if (maxprice != null) {
                if (and) {
                    query1 += " AND";
                }
                and = true;
                query1 += " price <= " + maxprice + "";
            }
            if (min_num_of_rooms != null) {
                if (and) {
                    query1 += " AND";
                }
                and = true;
                query1 += " num_of_rooms >= " + min_num_of_rooms + "";
            }
            if (max_num_of_rooms != null) {
                if (and) {
                    query1 += " AND";
                }
                and = true;
                query1 += " num_of_rooms <= " + max_num_of_rooms + "";
            }
            if (sort_by != null) {
                if (sort_by.equals("by_num_of_rooms")) {
                    if (order_by == null || order_by.equals("asc") || order_by.equals("ASC")) {
                        query1 += " ORDER BY num_of_rooms ASC";
                    } else if (order_by.equals("desc") || order_by.equals("DESC")) {
                        query1 += " ORDER BY num_of_rooms DESC";
                    }
                }
                if (sort_by.equals("by_city_name")) {
                    if (order_by == null || order_by.equals("asc") || order_by.equals("ASC")) {
                        query1 += " ORDER BY city ASC";
                    } else if (order_by.equals("desc") || order_by.equals("DESC")) {
                        query1 += " ORDER BY city DESC";
                    }
                }
                if (sort_by.equals("by_price")) {
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
            while (rs.next()) {
                Listing listing = new Listing(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("city"),
                        rs.getString("building"),
                        rs.getInt("num_of_rooms"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getString("postdate"),
                        rs.getString("contact_info"),
                        rs.getString("status"),
                        rs.getString("comment"));
                ((LinkedList<Listing>) list).addLast(listing);
            }
        } catch (Exception ex) {
            System.out.println("Exception in getListingsByParameters: " + ex.getMessage());
        } finally {
            return list;
        }
    }

    public List<Listing> getListingsForUser(String token) {
        LinkedList<Listing> list = new LinkedList();
        connector cnnt = new connector();
        String username = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE username = '" + username + "';";
            rs = st.executeQuery(query1);
            while (rs.next()) {
                Listing listing = new Listing(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("city"),
                        rs.getString("building"),
                        rs.getInt("num_of_rooms"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getString("postdate"),
                        rs.getString("contact_info"),
                        rs.getString("status"),
                        rs.getString("comment"));
                list.addLast(listing);
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + username + "','Visiting Profile','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in getListingsForUser: " + ex.getMessage());
        } finally {
            return list;
        }
    }

    public void deleteListing(String id, String token) {
        connector cnnt = new connector();
        String username1 = "";
        String username2 = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
            }
            query1 = "SELECT username FROM Listings WHERE id = " + id + ";";
            conn = cnnt.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username2 = rs.getString("username");
            }
            if (username1.equals(username2)) {
                query1 = "DELETE FROM Listings WHERE id = " + id + ";";
                st.executeUpdate(query1);
            }
        } catch (Exception ex) {
            System.out.println("Exception in deleteListing: " + ex.getMessage());
        }
    }

    public void hideListing(String id, String token) {
        connector cnnt = new connector();
        String username1 = "";
        String username2 = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
            }
            query1 = "SELECT username FROM Listings WHERE id = " + id + ";";
            conn = cnnt.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username2 = rs.getString("username");
            }
            if (username1.equals(username2)) {
                query1 = "SELECT status FROM Listings WHERE id = " + id + ";";
                conn = cnnt.getConnection();
                st = conn.createStatement();
                rs = st.executeQuery(query1);
                if(rs.next()){
                    String status = rs.getString("status");
                    if(status.equals("visible")){
                        query1 = "UPDATE Listings SET status = 'hidden' WHERE id = " + id + ";";
                        st.executeUpdate(query1);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception in hideListing: " + ex.getMessage());
        }
    }

    public void deleteListingForModerator(String id, String token) {
        connector cnnt = new connector();
        String username1 = "";
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
                query1 = "DELETE FROM Listings WHERE id = " + id + ";";
                st.executeUpdate(query1);
            }
        } catch (Exception ex) {
            System.out.println("Exception in deleteListingForModerator: " + ex.getMessage());
        }
    }

    public void approveListingForModerator(String id, String token) {
        connector cnnt = new connector();
        String username1 = "";
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
                query1 = "UPDATE Listings SET status = 'visible' WHERE id = " + id + ";";
                st.executeUpdate(query1);
            }
        } catch (Exception ex) {
            System.out.println("Exception in approveListingForModerator: " + ex.getMessage());
        }
    }

    public void notApproveListingForModerator(String id, String token, String comment) {
        connector cnnt = new connector();
        String username1 = "";
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
                query1 = "UPDATE Listings SET status = 'not approved', comment = '" + comment + "' WHERE id = " + id + ";";
                st.executeUpdate(query1);
            }
        } catch (Exception ex) {
            System.out.println("Exception in NotApproveListingForModerator: " + ex.getMessage());
        }
    }

    public List<Account> getAccountsForModerator(String token) {
        LinkedList<Account> toReturn = new LinkedList<>();
        connector cnnt = new connector();
        String username1 = "";
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username1 = rs.getString("username");
                query1 = "SELECT * FROM Accounts ORDER BY username ASC;";
                st.executeQuery(query1);
                ResultSet rs1 = st.executeQuery(query1);
                while (rs.next()) {
                    Account acc = new Account(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                    toReturn.addLast(acc);
                }
                return toReturn;
            }
        }catch (Exception ex){
            System.out.println("Exception in getAccountsForModerator: " + ex.getMessage());
        }finally{
            return toReturn;
        }
    }

    public List<Listing> getListingsUnderModerationForModerator(String token){
        LinkedList<Listing> list = new LinkedList();
        connector cnnt = new connector();
        String username = "";
        try {
            String query1 = "SELECT username FROM Moderators WHERE token = '" + token + "';";
            Connection conn = cnnt.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE status = 'under moderation';";
            rs = st.executeQuery(query1);
            while (rs.next()) {
                Listing listing = new Listing(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("city"),
                        rs.getString("building"),
                        rs.getInt("num_of_rooms"),
                        rs.getString("description"),
                        rs.getInt("price"),
                        rs.getString("postdate"),
                        rs.getString("contact_info"),
                        rs.getString("status"),
                        rs.getString("comment"));
                list.addLast(listing);
            }
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
            String query2 = "INSERT INTO Logs(date_time, username, activity, result)" +
                    "VALUES('" + timeStamp + "','" + username + "','Visiting Profile','Success');";
            st.executeUpdate(query2);
        } catch (Exception ex) {
            System.out.println("Exception in getListingsForUser: " + ex.getMessage());
        } finally {
            return list;
        }
    }




    private static String generateHash(String input) {
        //taken from https://dzone.com/articles/storing-passwords-java-web for now
        StringBuilder hash = new StringBuilder();
        final String SALT = "bitlabnurent";
        input = SALT + input;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
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
