package OracleConnection.OrderConfrimEjecutable.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;

public class DataFetcher {
	
	private static final String PROPERTIES_FILE = "/application.properties";
    private static String fechaInicial;
    private static String fechaFinal;

    // Bloque estático para cargar propiedades
    static {
        try (InputStream input = DataFetcher.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input != null) {
                properties.load(input);
                fechaInicial = properties.getProperty("fechaInicial");
                fechaFinal = properties.getProperty("fechaFinal");
            } else {
                throw new IOException("Archivo de propiedades no encontrado: " + PROPERTIES_FILE);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de propiedades: " + e.getMessage());
            fechaInicial = null;
            fechaFinal = null;
        }
    }

    public List<PosHeader> fetchPasilloData() {
        List<PosHeader> results = new ArrayList<>();

        String fechaActual = obtenerFechaActual(); // Fecha actual del sistema

        // Construir la consulta dinámica según las fechas proporcionadas
        String query = construirQuery(fechaInicial, fechaFinal, fechaActual);
        
        System.out.println(query);

        try (Connection connection = OracleDBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                PosHeader posHeader = new PosHeader(
                    resultSet.getString("POS_STORE"),
                    resultSet.getString("POS_TERMINAL"),
                    resultSet.getString("POS_TRANSACTION"),
                    resultSet.getString("POS_TRANSACTION_DATE"),
                    resultSet.getString("POS_ORDER_PASILLO"),
                    resultSet.getString("POS_ASSOCIATE_NUMBER")
                );
                // Datos de pagos
                posHeader.setPosTenderType(resultSet.getString("POS_TENDER_TYPE_CODE"));
                posHeader.setPosAmountDue(resultSet.getString("POS_AMOUNT_DUE"));
                posHeader.setesquema(resultSet.getString("POS_CHARGE_OPTIONS"));
                posHeader.setautorizacion(resultSet.getString("POS_AUTHORIZATION_CODE"));
                posHeader.setcodigoRespuesta(resultSet.getString("POS_RESPONSE_CODE"));
                posHeader.setnumTarjeta(resultSet.getString("POS_ACCOUNT_NUMBER"));

                results.add(posHeader);
            }

        } catch (SQLException e) {
            // Manejo mejorado de excepciones
            System.err.println("Error al ejecutar la consulta: " + e.getMessage());
        }

        return results;
    }

    private static String construirQuery(String fechaInicial, String fechaFinal, String fechaActual) {
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT h.POS_STORE, h.POS_TERMINAL, h.POS_TRANSACTION, h.POS_TRANSACTION_DATE, " +
            "       h.POS_ORDER_PASILLO, h.POS_ASSOCIATE_NUMBER, " +
            "       p.POS_TENDER_TYPE_CODE, p.POS_AMOUNT_DUE, p.POS_CHARGE_OPTIONS, p.POS_AUTHORIZATION_CODE, p.POS_RESPONSE_CODE, p.POS_ACCOUNT_NUMBER " +
            "FROM ONLINE_POS_HEADER h " +
            "LEFT JOIN ONLINE_POS_PAGOS p ON h.POS_STORE = p.POS_STORE " +
            "                             AND h.POS_TERMINAL = p.POS_TERMINAL " +
            "                             AND h.POS_TRANSACTION = p.POS_TRANSACTION " +
            "WHERE h.POS_ORDER_PASILLO IS NOT NULL " +
            "  AND h.POS_ORDER_PASILLO LIKE '400%' "
        );

        System.out.println("Fecha inicial:" + fechaInicial);
        System.out.println("Fecha final:" + fechaFinal);

        // Lógica para fechas
        if (fechaInicial != null && fechaFinal != null) {
        	System.out.println("Fecha inicial y fecha Final");
        	queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') BETWEEN TO_DATE('")
                         .append(fechaInicial)
                         .append("', 'DDMMYY') AND TO_DATE('")
                         .append(fechaFinal)
                         .append("', 'DDMMYY') ");
        } else if (fechaInicial != null) {
        	System.out.println("Fecha inical");
            queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') = TO_DATE('")
                         .append(fechaInicial)
                         .append("', 'DDMMYY') ");
        } else {
        	System.out.println("Fecha Actual");
            queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') = TO_DATE('")
                         .append(fechaActual)
                         .append("', 'DDMMYY') ");
        }

       
        return queryBuilder.toString();
    }


    public static String obtenerFechaActual() {
        // Obtiene la fecha actual
        LocalDate fechaActual = LocalDate.now();
        // Define el formato deseado
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("ddMMyy");
        // Formatea la fecha y la retorna como cadena
        return fechaActual.format(formato);
    }
}
