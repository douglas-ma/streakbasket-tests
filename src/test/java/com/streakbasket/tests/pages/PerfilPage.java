package com.streakbasket.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Janela "MEU PERFIL". Os testes usam apenas dados inválidos, barrados antes de qualquer envio. */
public class PerfilPage extends BasePage {
    private static final By EMAIL = By.cssSelector("input[aria-label='E-mail da conta']");
    private static final By SENHA = By.cssSelector("input[aria-label='Senha da conta']");

    public PerfilPage(WebDriver driver) {
        super(driver);
    }

    public void preencher(String email, String senha) {
        digitar(visivel(EMAIL), email);
        digitar(visivel(SENHA), senha);
    }

    public void escolherAba(String aba) {
        clicarNoModal(aba);
    }

    public void enviar(String botao) {
        // Na aba "CRIAR CONTA" a aba e o botão de envio têm o mesmo texto; o envio vem depois dos campos.
        clicar(By.xpath("//input[@aria-label='Senha da conta']/following::button[normalize-space(.)="
                + literal(botao) + "][1]"));
    }

    public boolean botaoDeEnvioVisivel(String texto) {
        return visivel(By.xpath("//input[@aria-label='Senha da conta']/following::button[normalize-space(.)="
                + literal(texto) + "][1]")).isDisplayed();
    }

    public void fechar() {
        clicar(By.cssSelector("button[aria-label='Fechar conta']"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(EMAIL));
    }

    public boolean formularioFechado() {
        return driver.findElements(EMAIL).stream().noneMatch(WebElement::isDisplayed);
    }

    private static void digitar(WebElement campo, String valor) {
        campo.clear();
        if (!valor.isEmpty()) {
            campo.sendKeys(valor);
        }
    }
}
