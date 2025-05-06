import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostTest {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    String url = "https://httpbin.org/post"; // Public test endpoint that echoes POST request data

    // Sample headers and bodies for demonstration
    Map<String, String> headers = Map.of(
        "Authorization", "Bearer abc123", // Example token for secured endpoints
        "Custom-Header", "value" // Custom header for testing
    );

    String stringBody = "name=JTodd&age=100"; // Form-encoded body (application/x-www-form-urlencoded)

    JsonNode jsonBody = objectMapper.createObjectNode()
        .put("name", "JTodd")
        .put("age", 100); // JSON body with user details

    // 1. Basic POST request using only the URL
    // - This sends a POST request without any headers, body, or file saving
    JsonNode data1 = Requests.post(url);

    // 2. POST request with file output enabled (default file path)
    // - The response will be automatically saved to "data/data.json"
    JsonNode data2 = Requests.post(url, true);

    // 3. POST request with custom file path for saving the response
    // - Useful for organizing POST responses into different directories
    JsonNode data3 = Requests.post(url, true, "data/response.json");

    // 4. POST request with headers only
    // - This sends a POST request with custom headers, without a body or file
    // output
    JsonNode data4 = Requests.post(url, headers);

    // 5. POST request with headers and automatic file saving to the default path
    // - Useful when you need to save the response after sending headers only
    JsonNode data5 = Requests.post(url, headers, true);

    // 6. POST request with headers and custom file path for saving the response
    // - Allows you to save the response to a specific file path
    JsonNode data6 = Requests.post(url, headers, true, "data/post_headers_only.json");

    // 7. POST request with a string body only (application/x-www-form-urlencoded)
    // - Sending data as a URL-encoded string
    JsonNode data7 = Requests.post(url, stringBody);

    // 8. POST request with a string body and automatic file saving
    // - Useful for saving the response when submitting a URL-encoded string
    JsonNode data8 = Requests.post(url, stringBody, true);

    // 9. POST request with a string body and custom file path
    // - Send a URL-encoded string and save the response to a custom file
    JsonNode data9 = Requests.post(url, stringBody, true, "data/response_form_encoded.json");

    // 10. POST request with a JSON body only (application/json)
    // - Send data in JSON format
    JsonNode data10 = Requests.post(url, jsonBody);

    // 11. POST request with a JSON body and automatic file saving
    // - Save the response to the default path after sending JSON data
    JsonNode data11 = Requests.post(url, jsonBody, true);

    // 12. POST request with a JSON body and custom file path
    // - Save the JSON response to a specific file
    JsonNode data12 = Requests.post(url, jsonBody, true, "data/response_json.json");

    // 13. POST request with headers and a string body
    // (application/x-www-form-urlencoded)
    // - Combine headers with a URL-encoded string body
    JsonNode data13 = Requests.post(url, headers, stringBody);

    // 14. POST request with headers, string body, and automatic file saving
    // - Send headers and a string body while saving the response to the default
    // file path
    JsonNode data14 = Requests.post(url, headers, stringBody, true);

    // 15. POST request with headers, string body, and custom file path
    // - Send headers and a string body, saving the response to a custom file path
    JsonNode data15 = Requests.post(url, headers, stringBody, true, "data/post_string.json");

    // 16. POST request with headers and a JSON body (application/json)
    // - Combine headers with a JSON body
    JsonNode data16 = Requests.post(url, headers, jsonBody);

    // 17. POST request with headers, JSON body, and automatic file saving
    // - Combine headers and a JSON body, saving the response to the default file
    // path
    JsonNode data17 = Requests.post(url, headers, jsonBody, true);

    // 18. POST request with headers, JSON body, and custom file path
    // - Combine headers and a JSON body, saving the response to a custom file path
    JsonNode data18 = Requests.post(url, headers, jsonBody, true, "data/post_json.json");
  }
}
