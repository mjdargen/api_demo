/*
============================
Maven Setup Instructions
============================

This class uses the built-in Java 11+ HTTP Client (java.net.http)
and the Jackson library for JSON parsing.

Java Version Requirement:
- Make sure your project is using Java 11 or higher.

Add the following to your pom.xml file inside <dependencies>:

<dependencies>
  <!-- Jackson for JSON handling -->
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version> <!-- or latest stable version -->
  </dependency>
</dependencies>

If you're not using Maven:
- Download jackson-databind (and its required modules like jackson-core and jackson-annotations)
  from https://mvnrepository.com
- Add them to your project's classpath manually.
*/

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A beginner-friendly utility class for making HTTP GET and POST requests.
 * Works like Python's requests module. Includes support for:
 * - Optional headers
 * - Optional query parameters (GET)
 * - Optional JSON request body (POST)
 * - Optional saving of JSON responses to file
 */
public class Requests {

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final HttpClient client = HttpClient.newHttpClient();

  // ========== GET METHODS ==========

  /**
   * Sends a simple GET request to the specified URL with no headers or query
   * parameters.
   *
   * @param url the endpoint to send the GET request to
   * @return parsed JSON response
   */
  public static JsonNode get(String url) {
    return get(url, null, null, null);
  }

  /**
   * Sends a GET request to the specified URL and optionally writes the response
   * to a default file.
   *
   * @param url         the endpoint to send the GET request to
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode get(String url, boolean writeToFile) {
    return get(url, null, null, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a GET request to the specified URL and optionally writes the response
   * to a custom file.
   *
   * @param url         the endpoint to send the GET request to
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the path to write the JSON response if {@code writeToFile}
   *                    is true
   * @return parsed JSON response
   */
  public static JsonNode get(String url, boolean writeToFile, String filePath) {
    return get(url, null, null, writeToFile ? filePath : null);
  }

