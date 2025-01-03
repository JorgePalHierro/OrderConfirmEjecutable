package OracleConnection.OrderConfrimEjecutable.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleDBConnection {
	
	static {
	    try {
	        // Carga explícita del controlador JDBC de Oracle
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        System.out.println("Driver de Oracle cargado exitosamente.");
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException("No se encontró el driver de Oracle", e);
	    }
	}


    private static Properties properties = new Properties();

    static {
        try (InputStream input = OracleDBConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
        	
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo database.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar el archivo database.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

  /*  public static Connection getConnectionProductivo() throws SQLException {
        String url = properties.getProperty("db.url.productivo");
        String user = properties.getProperty("db.user.productivo");
        String password = properties.getProperty("db.password.productivo");
        return DriverManager.getConnection(url, user, password);
    }*/
}
