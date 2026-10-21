package com.streakbasket.tests.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class Hooks {
    private final Contexto contexto;

    public Hooks(Contexto contexto) {
        this.contexto = contexto;
    }

    @AfterStep // adicionado para que consigamos observar os testes com mais calma!
    public void pausaParaApresentacao() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Before
    public void iniciarNavegador() {
        contexto.iniciarNavegador();
    }

    @After
    public void encerrarNavegador(Scenario cenario) {
        if (!contexto.navegadorIniciado()) {
            return;
        }
        try {
            if (cenario.isFailed()) {
                WebDriver driver = contexto.driver();
                try {
                    byte[] captura = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    cenario.attach(captura, "image/png", "Tela no momento da falha");
                    Path pasta = Path.of("target", "screenshots");
                    Files.createDirectories(pasta);
                    Files.write(pasta.resolve(UUID.randomUUID() + ".png"), captura);
                    cenario.log("URL: " + driver.getCurrentUrl());
                } catch (WebDriverException | IOException exception) {
                    cenario.log("Não foi possível salvar a captura: " + exception.getMessage());
                }
            }
        } finally {
            contexto.encerrar();
        }
    }
}
