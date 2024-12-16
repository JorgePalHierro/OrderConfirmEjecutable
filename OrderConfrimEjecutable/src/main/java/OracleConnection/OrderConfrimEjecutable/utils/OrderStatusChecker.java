package OracleConnection.OrderConfrimEjecutable.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderStatusChecker {

	public String isOrderConfirmed(String jsonResponse) throws JsonMappingException, JsonProcessingException {
		try {
			if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
				System.err.println("Respuesta nula o vacía.");
				return "2";
			}

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonResponse);
			String paymentStatus = rootNode.get("payment_status").asText();

			if (!paymentStatus.contains("not_paid") && !paymentStatus.contains("no_encontrado")) {

				return "1";
			} else {
				return "0";
			}

		} catch (JSONException e) {
			System.err.println("La respuesta no es un JSON válido: " + e.getMessage());
			return "2";
		}
	}

	public String ObtenerOrder_total(String jsonResponse) throws JsonMappingException, JsonProcessingException {
		String amount;
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonResponse);

			amount = rootNode.get("order_total").asText(); // Extrae el campo
															// "c_originalPrice"
			return shiftDecimal(amount);

		} catch (Exception e) {
			e.printStackTrace();
			return "00";
		}
	}

	public static String shiftDecimal(String input) {
		try {
			// Convierte la cadena a un número flotante
			double number = Double.parseDouble(input);

			// Multiplica por 100 para mover el punto decimal dos lugares a la derecha
			long shiftedNumber = Math.round(number * 100);

			// Devuelve el número como cadena
			return String.valueOf(shiftedNumber);
		} catch (NumberFormatException e) {
			// Manejo de errores si la cadena no es válida
			System.err.println("Error: Entrada no válida");
			return null;
		}
	}

}
