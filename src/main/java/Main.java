import java.util.Iterator;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;

public class Main {
  public static void main(String[] args) {

    // ========================================
    // Example 1: GET request to retrieve IP info
    // ========================================
    System.out.println("\n\n -- Example 1: GET request to retrieve IP info -- ");
    System.out.println("Your IP Address Info: ");

    // Define the URL to fetch your public IP info as JSON
    String url = "http://ipinfo.io/json";

    // Make the GET request and parse response as a JsonNode
    JsonNode ipResponse = Requests.get(url, true);

    // Loop through all fields and print each key-value pair
    Iterator<String> keyNames = ipResponse.fieldNames();
    while (keyNames.hasNext()) {
      String keyName = keyNames.next();
      JsonNode value = ipResponse.get(keyName);
      System.out.println(keyName + ": " + value.asText());
    }

    // Alternatively: manually print specific fields from the response
    System.out.println("\n\nYour IP Address Info (selected fields): ");
    System.out.println("IP: " + ipResponse.get("ip").asText());
    System.out.println("Hostname: " + ipResponse.get("hostname").asText());
    System.out.println("City: " + ipResponse.get("city").asText());
    System.out.println("Region: " + ipResponse.get("region").asText());
    System.out.println("Country: " + ipResponse.get("country").asText());
    System.out.println("Location (lat,long): " + ipResponse.get("loc").asText());
    System.out.println("Org: " + ipResponse.get("org").asText());
    System.out.println("Postal Code: " + ipResponse.get("postal").asInt());
    System.out.println("Timezone: " + ipResponse.get("timezone").asText());

    // ========================================
    // Example 2: POST request to Emotion API
    // ========================================
    System.out.println("\n\n -- Example 2: POST request to Emotion API -- ");
    // Set the API endpoint URL for emotion analysis
    url = "https://twinword-emotion-analysis-v1.p.rapidapi.com/analyze/";

    // Define required headers, including API key and content type
    Map<String, String> headers = Map.of(
        "X-RapidAPI-Key", "f57f8e9a13mshc8dbe8587fc090cp1e6b8cjsndfb06d21fb13",
        "X-RapidAPI-Host", "twinword-emotion-analysis-v1.p.rapidapi.com");

    // Define the body of the POST request — must be form-encoded
    String body = "text=I am so excited to start my new adventure!";

    // Send the POST request and get the response as JsonNode
    JsonNode sentimentResponse = Requests.post(url, headers, body);

    // Print the full JSON response (useful for debugging)
    System.out.println("\nFull API Response:");
    System.out.println(sentimentResponse);

    // Print just the list of detected emotions
    System.out.println("\nEmotions Detected:");
    System.out.println(sentimentResponse.get("emotions_detected"));

    // Print emotion scores as a whole
    System.out.println("\nEmotion Scores:");
    System.out.println(sentimentResponse.get("emotion_scores"));

    // Iterate and print each individual emotion score
    System.out.println("\nIndividual Scores:");
    Iterator<String> fieldNames = sentimentResponse.get("emotion_scores").fieldNames();
    while (fieldNames.hasNext()) {
      String key = fieldNames.next();
      double value = sentimentResponse.get("emotion_scores").get(key).asDouble();
      System.out.println(key + " = " + value);
    }
  }
}
