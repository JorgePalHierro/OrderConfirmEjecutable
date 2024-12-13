package OracleConnection.OrderConfrimEjecutable.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleDBConnection {
	
    private static final String URL = "jdbc:oracle:thin:@10.10.13.14:1529:sfc";
    private static final String USER = "u605";
    private static final String PASSWORD = "i53sOc1n";
    
    
    /* Productiva*/
    private static final String URL_prodcutivo = "jdbc:oracle:thin:@10.10.13.14:1529:sfc";
    private static final String USER_productivo = "MACBHE";
    private static final String PASSWORD_productivo = "D3nV5Er9";
    
  
    

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static Connection getConnectionProductivo() throws SQLException {
        return DriverManager.getConnection(URL_prodcutivo, USER_productivo, PASSWORD_productivo);
    }
}
