package seleniumtest;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.VncRecordingContainer;

import java.io.File;
import java.net.URL;

public class ConfigureWebDriver {
  public static BrowserWebDriverContainer<?> configure(int autPort, ChromeOptions chromeOptions) {
    BrowserWebDriverContainer<?> chrome = new BrowserWebDriverContainer<>()
      .withAccessToHost(true)
      .withRecordingMode(
        BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
        new File("video"),
        VncRecordingContainer.VncRecordingFormat.MP4
      );

    Testcontainers.exposeHostPorts(autPort);
    chrome.setShmSize(0L);
    chrome.start();
    URL chromeSeleniumAddress = chrome.getSeleniumAddress();
    System.out.println(
      "chrome selenium address is "
        + chromeSeleniumAddress.toString().replaceFirst("(\\d+)/.+$", "$1")
    );

    WebDriver driver = new RemoteWebDriver(chromeSeleniumAddress, chromeOptions);
    WebDriverRunner.setWebDriver(driver);

    return chrome;
  }
}
