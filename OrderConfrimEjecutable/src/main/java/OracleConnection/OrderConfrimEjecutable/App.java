package OracleConnection.OrderConfrimEjecutable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;
import OracleConnection.OrderConfrimEjecutable.utils.DataFetcher;
import OracleConnection.OrderConfrimEjecutable.utils.DataInserter;
import OracleConnection.OrderConfrimEjecutable.utils.OracleDBConnection;
import OracleConnection.OrderConfrimEjecutable.utils.OrderStatusChecker;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		

		// Se obtienen los registros de la base de datos
		DataFetcher dataFetcher = new DataFetcher();
		
		//Se crea la lista de orndenes que serán insertadas en la base
		List<PosHeader> pasilloList = dataFetcher.fetchPasilloData();
		/*
		 * if (!pasilloList.isEmpty()) { DataInserter dataInserter = new DataInserter();
		 * dataInserter.insertData(pasilloList); } else {
		 * System.out.println("No se encontraron datos para insertar."); }
		 * 
		 */
		
		

		int contador = 0;
		for (PosHeader pasillo : pasilloList) {
			contador++;

			// System.out.println("orden:"+ pasillo.toString());
			OrderStatusChecker orderStatus = new OrderStatusChecker();
			ApiService apiService = new ApiService();
			String respuesta = apiService.getApiResponse(pasillo.getPosOrderPasillo());

			try {
				// Verificar si la respuesta es válida
				pasillo.setconfirmacion(orderStatus.isOrderConfirmed(respuesta));
				
				pasillo.setTotal(orderStatus.ObtenerOrder_total(respuesta));

			} catch (Exception e) {
				// Manejo del error si no es un JSON válido
				System.err.println("Error al procesar la respuesta de la API: " + e.getMessage());
				System.err.println("Respuesta recibida: " + respuesta);
				pasillo.setconfirmacion("0"); // Valor predeterminado en caso de error
			}

			System.out.println("Número de orden: " + pasillo.toString());
			
			  if(contador >100) { break; }
			 
		}

		DataInserter dt = new DataInserter();
		dt.insertData(pasilloList);
	}

}
