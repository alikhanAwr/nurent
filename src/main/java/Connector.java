

import java.sql.*;
import com.mysql.jdbc.Driver;
class Connector {

    public Connection getConnection(){
            Connection conn = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://10.10.3.14:3306/bitlab", "bitlab", "password");
            } catch(Exception ex){
                System.out.println("Exception in Connector: "+ex.getMessage());
            }finally{
                return conn;
            }
    }
}
