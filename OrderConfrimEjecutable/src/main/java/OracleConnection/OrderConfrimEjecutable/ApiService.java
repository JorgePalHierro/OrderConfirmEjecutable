package OracleConnection.OrderConfrimEjecutable;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class ApiService {

    private static final String URL = "https://ph-bypass-pos-exp-api.us-w1.cloudhub.io/api/v1/orders/";
    private static final String CLIENT_ID = "87cd348c017447b9991fc1eb4e0cccd4";
    private static final String CLIENT_SECRET = "de22aa5e10514445Ac9eDa16475E5bFf";

    public String getApiResponse(String numOrden) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("client_id", CLIENT_ID);
        headers.set("client_secret", CLIENT_SECRET);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.exchange(
                URL + numOrden,
                HttpMethod.GET,
                entity,
                String.class
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                System.err.println("Error 404: Recurso no encontrado - " + e.getResponseBodyAsString());
                return "No se encontró la orden";
            } else {
                System.err.println("Error de cliente: " + e.getStatusCode() + " - " + e.getMessage());
                return "Error al consultar la API: " + e.getMessage();
            }
        } catch (HttpServerErrorException e) {
            System.err.println("Error de servidor: " + e.getStatusCode() + " - " + e.getMessage());
            return "Error del servidor: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
            return "Error inesperado: " + e.getMessage();
        }

        return response.getBody();
    }
}
