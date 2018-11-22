import org.apache.commons.dbutils.DbUtils;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

class Request {

    private static final String LogStatement = "INSERT INTO Logs(date_time, username, activity, result, additional_info)" +
            "VALUES(?,?,?,?,?);";

//    //only for minimal-viable demonstration
//    @Deprecated
//    public Pair<Boolean, String> addNewUser_name_and_password_only(String name, String password) {
//        Connector connector = new Connector();
//        String toReturn;
//        Pair<Boolean, String> ret = null;
//        try {
//
//            String query1 = "SELECT * FROM Accounts WHERE username = \"" + name + "\";";
//            Connection conn = connector.getConnection();
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(query1);
//            if (rs.next()) {
//                System.out.println("User with such name already exists");
//                toReturn = "User with such name already exists";
//                ret = new Pair<>(false, toReturn);
//            } else {
//                query1 = "INSERT INTO Accounts(username, password) " +
//                        "VALUES(\"" + name + "\",\"" + generateHash(password) + "\");";
//                st.executeUpdate(query1);
//                System.out.println("NewUser Added Successfully");
//                toReturn = "NewUser Added Successfully";
//                ret = new Pair<>(true, toReturn);
//            }
//        } catch (Exception ex) {
//            System.out.println("Exception in addNewUser: " + ex.getMessage());
//        } finally {
//            return ret;
//        }
//    }

