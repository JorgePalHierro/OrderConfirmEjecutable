package OracleConnection.OrderConfrimEjecutable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import OracleConnection.OrderConfrimEjecutable.Modelos.PosHeader;
import OracleConnection.OrderConfrimEjecutable.utils.DataFetcher;

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

        // Imprimir las fechas seleccionadas
        System.out.println("Fecha inicial: " + fechaInicial);
        System.out.println("Fecha final: " + fechaFinal);

        // Pasar las fechas a DataFetcher
        DataFetcher dataFetcher = new DataFetcher(fechaInicial, fechaFinal);

        // Obtener los datos
        List<PosHeader> pasilloList = dataFetcher.fetchPasilloData();

        // Procesar los resultados
        int contador = 0;
        for (PosHeader pasillo : pasilloList) {
            contador++;
            System.out.println("Orden procesada: " + pasillo.toString());
            if (contador > 100) {
                break;
            }
        }
    }
}
