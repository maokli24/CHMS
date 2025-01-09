package bdio.chms.chms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connexion;
    public static Connection getConnection(){
        if(connexion == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/chms", "root", "");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connexion;

}


}
