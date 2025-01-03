package OracleConnection.OrderConfrimEjecutable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;
import OracleConnection.OrderConfrimEjecutable.utils.DataFetcher;
import OracleConnection.OrderConfrimEjecutable.utils.DataInserter;
import OracleConnection.OrderConfrimEjecutable.utils.OrderStatusChecker;

public class App {
    public static void main(String[] args) {
        // Formato para las fechas
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("ddMMyy");
        String fechaInicial;
        String fechaFinal = null;

        // Verificar los argumentos
        if (args.length == 2) {
            // Si se proporcionan dos argumentos, usarlos como fechaInicial y fechaFinal
            fechaInicial = args[0];
            fechaFinal = args[1];
        } else if (args.length == 1) {
            // Si se proporciona un argumento, usarlo como fechaInicial y la fecha actual como fechaFinal
            fechaInicial = args[0];
          //  fechaFinal = LocalDate.now().format(formatoFecha);
        } else {
            // Si no se proporcionan argumentos, usar la fecha actual para ambas
            LocalDate fechaHoy = LocalDate.now();
            fechaInicial = fechaHoy.format(formatoFecha);
           // fechaFinal = fechaHoy.format(formatoFecha);
        }

       
        // Pasar las fechas a DataFetcher
        DataFetcher dataFetcher = new DataFetcher(fechaInicial, fechaFinal);

        // Obtener los datos
        List<PosHeader> pasilloList = dataFetcher.fetchPasilloData();

        // Procesar los resultados
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
			
			 // if(contador >100) { break; }
			 
		}

        
    	DataInserter dt = new DataInserter();
    		dt.insertData(pasilloList);
    }
}