    public String generateToken(String username) {
        String uuid = UUID.randomUUID().toString();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        PreparedStatement ps = null;
        try {
            String psquery1 = "UPDATE bitlab.Accounts SET token = ? WHERE username = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, uuid);
            ps.setString(2, username);
            ps.executeUpdate();
            return uuid;
        } catch (Exception ex) {
            System.out.println("Exception in generateToken(): " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public String generateTokenForModerator(String username) {
        String uuid = UUID.randomUUID().toString();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        PreparedStatement ps = null;
        try {
            String psquery1 = "UPDATE bitlab.Moderators SET token = ? WHERE username = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, uuid);
            ps.setString(2, username);
            ps.executeUpdate();
            return uuid;
        } catch (Exception ex) {
            System.out.println("Exception in generateToken(): " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public boolean checkToken(String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT username FROM bitlab.Accounts WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            System.out.println("Exception in checkToken() " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    public boolean checkTokenForModerator(String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT username FROM bitlab.Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception ex) {
            System.out.println("Exception in checkTokenForModerator() " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    public void deleteToken(String token_to_delete) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String psquery1 = "SELECT username FROM bitlab.Accounts WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token_to_delete);
            rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
            String psquery2 = "UPDATE bitlab.Accounts SET token = NULL WHERE token = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, token_to_delete);
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log Out");
            ps.setString(4, "Success");
            ps.setString(5, username);
        } catch (Exception ex) {
            System.out.println("Exception in deleteToken(): " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void deleteTokenForModerator(String token_to_delete) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token_to_delete);
            rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
            String psquery2 = "UPDATE Moderators SET token = NULL WHERE token = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, token_to_delete);
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log Out");
            ps.setString(4, "Success");
            ps.setString(5, username);
        } catch (Exception ex) {
            System.out.println("Exception in deleteTokenForModerator(): " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void addNewUser(String username, String password, String name, String surname, String phone) throws Exception {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT * FROM bitlab.Accounts WHERE username = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new Exception("User with such username already exists");
            }
            String psquery2 = "SELECT * FROM Accounts WHERE phone = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, phone);
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new Exception("User with such phone number already exists");
            }
            String psquery3 = "INSERT INTO Accounts(username, password, name, surname, phone) " +
                    "VALUES(?,?,?,?,?);";
            ps = conn.prepareStatement(psquery3);
            ps.setString(1, username);
            ps.setString(2, generateHash(password));
            ps.setString(3, name);
            ps.setString(4, surname);
            ps.setString(5, phone);
            ps.executeUpdate();
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Registration");
            ps.setString(4, "Success");
            ps.setString(5, username);
            ps.executeUpdate();
        } catch (Exception ex) {
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Registration");
            ps.setString(4, "Failure");
            ps.setString(5, ex.getMessage());
            ps.executeUpdate();
            System.out.println("Exception in addNewUser: " + ex.getMessage());
            throw new Exception("Exception in addNewUser:" + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void addNewModerator(String username, String password) throws Exception {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT * FROM bitlab.Moderators WHERE username = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new Exception("Moderator with such username already exists");
            }
            String psquery2 = "INSERT INTO Moderators(username, password) " +
                    "VALUES(?,?);";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, username);
            ps.setString(2, generateHash(password));
            ps.executeUpdate();
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Add Moderator");
            ps.setString(4, "Success");
            ps.setString(5, username);
            ps.executeUpdate();
        } catch (Exception ex) {
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Add Moderaor");
            ps.setString(4, "Failure");
            ps.setString(5, ex.getMessage());
            ps.executeUpdate();
            System.out.println("Exception in addNewModerator: " + ex.getMessage());
            throw new Exception("Exception in addNewModerator:" + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void addListing(String title, String city, String building, String num_of_rooms, String description, String price, String contact_info, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String psquery1 = "SELECT username FROM bitlab.Accounts WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                String psquery2 = "INSERT INTO bitlab.Listings" +
                        "(username, title, city, building, num_of_rooms, description, price, contact_info , postdate , status) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?);";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1, username);
                ps.setString(2, title);
                ps.setString(3, city);
                ps.setString(4, building);
                ps.setInt(5, Integer.parseInt(num_of_rooms));
                ps.setString(6, description);
                ps.setInt(7, Integer.parseInt(price));
                ps.setString(8, contact_info);
                ps.setString(9, getTime());
                ps.setString(10, "under moderation");
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username);
                ps.setString(3, "Add Listing");
                ps.setString(4, "Success");
                ps.setString(5, title);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            try {
                System.out.println("Exception in addListing() " + ex.getMessage());
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username);
                ps.setString(3, "Add Listing");
                ps.setString(4, "Failure");
                ps.setString(5, title);
                ps.executeUpdate();
            } catch (Exception ex2) {
                System.out.println("Exception in addListing() 2" + ex.getMessage());
            } finally {
                DbUtils.closeQuietly(rs);
                DbUtils.closeQuietly(ps);
                DbUtils.closeQuietly(conn);
            }
        }
    }

    public void checkNameAndPassword(String username, String password) throws Exception {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT password FROM bitlab.Accounts WHERE username = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, username);
            rs = ps.executeQuery();
            boolean next = rs.next();
            if (!next) {
                throw new Exception("Invalid username");
            }
            String pass = rs.getString("password");
            if (!pass.equals(generateHash(password))) {
                throw new Exception("Invalid password");
            }
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log In");
            ps.setString(4, "Success");
            ps.setString(5, username);
            ps.executeUpdate();
        } catch (Exception ex) {
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log In");
            ps.setString(4, "Failure");
            ps.setString(5, ex.getMessage());
            ps.executeUpdate();
            System.out.println("Exception in checkNameAndPassword: " + ex.getMessage());
            throw new Exception("Exception in checkNameAndPassword:" + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void checkNameAndPasswordForModerator(String username, String password) throws Exception {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String psquery1 = "SELECT password FROM bitlab.Moderators WHERE username = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, username);
            rs = ps.executeQuery();
            boolean next = rs.next();
            if (!next) {
                throw new Exception("Invalid username");
            }
            String pass = rs.getString("password");
            if (!pass.equals(generateHash(password))) {
                throw new Exception("Invalid password");
            }
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log In (Moderator)");
            ps.setString(4, "Success");
            ps.setString(5, username);
            ps.executeUpdate();
        } catch (Exception ex) {
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Log In (Moderator)");
            ps.setString(4, "Failure");
            ps.setString(5, ex.getMessage());
            ps.executeUpdate();
            System.out.println("Exception in checkNameAndPasswordForModerator: " + ex.getMessage());
            throw new Exception("Exception in checkNameAndPasswordForModerator:" + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    // possible sort_types: by_num_of_rooms , by_city_name , by_price ;
    // posiible sort_order: asc , desc (or)  ASC , DESC ;
    // if sort_order == null -> asc will be used
    public List<Listing> getListingsByParameters(String city, String minprice, String maxprice,
                                                 String min_num_of_rooms, String max_num_of_rooms,
                                                 String sort_by, String order_by) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String query1 = "SELECT * FROM Listings";
            query1 += " WHERE status = 'visible'";
            if (city != null) {
                query1 += " AND";
                query1 += " city = '" + city + "'";
            }
            if (minprice != null) {
                query1 += " AND";
                query1 += " price >= " + minprice + "";
            }
            if (maxprice != null) {
                query1 += " AND";
                query1 += " price <= " + maxprice + "";
            }
            if (min_num_of_rooms != null) {
                query1 += " AND";
                query1 += " num_of_rooms >= " + min_num_of_rooms + "";
            }
            if (max_num_of_rooms != null) {
                query1 += " AND";
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
            conn = connector.getConnection();
            Statement st = conn.createStatement();
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
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getListingsByParameters: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public List<Listing> getListingsForUser(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String psquery1 = "SELECT username FROM Accounts WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
            String psquery2 = "SELECT * FROM Listings WHERE username = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, username);
            rs = ps.executeQuery();
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "Visiting profile");
            ps.setString(4, "Success");
            ps.setString(5, username);
            ps.executeUpdate();
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getListingsForUser: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public void deleteListing(String id, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1 = "";
        String username2 = "";
        try {
            String psquery1 = "SELECT username FROM Accounts WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
            }
            String psquery2 = "SELECT username FROM Listings WHERE id = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                username2 = rs.getString("username");
            }
            if (username1.equals(username2)) {
                String psquery3 = "DELETE FROM Listings WHERE id = ?;";
                ps = conn.prepareStatement(psquery3);
                ps.setString(1, id);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Delete Listing");
                ps.setString(4, "Success");
                ps.setString(5, username1);
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Delete Listing");
                ps.setString(4, "Failure");
                ps.setString(5, "UNAUTHORIZED");
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            try {
                System.out.println("Exception in addListing() " + ex.getMessage());
                conn = connector.getConnection();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Delete Listing");
                ps.setString(4, "Failure");
                ps.setString(5, ex.getMessage());
                ps.executeUpdate();
            } catch (Exception ex2) {
                System.out.println("Exception in deleteListing() 2" + ex.getMessage());
            }
            System.out.println("Exception in deleteListing: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void hideListing(String id, String token) {
        System.out.println("1");
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1 = "";
        String username2 = "";
        try {
            String psquery1 = "SELECT username FROM Accounts WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
            }
            String psquery2 = "SELECT username FROM Listings WHERE id = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                username2 = rs.getString("username");
            }
            if (username1.equals(username2)) {
                String psquery3 = "SELECT status FROM Listings WHERE id = ?;";
                ps = conn.prepareStatement(psquery3);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String status = rs.getString("status");
                    if (status.equals("visible")) {
                        String psquery4 = "UPDATE Listings SET status = 'hidden' WHERE id = ?;";
                        ps = conn.prepareStatement(psquery4);
                        ps.setString(1, id);
                        ps.executeUpdate();
                        ps = conn.prepareStatement(LogStatement);
                        ps.setString(1, getTime());
                        ps.setString(2, username1);
                        ps.setString(3, "Hide Listing");
                        ps.setString(4, "Success");
                        ps.setString(5, username1);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception in hideListing: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void showListing(String id, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1 = "";
        String username2 = "";
        try {
            String psquery1 = "SELECT username FROM Accounts WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
            }
            String psquery2 = "SELECT username FROM Listings WHERE id = ?;";
            ps = conn.prepareStatement(psquery2);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                username2 = rs.getString("username");
            }
            if (username1.equals(username2)) {
                String psquery3 = "SELECT status FROM Listings WHERE id = ?;";
                ps = conn.prepareStatement(psquery3);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String status = rs.getString("status");
                    if (status.equals("hidden")) {
                        String psquery4 = "UPDATE Listings SET status = 'visible' WHERE id = ?;";
                        ps = conn.prepareStatement(psquery4);
                        ps.setString(1, id);
                        ps.executeUpdate();
                        ps = conn.prepareStatement(LogStatement);
                        ps.setString(1, getTime());
                        ps.setString(2, username1);
                        ps.setString(3, "Show Listing");
                        ps.setString(4, "Success");
                        ps.setString(5, username1);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception in showListing: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void deleteListingForModerator(String id, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1;
        try {
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
                String psquery2 = "DELETE FROM Listings WHERE id = ?;";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1, id);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Delete Listing (Moderator)");
                ps.setString(4, "Success");
                ps.setString(5, username1);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Exception in deleteListingForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void approveListingForModerator(String id, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1;
        try {
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
                String psquery2 = "UPDATE Listings SET status = 'visible' WHERE id = ?;";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1, id);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Approve Listing (Moderator)");
                ps.setString(4, "Success");
                ps.setString(5, username1);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Exception in approveListingForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void notApproveListingForModerator(String id, String token, String comment) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1;
        try {
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            conn = connector.getConnection();
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
                String psquery2 = "UPDATE Listings SET status = 'not approved', comment = ?  WHERE id = ?;";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1,comment);
                ps.setString(2, id);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Disapprove Listing (Moderator)");
                ps.setString(4, "Success");
                ps.setString(5, username1);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Exception in NotApproveListingForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public List<Account> getAccountsForModerator(String token) {
        LinkedList<Account> toReturn = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = connector.getConnection();
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                String psquery2 = "SELECT * FROM Accounts ORDER BY username ASC;";
                ps = conn.prepareStatement(psquery2);
                rs = ps.executeQuery();
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
        } catch (Exception ex) {
            System.out.println("Exception in getAccountsForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }

    public List<Listing> getListingsUnderModerationForModerator(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            conn = connector.getConnection();
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
            String psquery2 = "SELECT * FROM Listings WHERE status = 'under moderation';";
            ps = conn.prepareStatement(psquery2);
            rs = ps.executeQuery();
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "get not approved listings (moderator)");
            ps.setString(4, "Success");
            ps.setString(4, "-");
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getListingsForUser: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return list;
    }

    public List<Listing> getListingsUnderModeration(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            conn = connector.getConnection();
            Statement st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE username = '" + username + "' AND status = 'under moderation';";
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "get listings under moderation");
            ps.setString(4, "Success");
            ps.setString(5, "-");
            ps.executeUpdate();
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getListingsUnderModeration: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return list;
    }

    public List<Listing> getVisibleListings(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            conn = connector.getConnection();
            Statement st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE username = '" + username + "' AND status = 'visible';";
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "get visible listings");
            ps.setString(4, "Success");
            ps.setString(5, "-");
            ps.executeUpdate();
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getVisibleListings: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return list;
    }

    public List<Listing> getHiddenListings(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            conn = connector.getConnection();
            Statement st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE username = '" + username + "' AND status = 'hidden';";
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "get hidden listings");
            ps.setString(4, "Success");
            ps.setString(5, "-");
            ps.executeUpdate();
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getHiddenListings: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return list;
    }

    public List<Listing> getNotApprovedListings(String token) {
        LinkedList<Listing> list = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username = "";
        try {
            String query1 = "SELECT username FROM Accounts WHERE token = '" + token + "';";
            conn = connector.getConnection();
            Statement st = conn.createStatement();
            rs = st.executeQuery(query1);
            if (rs.next()) {
                username = rs.getString("username");
            }
            query1 = "SELECT * FROM Listings WHERE username = '" + username + "' AND status = 'not approved';";
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
            ps = conn.prepareStatement(LogStatement);
            ps.setString(1, getTime());
            ps.setString(2, username);
            ps.setString(3, "get not approved listings");
            ps.setString(4, "Success");
            ps.setString(5, "-");
            ps.executeUpdate();
            return list;
        } catch (Exception ex) {
            System.out.println("Exception in getNotApprovedListings: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return list;
    }

    public void banAccountForModerator(String username, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1;
        try {
            conn = connector.getConnection();
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
                String psquery2 = "UPDATE bitlab.Accounts SET banned = 1 WHERE username = ?;";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1, username);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Ban Account");
                ps.setString(4, "Success");
                ps.setString(5, username);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Exception in banAccountForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public void unbanAccountForModerator(String username, String token) {
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String username1;
        try {
            conn = connector.getConnection();
            String psquery1 = "SELECT username FROM Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                username1 = rs.getString("username");
                String psquery2 = "UPDATE bitlab.Accounts SET banned = 0 WHERE username = ?;";
                ps = conn.prepareStatement(psquery2);
                ps.setString(1, username);
                ps.executeUpdate();
                ps = conn.prepareStatement(LogStatement);
                ps.setString(1, getTime());
                ps.setString(2, username1);
                ps.setString(3, "Unban Account");
                ps.setString(4, "Success");
                ps.setString(5, username);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println("Exception in banAccountForModerator: " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
    }

    public List<LogRec> getLogsByParameter(String token, boolean user, String username,
                                           boolean logins, boolean listings) {
        LinkedList<LogRec> toReturn = new LinkedList<>();
        Connector connector = new Connector();
        Connection conn = connector.getConnection();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            conn = connector.getConnection();
            String psquery1 = "SELECT username FROM bitlab.Moderators WHERE token = ?;";
            ps = conn.prepareStatement(psquery1);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (user || logins || listings) {
                    String psquery2 = "SELECT * FROM bitlab.Logs WHERE";
                    boolean or = false;
                    if (user) {
                        or = true;
                        psquery2 += " username = ?";
                    }
                    if (logins) {
                        if (or) {
                            psquery2 += " OR";
                        }
                        psquery2 += " (activity = 'Log In' OR activity = 'Log Out' " +
                                "OR activity = 'Registration' OR activity = 'Log In(Moderator)' " +
                                "OR activity = 'Log Out(Moderator)' OR activity = 'Add Moderator')";
                        or = true;
                    }
                    if (listings) {
                        if (or) {
                            psquery2 += " OR";
                        }
                        psquery2 += " (activity = 'Show Listing' OR activity = 'Hide Listing' " +
                                "OR activity = 'Delete Listing' OR activity = 'Add Listing' OR " +
                                "activity = 'Delete Listing (Moderator)' OR activity = 'Approve Listing (Moderator)' " +
                                "OR activity = 'disapprove Listing (Moderator)')";
                        or = true;
                    }
                    psquery2 += ";";
                    ps = conn.prepareStatement(psquery2);
                    if (user) {
                        ps.setString(1, username);
                    }
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        LogRec log = new LogRec(rs.getInt("id"),
                                rs.getString("date_time"),
                                rs.getString("username"),
                                rs.getString("activity"),
                                rs.getString("result"),
                                rs.getString("additional_info"));
                        toReturn.addLast(log);
                    }
                }
            }
            return toReturn;
        } catch (Exception ex) {
            System.out.println("Exception in getLogsByParameters " + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(conn);
        }
        return null;
    }


    @org.jetbrains.annotations.NotNull
    private String generateHash(String input) {
        //taken from https://dzone.com/articles/storing-passwords-java-web for now
        StringBuilder hash = new StringBuilder();
        final String SALT = "bitlabnurent";
        input = SALT + input;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};
            IntStream.range(0, hashedBytes.length).forEach(idx -> {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            });
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception in fenerateHash() " + e.getMessage());
        }
        return hash.toString();
    }

    @NotNull
    private String getTime() {
        return new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss").format(Calendar.getInstance().getTime());
    }
}
