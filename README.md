# Using `Requests.java` for HTTP Requests in Java

## Table of Contents

[1. Introduction to HTTP and JSON](#1-introduction-to-http-and-json)  
[2. Understanding GET vs POST](#2-understanding-get-vs-post)  
[3. What is Jackson?](#3-what-is-jackson)  
[4. Setting Up Your Project with Maven](#4-setting-up-your-java-project-with-maven)  
[5. Overview of `Requests.java`](#5-overview-of-requestsjava)  
[6. Full API Documentation](#6-full-api-documentation)  
[7. Example Usages](#7-example-usages)  
[8. How the Class Works Internally](#8-how-the-class-works-internally)  
[9. What is Jackson's `JsonNode`?](#9-what-is-jacksons-jsonnode)  
[10. Navigating and Accessing Data in a `JsonNode`](#10-navigating-and-accessing-data-in-a-jsonnode)  
[11. Example: Reading IP Information from a JSON Response](#11-example-reading-ip-information-from-a-json-response)  
[12. Additional Tips](#12-additional-tips)

<br>

## 1. Introduction to HTTP and JSON

### What is an HTTP request?

HTTP (HyperText Transfer Protocol) is the standard for how clients (like your program) communicate with servers over the internet. Every time you access a website or use an app that connects to an API, you're making HTTP requests.

### Types of HTTP Requests

- **GET**: Retrieves data from a server.
- **POST**: Sends data to a server (e.g., submitting a form, creating a record).

### What is JSON?

JSON (JavaScript Object Notation) is a format for storing and transferring data. It looks like this:

```json
{
  "name": "Alice",
  "age": 17
}
```

APIs often send and receive data as JSON because it's simple and human-readable.

<br>

## 2. Understanding GET vs POST

| Feature      | GET                                   | POST                                  |
| ------------ | ------------------------------------- | ------------------------------------- |
| Purpose      | Retrieve data                         | Send data                             |
| Data sent in | URL (query string)                    | Request body                          |
| Use case     | Fetching info like weather, user data | Sending form data, creating new items |
| Visibility   | Data is visible in URL                | Data is hidden in body                |

<br>

## 3. What is Jackson?

Jackson is a Java library used to convert between Java objects and JSON. It handles reading JSON responses from HTTP APIs and turning them into Java objects you can work with.

<br>

## 4. Setting Up Your Project with Maven

1. **Install JDK 17+ and Maven**

2. **Create Maven Project** - select when creating Java Project in VSCode

3. **Add Dependencies in `pom.xml`**

   Add this inside `<dependencies>`:

   ```xml
   <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version> <!-- or latest -->
   </dependency>
   ```

   Or you can do it via the GUI in VScode

4. **Add `Requests.java` to Your `src/main/java/...` Folder**

**If you're not using Maven:**

Download the following JAR files and add them to your classpath:

- `jackson-databind`
- `jackson-core`
- `jackson-annotations`

You can find them at: [https://mvnrepository.com](https://mvnrepository.com)

<br>

## 5. Overview of `Requests.java`

`Requests.java` is a utility class that wraps Java’s `HttpURLConnection` and simplifies making HTTP GET and POST requests. It uses Jackson to parse the JSON responses and optionally save them to disk.

### Features:

- Multiple overloaded `get` and `post` methods
- Supports headers and query parameters
- Supports sending data as strings or JSON
- Automatically parses responses into `JsonNode`
- Optionally writes the response to a file

<br>

## 6. Full API Documentation

### GET Methods

```java
JsonNode get(String url)
JsonNode get(String url, boolean saveToFile)
JsonNode get(String url, boolean saveToFile, String filePath)
JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams)
JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams, boolean saveToFile)
JsonNode get(String url, Map<String, String> headers, Map<String, String> queryParams, boolean saveToFile, String filePath)
```

### POST Methods

```java
JsonNode post(String url)
JsonNode post(String url, boolean saveToFile)
JsonNode post(String url, boolean saveToFile, String filePath)
JsonNode post(String url, Map<String, String> headers)
JsonNode post(String url, Map<String, String> headers, boolean saveToFile)
JsonNode post(String url, Map<String, String> headers, boolean saveToFile, String filePath)
JsonNode post(String url, String body)
JsonNode post(String url, String body, boolean saveToFile)
JsonNode post(String url, String body, boolean saveToFile, String filePath)
JsonNode post(String url, JsonNode jsonBody)
JsonNode post(String url, JsonNode jsonBody, boolean saveToFile)
JsonNode post(String url, JsonNode jsonBody, boolean saveToFile, String filePath)
JsonNode post(String url, Map<String, String> headers, String body)
JsonNode post(String url, Map<String, String> headers, String body, boolean saveToFile)
JsonNode post(String url, Map<String, String> headers, String body, boolean saveToFile, String filePath)
JsonNode post(String url, Map<String, String> headers, JsonNode jsonBody)
JsonNode post(String url, Map<String, String> headers, JsonNode jsonBody, boolean saveToFile)
JsonNode post(String url, Map<String, String> headers, JsonNode jsonBody, boolean saveToFile, String filePath)
```

<br>

## 7. Example Usages

### GET Example

```java
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
```

### POST Example

```java
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
```

<br>

## 8. How the Class Works Internally

- Uses `HttpURLConnection` for sending HTTP requests.
- Automatically adds appropriate `Content-Type` headers.
- Builds query strings from `Map<String, String>`.
- Converts JSON strings to `JsonNode` with Jackson.
- Saves responses to disk using `ObjectMapper.writeValue`.

Each overloaded method calls a more complex version, funneling into a core function that sets up the connection, writes the body if needed, reads the response, and optionally writes it to disk.

Here's a clear, structured guide on how to use **Jackson's `JsonNode`** in a Java project. This is based on your `Main.java` example and is aimed at students who are new to handling JSON in Java.

<br>

## 9. What is Jackson's `JsonNode`?

`JsonNode` is a flexible data structure from the Jackson library used to represent JSON objects in Java. It allows you to:

- Access JSON fields using key names
- Loop through fields or arrays
- Convert values to Java primitives like `int`, `double`, `String`, etc.
- Handle dynamic or unknown JSON structures

`JsonNode` is part of the `com.fasterxml.jackson.databind` package.

<br>

## 10. Navigating and Accessing Data in a `JsonNode`

### Access a field directly:

```java
String ip = response.get("ip").asText();
int postal = response.get("postal").asInt();
```

### Loop through all fields:

```java
Iterator<String> fields = response.fieldNames();
while (fields.hasNext()) {
  String field = fields.next();
  System.out.println(field + ": " + response.get(field).asText());
}
```

### Access nested fields:

If a field contains another JSON object:

```java
JsonNode nested = response.get("emotion_scores");
double joyScore = nested.get("joy").asDouble();
```

<br>

## 11. Example: Reading IP Information from a JSON Response

```java
JsonNode ipResponse = Requests.get("http://ipinfo.io/json");

// Print all fields
Iterator<String> keyNames = ipResponse.fieldNames();
while (keyNames.hasNext()) {
  String key = keyNames.next();
  JsonNode value = ipResponse.get(key);
  System.out.println(key + ": " + value.asText());
}

// Print selected fields
System.out.println("IP: " + ipResponse.get("ip").asText());
System.out.println("City: " + ipResponse.get("city").asText());
System.out.println("Country: " + ipResponse.get("country").asText());
```

<br>

## 12. Additional Tips

- `.get("key")` returns a `JsonNode`; to convert it to a regular Java type, use:

  - `.asText()` for strings
  - `.asInt()` for integers
  - `.asDouble()` for floating point numbers
  - `.asBoolean()` for booleans

- Always check for `null` when accessing fields that might not be present.
- Use `.has("key")` or `.hasNonNull("key")` to check if a field exists:

```java
if (json.has("error")) {
  System.out.println("Error: " + json.get("error").asText());
}
```

<br>

Let me know if you'd like this exported to a printable format or converted to slides.
