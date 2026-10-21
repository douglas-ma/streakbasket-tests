package com.streakbasket.tests.support;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class BrowserFactory {
    private BrowserFactory() {
    }

    public static WebDriver criarChrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000", "--lang=pt-BR");
        options.setExperimentalOption("prefs", Map.of("intl.accept_languages", "pt-BR,pt"));
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }

        WebDriver driver = new ChromeDriver(options);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
            return driver;
        } catch (RuntimeException exception) {
            driver.quit();
            throw exception;
        }
    }
}