  /**
   * Sends a GET request with optional headers and query parameters.
   *
   * @param url         the endpoint to send the GET request to
   * @param headers     optional headers to include in the request (can be null)
   * @param queryParams optional query parameters to append to the URL (can be
   *                    null)
   * @return parsed JSON response
   */
  public static JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams) {
    return get(url, headers, queryParams, null);
  }

  /**
   * Sends a GET request with optional headers and query parameters, and
   * optionally writes the response to a default file.
   *
   * @param url         the endpoint to send the GET request to
   * @param headers     optional headers to include in the request (can be null)
   * @param queryParams optional query parameters to append to the URL (can be
   *                    null)
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams,
      boolean writeToFile) {
    return get(url, headers, queryParams, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a GET request with optional headers and query parameters, and
   * optionally writes the response to a custom file.
   *
   * @param url         the endpoint to send the GET request to
   * @param headers     optional headers to include in the request (can be null)
   * @param queryParams optional query parameters to append to the URL (can be
   *                    null)
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the path to write the JSON response if {@code writeToFile}
   *                    is true
   * @return parsed JSON response
   */
  public static JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams,
      boolean writeToFile, String filePath) {
    return get(url, headers, queryParams, writeToFile ? filePath : null);
  }

  /**
   * Sends a GET request with optional headers and query parameters.
   *
   * @param url         base URL
   * @param headers     optional headers (can be null)
   * @param queryParams optional query parameters (can be null)
   * @param filePath    optional path to write JSON response (null = don’t write)
   * @return parsed JSON response
   */
  public static JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams,
      String filePath) {
    try {
      HttpClient client = HttpClient.newHttpClient();

      // Add query parameters to the URL if present
      String fullUrl = url;
      if (queryParams != null && !queryParams.isEmpty()) {
        String paramString = queryParams.entrySet().stream()
            .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
        fullUrl += (url.contains("?") ? "&" : "?") + paramString;
      }

      // Build request with optional headers
      HttpRequest.Builder builder = HttpRequest.newBuilder()
          .uri(URI.create(fullUrl))
          .header("Accept", "application/json");

      if (headers != null) {
        headers.forEach(builder::header);
      }

      HttpRequest request = builder.build();
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      // Parse and return JSON
      JsonNode json = objectMapper.readTree(response.body());

      // Optionally write to file
      if (filePath != null) {
        writeToFile(filePath, json);
      }

      return json;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // ========== POST METHODS ==========
  /**
   * Sends a POST request with no headers or body.
   *
   * @param url the endpoint to send the POST request to
   * @return parsed JSON response
   */
  public static JsonNode post(String url) {
    return post(url, null, null, null);
  }

  /**
   * Sends a POST request and optionally writes the response to the default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, boolean writeToFile) {
    return post(url, null, null, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request and optionally writes the response to a specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, boolean writeToFile, String filePath) {
    return post(url, null, null, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with headers but no body.
   *
   * @param url     the endpoint to send the POST request to
   * @param headers optional headers to include in the request
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers) {
    return post(url, headers, null, null);
  }

  /**
   * Sends a POST request with headers and optionally writes the response to the
   * default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, boolean writeToFile) {
    return post(url, headers, null, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request with headers and optionally writes the response to a
   * specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, boolean writeToFile, String filePath) {
    return post(url, headers, null, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with a string body and no headers.
   *
   * @param url  the endpoint to send the POST request to
   * @param body the form-encoded body to send
   * @return parsed JSON response
   */
  public static JsonNode post(String url, String body) {
    return post(url, null, body, null);
  }

  /**
   * Sends a POST request with a string body and optionally writes the response to
   * the default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param body        the form-encoded body to send
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, String body, boolean writeToFile) {
    return post(url, null, body, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request with a string body and optionally writes the response to
   * a specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param body        the form-encoded body to send
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, String body, boolean writeToFile, String filePath) {
    return post(url, null, body, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with a JSON body and no headers.
   *
   * @param url  the endpoint to send the POST request to
   * @param body the JSON body to send
   * @return parsed JSON response
   */
  public static JsonNode post(String url, JsonNode body) {
    return post(url, null, body, null);
  }

  /**
   * Sends a POST request with a JSON body and optionally writes the response to
   * the default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param body        the JSON body to send
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, JsonNode body, boolean writeToFile) {
    return post(url, null, body, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request with a JSON body and optionally writes the response to a
   * specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param body        the JSON body to send
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, JsonNode body, boolean writeToFile, String filePath) {
    return post(url, null, body, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with headers and a string body.
   *
   * @param url     the endpoint to send the POST request to
   * @param headers optional headers to include in the request
   * @param body    the form-encoded body to send
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, String body) {
    return post(url, headers, body, null);
  }

  /**
   * Sends a POST request with headers and a string body, optionally writing the
   * response to the default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param body        the form-encoded body to send
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, String body, boolean writeToFile) {
    return post(url, headers, body, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request with headers and a string body, optionally writing the
   * response to a specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param body        the form-encoded body to send
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, String body, boolean writeToFile,
      String filePath) {
    return post(url, headers, body, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with headers and a JSON body.
   *
   * @param url     the endpoint to send the POST request to
   * @param headers optional headers to include in the request
   * @param body    the JSON body to send
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, JsonNode body) {
    return post(url, headers, body, null);
  }

  /**
   * Sends a POST request with headers and a JSON body, optionally writing the
   * response to the default file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param body        the JSON body to send
   * @param writeToFile whether to write the JSON response to "data/data.json"
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, JsonNode body, boolean writeToFile) {
    return post(url, headers, body, writeToFile ? "data/data.json" : null);
  }

  /**
   * Sends a POST request with headers and a JSON body, optionally writing the
   * response to a specified file.
   *
   * @param url         the endpoint to send the POST request to
   * @param headers     optional headers to include in the request
   * @param body        the JSON body to send
   * @param writeToFile whether to write the JSON response to the provided file
   *                    path
   * @param filePath    the file path to write the response to, if
   *                    {@code writeToFile} is true
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, JsonNode body, boolean writeToFile,
      String filePath) {
    return post(url, headers, body, writeToFile ? filePath : null);
  }

  /**
   * Sends a POST request with optional headers and body (either JSON or String).
   *
   * @param url      endpoint to POST to
   * @param headers  optional headers (can be null)
   * @param body     optional body, which can be either a String or a JsonNode
   *                 (can be null)
   * @param filePath optional path to write JSON response (null = don’t write)
   * @return parsed JSON response
   */
  public static JsonNode post(String url, Map<String, String> headers, Object body, String filePath) {
    try {
      // Determine content type and body type
      String contentType = "application/json"; // Default content type
      String bodyString = null;

      // Build request with optional headers
      HttpRequest.Builder builder = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("Accept", "application/json");

      if (headers != null) {
        headers.forEach(builder::header);
      }

      // Handle body depending on whether it's a String or JsonNode
      if (body != null) {
        if (body instanceof JsonNode) {
          contentType = "application/json";
          bodyString = body.toString();
        } else if (body instanceof String) {
          contentType = "application/x-www-form-urlencoded";
          bodyString = (String) body;
        }
      }

      builder.header("Content-Type", contentType);

      if (bodyString == null) {
        builder.POST(BodyPublishers.noBody());
      } else {
        builder.POST(BodyPublishers.ofString(bodyString));
      }

      HttpRequest request = builder.build();
      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      // Parse and return JSON
      JsonNode json = objectMapper.readTree(response.body());

      // Optionally write to file
      if (filePath != null) {
        writeToFile(filePath, json);
      }

      return json;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // ========== LOCAL JSON UTILITY ==========

  /**
   * Loads and parses a local JSON file into a JsonNode.
   *
   * @param filePath path to the local JSON file
   * @return JsonNode representation of the file contents, or null on failure
   */
  public static JsonNode loadLocalJson(String filePath) {
    try {
      String jsonContent = Files.readString(Path.of(filePath));
      return objectMapper.readTree(jsonContent);
    } catch (IOException e) {
      System.out.println("Failed to read or parse local JSON file: " + filePath);
      e.printStackTrace();
      return null;
    }
  }

  // ========== UTILITY ==========

  /**
   * Helper method to write a JsonNode to a file as pretty-printed JSON.
   *
   * @param filePath where to write the JSON
   * @param data     the JsonNode to write
   */
  private static void writeToFile(String filePath, JsonNode data) {
    try {
      Path path = Path.of(filePath);
      Files.createDirectories(path.getParent());
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
        writer.write(data.toPrettyString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
