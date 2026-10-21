package com.streakbasket.tests.support;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.streakbasket.tests.pages.BasePage;
import org.openqa.selenium.WebDriver;

/**
 * Estado de um cenário, compartilhado por hooks e classes de passos.
 * O PicoContainer cria uma instância nova por cenário e a injeta pelo construtor.
 */
public class Contexto {
    private WebDriver driver;
    private final Map<Class<?>, BasePage> paginas = new HashMap<>();

    public void iniciarNavegador() {
        driver = BrowserFactory.criarChrome();
    }

    public WebDriver driver() {
        if (driver == null) {
            throw new IllegalStateException("O navegador ainda não foi iniciado");
        }
        return driver;
    }

    public boolean navegadorIniciado() {
        return driver != null;
    }

    /** Devolve a página do tipo pedido, criando-a no primeiro uso do cenário. */
    public <T extends BasePage> T pagina(Class<T> tipo) {
        return tipo.cast(paginas.computeIfAbsent(tipo, this::criar));
    }

    public void encerrar() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        paginas.clear();
    }

    private BasePage criar(Class<?> tipo) {
        try {
            return (BasePage) tipo.getConstructor(WebDriver.class).newInstance(driver());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException exception) {
            throw new IllegalStateException("Não foi possível criar a página " + tipo.getSimpleName(), exception);
        }
    }
}
