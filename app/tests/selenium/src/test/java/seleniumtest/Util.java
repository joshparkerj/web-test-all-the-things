package seleniumtest;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.logging.LogEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.executeAsyncJavaScript;
import static com.codeborne.selenide.Selenide.executeJavaScript;

public class Util {
  public static String getStringFromJson(String jsonString, String[] keys) {
    //language=JavaScript
    String script = "const obj = JSON.parse(arguments[0]);" +
      "return arguments[1].reduce((acc, e) => acc ? acc[e] : null, obj);";

    return executeJavaScript(script, jsonString, keys);
  }

  public static List<LogEntry> getRequestsByMethod(String method) {
    return WebDriverRunner.getWebDriver().manage().logs().get("performance").getAll().stream()
      .filter(logEntry -> method.equals(getStringFromJson(
        logEntry.getMessage(),
        new String[]{"message", "params", "request", "method"}
      )))
      .collect(Collectors.toList());
  }

  public static Set<String> objectKeysFromApiCall(String path) {
    //language=JavaScript
    String script = "const callback = arguments[arguments.length - 1];" +
      "fetch(arguments[0])" +
      ".then(response => { console.log('got response'); return response.json(); })" +
      ".then(data => { console.log(data); const keys = Object.keys(data); console.log(keys); const keySet = new Set(keys); console.log(keySet); callback(keys); })";
    return new HashSet(executeAsyncJavaScript(script, path));
  }
}
