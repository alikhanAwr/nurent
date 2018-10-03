

import java.sql.*;
import com.mysql.jdbc.Driver;
class connector{

    public Connection getConnection(){
            Connection conn = null;
            try {

                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nurent", "root", "password123");
                System.out.println("Database is connected !");
                if (conn == null){
                    System.out.println("null2");
                }

            } catch(Exception ex){
                System.out.println("Exception in Connector: "+ex.getMessage());
            }finally{
                return conn;
            }
    }
}
