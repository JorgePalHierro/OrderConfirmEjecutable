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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;

public class DataFetcher {

	
	private static String fechaInicial;
	private static String fechaFinal;

	// Bloque estático para cargar propiedades
	/*static {
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
	}*/

	
	  public DataFetcher(String fechaInicial, String fechaFinal) {
	        this.fechaInicial = fechaInicial;
	        this.fechaFinal = fechaFinal;
	    }

	public List<PosHeader> fetchPasilloData() {
		List<PosHeader> results = new ArrayList<>();

		String fechaActual = obtenerFechaActual(); // Fecha actual del sistema
		String query = construirQuery(fechaInicial, fechaFinal, fechaActual);

		System.out.println(query);

		try (Connection connection = OracleDBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			int cont = 0;
			while (resultSet.next()) {
				if (cont > 20) {
					break;
				}
				PosHeader posHeader = new PosHeader(resultSet.getString("POS_STORE"),
						resultSet.getString("POS_TERMINAL"), resultSet.getString("POS_TRANSACTION"),
						resultSet.getString("POS_TRANSACTION_DATE"), resultSet.getString("POS_ORDER_PASILLO"),
						resultSet.getString("POS_ASSOCIATE_NUMBER"));

				int numRegistroPosPagos = countPosHeaderRecords(posHeader);

				if (numRegistroPosPagos > 0) {
					results.add(posHeader);
					// System.out.println(posHeader.toString());
					// System.out.println("Número de registros" + countPosHeaderRecords(posHeader));
				}

				countPosHeaderRecords(posHeader);
				cont++;
			}

		} catch (SQLException e) {
			System.err.println("Error al ejecutar la consulta: " + e.getMessage());
		}

		// Llamada al nuevo método para completar los datos de pagos
		return fetchPagosData(results);
	}

	private static String construirQuery(String fechaInicial, String fechaFinal, String fechaActual) {
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT h.POS_STORE, h.POS_TERMINAL, h.POS_TRANSACTION, h.POS_TRANSACTION_DATE, "
						+ "       h.POS_ORDER_PASILLO, h.POS_ASSOCIATE_NUMBER " + // Solo columnas de ONLINE_POS_HEADER
						"FROM ONLINE_POS_HEADER h " + "WHERE h.POS_ORDER_PASILLO IS NOT NULL "
						+ "  AND h.POS_ORDER_PASILLO LIKE '400%' ");

		System.out.println("Fecha inicial:" + fechaInicial);
		System.out.println("Fecha final:" + fechaFinal);

		// Lógica para fechas
		if (fechaInicial != null && fechaFinal != null) {
			System.out.println("Fecha inicial y fecha Final");
			queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') BETWEEN TO_DATE('").append(fechaInicial)
					.append("', 'DDMMYY') AND TO_DATE('").append(fechaFinal).append("', 'DDMMYY') ");
		} else if (fechaInicial != null) {
			System.out.println("Fecha inicial");
			queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') = TO_DATE('").append(fechaInicial)
					.append("', 'DDMMYY') ");
		} else {
			System.out.println("Fecha Actual");
			queryBuilder.append("AND TO_DATE(h.POS_TRANSACTION_DATE, 'DDMMYY') = TO_DATE('").append(fechaActual)
					.append("', 'DDMMYY') ");
		}

		return queryBuilder.toString();
	}

	public List<PosHeader> fetchPagosData(List<PosHeader> results) {
	    List<PosHeader> respuesta = new ArrayList<>();
	    
	    String queryPagos = "SELECT p.POS_STORE, p.POS_TERMINAL, p.POS_TRANSACTION, "
	            + "       p.POS_TENDER_TYPE_CODE, p.POS_AMOUNT_DUE, p.POS_TENDER_AUTHORIZATION_TEXT, p.POS_CHARGE_OPTIONS, "
	            + "       p.POS_AUTHORIZATION_CODE, p.POS_RESPONSE_CODE, p.POS_ACCOUNT_NUMBER, p.POS_TENDER_DELETED "
	            + "FROM ONLINE_POS_PAGOS p " + "WHERE p.POS_STORE = ? AND p.POS_TERMINAL = ? AND p.POS_TRANSACTION = ? AND p.POS_BUSINESS_DATE = ?";

	    try (Connection connection = OracleDBConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(queryPagos)) {

	        for (PosHeader header : results) {
	            preparedStatement.setString(1, header.getPosStore());
	            preparedStatement.setString(2, header.getPosTerminal());
	            preparedStatement.setString(3, header.getPosTransaction());
	            preparedStatement.setString(4, header.getPosTransactionDate());

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    String authText = resultSet.getString("POS_TENDER_AUTHORIZATION_TEXT");
	                    String tenderTypeCode = resultSet.getString("POS_TENDER_TYPE_CODE");
	                    String tenderDeleted = resultSet.getString("POS_TENDER_DELETED");

	                    // Verificar condiciones de validación
	                    if ("***BORRAR FORMA PAGO***".equals(tenderDeleted)) {
	                        continue; // Omitir este registro
	                    }

	                    if ((authText == null && "1".equals(tenderTypeCode))
	                            || "Transacion aprobada".equals(authText)) {
	                        // Crear un nuevo PosHeader basado en el objeto original
	                        PosHeader nuevoRegistro = new PosHeader(
	                                header.getConsecutivo(),
	                                header.getPosStore(),
	                                header.getPosTerminal(),
	                                header.getPosTransaction(),
	                                header.getPosTransactionDate(),
	                                header.getPosOrderPasillo(),
	                                header.getPosAssociateNumber(),
	                                resultSet.getString("POS_TENDER_TYPE_CODE"),
	                                resultSet.getString("POS_AMOUNT_DUE"),
	                                header.getfechaCompleta(),
	                                resultSet.getString("POS_CHARGE_OPTIONS"),
	                                resultSet.getString("POS_AUTHORIZATION_CODE"),
	                                resultSet.getString("POS_RESPONSE_CODE"),
	                                header.getconfirmacion(),
	                                resultSet.getString("POS_ACCOUNT_NUMBER")
	                        );

	                        respuesta.add(nuevoRegistro);
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error al ejecutar el query: " + e.getMessage());
	    }
	    
	    return respuesta;
	}



	public int countPosHeaderRecords(PosHeader header) {
		String countQuery = "SELECT COUNT(*) AS total_count " + "FROM ONLINE_POS_PAGOS p "
				+ "WHERE p.POS_STORE = ? AND p.POS_TERMINAL = ? AND p.POS_TRANSACTION = ? AND p.POS_BUSINESS_DATE = ?";
		int recordCount = 0;

		try (Connection connection = OracleDBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(countQuery)) {

			// Configurar los parámetros del query
			preparedStatement.setString(1, header.getPosStore());
			preparedStatement.setString(2, header.getPosTerminal());
			preparedStatement.setString(3, header.getPosTransaction());
			preparedStatement.setString(4, header.getPosTransactionDate());

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					recordCount = resultSet.getInt("total_count");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error al contar registros: " + e.getMessage());
		}

		return recordCount;
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
