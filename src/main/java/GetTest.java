import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public class GetTest {

  public static void main(String[] args) {
    String url = "https://httpbin.org/get"; // Public test endpoint that echoes GET request data

    // 1. Basic GET request using only the URL
    // - No headers, query parameters, or file output
    JsonNode data1 = Requests.get(url);

    // 2. GET request with file output enabled
    // - The response will be saved to "data/data.json" automatically
    JsonNode data2 = Requests.get(url, true);

    // 3. GET request with file output saved to a custom file path
    // - Useful for organizing responses into separate output files
    JsonNode data3 = Requests.get(url, true, "data/custom_output.json");

    // Define example headers and query parameters
    Map<String, String> headers = Map.of(
        "Authorization", "Bearer abc123", // Example token for secured endpoints
        "Accept-Language", "en-US" // Ask for English language content
    );

    Map<String, String> queryParams = Map.of(
        "page", "1", // Simulate pagination
        "limit", "20" // Number of results to retrieve
    );

    // 4. GET request including headers and query parameters
    // - Common for API requests that require authentication or customization
    JsonNode data4 = Requests.get(url, headers, queryParams);

    // 5. GET request with headers, query parameters, and automatic file saving
    // - Results will be saved to "data/data.json"
    JsonNode data5 = Requests.get(url, headers, queryParams, true);

    // 6. Full-featured GET request: headers, query parameters, and custom output
    // file
    // - Ideal for logging or archiving specific API calls for later review
    JsonNode data6 = Requests.get(url, headers, queryParams, true, "data/full_response.json");
  }
}
