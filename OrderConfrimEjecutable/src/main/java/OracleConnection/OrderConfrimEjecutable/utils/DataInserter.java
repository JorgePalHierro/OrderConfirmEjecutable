package OracleConnection.OrderConfrimEjecutable.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;

public class DataInserter {

	private static final String INSERT_QUERY = "INSERT INTO CONFIRMACION_ORDEN (CONSECUTIVO, TIENDA, TERMINAL, TRANSACCION, FECHA, FECHACOMPLETA, "
			+ "NUMEROORDEN, TIPO, NUMTARJETA, IMPORTE, VENDEDOR, ESQUEMA, "
			+ "NUMAUTORIZACION, CODIGORESPUESTA, CONFIRMACION) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String CHECK_EXISTENCE_QUERY = "SELECT COUNT(*) AS COUNT FROM CONFIRMACION_ORDEN WHERE  TIENDA = ? AND TERMINAL = ? AND TRANSACCION = ? AND FECHA = ?";

	public void insertData(List<PosHeader> posHeaders) {

		try (Connection connection = OracleDBConnection.getConnectionProductivo();
				PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY)) {
			int contador = 0;
			
			for (PosHeader posHeader : posHeaders) {
				int numeroRegistro = numdeRegistros(posHeader);
				System.out.println("Número de orden: " + posHeader.toString());
				contador++;
				System.out.println("total de registros:" + numeroRegistro);
				String num;
				if (numeroRegistro == 0) {
					num = String.valueOf(numeroRegistro);
				} else {
					num = String.valueOf(Integer.valueOf(numeroRegistro - 1));
				}

				posHeader.setConsecutivo(num);
				try {
					// posHeader.setConsecutivo("0");
					// Preparar inserción
					System.out.println("Número: " + posHeader.getConsecutivo());
					insertStatement.setString(1, posHeader.getConsecutivo());
					insertStatement.setString(2, posHeader.getPosStore());
					insertStatement.setString(3, posHeader.getPosTerminal());
					insertStatement.setString(4, posHeader.getPosTransaction());
					insertStatement.setString(5, posHeader.getPosTransactionDate());
					insertStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					insertStatement.setString(7, posHeader.getPosOrderPasillo());
					insertStatement.setString(8, posHeader.getPosTenderType());
					insertStatement.setString(9, posHeader.getnumTarjeta());
					insertStatement.setString(10, posHeader.getPosAmountDue());
					insertStatement.setString(11, posHeader.getPosAssociateNumber());
					insertStatement.setString(12, posHeader.getesquema());
					insertStatement.setString(13, posHeader.getautorizacion());
					insertStatement.setString(14, posHeader.getcodigoRespuesta());
					insertStatement.setString(15, posHeader.getconfirmacion());

					insertStatement.executeUpdate(); // Ejecutar inserción para el registro actual
					
				/*	insertStatement.setString(1, "999");
					insertStatement.setString(2, posHeader.getPosStore());
					insertStatement.setString(3, posHeader.getPosTerminal());
					insertStatement.setString(4, posHeader.getPosTransaction());
					insertStatement.setString(5, posHeader.getPosTransactionDate());
					insertStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					insertStatement.setString(7, posHeader.getPosOrderPasillo());
					insertStatement.setString(8, posHeader.getPosTenderType());
					insertStatement.setString(9, posHeader.getnumTarjeta());
					insertStatement.setString(10, posHeader.getTotal());
					insertStatement.setString(11, posHeader.getPosAssociateNumber());
					insertStatement.setString(12, posHeader.getesquema());
					insertStatement.setString(13, posHeader.getautorizacion());
					insertStatement.setString(14, posHeader.getcodigoRespuesta());
					insertStatement.setString(15, posHeader.getconfirmacion());

					
					insertStatement.executeUpdate();*/
					System.out.println("Registro insertado correctamente: " + posHeader);
				} catch (SQLException e) {
					System.err.println("Error al procesar el registro: " + posHeader + ". Error: " + e.getMessage());
				}

				
				  if (contador > 100) { break; }
				 
			}
		} catch (SQLException e) {
			System.err.println("Error al conectar o preparar la base de datos: " + e.getMessage());
		}
	}

	public int numdeRegistros(PosHeader posHeader) {

		try (Connection connection = OracleDBConnection.getConnectionProductivo();
				PreparedStatement checkStatement = connection.prepareStatement(CHECK_EXISTENCE_QUERY)) {

			checkStatement.setString(1, posHeader.getPosStore());
			checkStatement.setString(2, posHeader.getPosTerminal());
			checkStatement.setString(3, posHeader.getPosTransaction());
			checkStatement.setString(4, posHeader.getPosTransactionDate());

			int count = 0;

			try (ResultSet resultSet = checkStatement.executeQuery()) {
				if (resultSet.next()) {
					count = resultSet.getInt("COUNT");
					// System.out.println("Número de registros existentes con la misma clave: " +
					// count);
					if (count > 0) {
						// System.out.println("Registro ya existe: " + posHeader.toString());
					}
				}
			}
			return count;

		} catch (SQLException e) {
			System.err.println("Error al conectar o preparar la base de datos: " + e.getMessage());
			return 0;
		}

	}

	public static String obtenerFechaHora() {
		// Obtener la fecha y hora actual del sistema
		LocalDateTime ahora = LocalDateTime.now();

		// Formato deseado: "yyyy-MM-dd HH:mm:ss.SSS"
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

		// Formatear la fecha y hora
		return ahora.format(formato);
	}
}
