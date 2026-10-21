package com.streakbasket.tests.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Esperas, cliques e seletores compartilhados pelas páginas.
 *
 * <p>O site é feito com React Native Web: telas e janelas usam {@code role="dialog"},
 * alguns controles são {@code div[tabindex="0"]} em vez de {@code button} e a opção
 * selecionada só é indicada pela cor de fundo.
 */
public abstract class BasePage {
    protected static final By MODAL = By.cssSelector("[role='dialog'][aria-modal='true']");
    private static final By PERFIL = By.cssSelector("button[aria-label='Entrar para sincronizar progresso']");

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Long.getLong("timeout.seconds", 20L)));
        this.wait.ignoring(StaleElementReferenceException.class);
    }

    protected String baseUrl() {
        return System.getProperty("base.url", "https://streakbasket.vercel.app").replaceAll("/+$", "");
    }

    public String urlAtual() {
        return driver.getCurrentUrl();
    }

    public void aguardarUrlTerminadaEm(String caminho) {
        wait.until(d -> d.getCurrentUrl().replaceAll("/+$", "").endsWith(caminho));
    }

    public boolean textoVisivel(String texto) {
        return visivel(By.xpath("//*[normalize-space(text())=" + literal(texto) + "]")).isDisplayed();
    }

    public boolean textoNaPagina(String trecho) {
        return Boolean.TRUE.equals(wait.until(d -> d.findElement(By.tagName("body")).getText().contains(trecho)));
    }

    public boolean botaoVisivel(String texto) {
        return visivel(botaoComTexto(texto)).isDisplayed();
    }

    public boolean botaoHabilitado(String texto) {
        return visivel(botaoComTexto(texto)).isEnabled();
    }

    /** A janela aberta por último; as telas internas também são diálogos e ficam por baixo. */
    public WebElement ultimoModal() {
        return wait.until(d -> {
            List<WebElement> visiveis = d.findElements(MODAL).stream().filter(WebElement::isDisplayed).toList();
            return visiveis.isEmpty() ? null : visiveis.get(visiveis.size() - 1);
        });
    }

    public String textoDoUltimoModal() {
        return wait.until(d -> ultimoModal().getText());
    }

    public void aguardarTextoNoModal(String trecho) {
        wait.until(d -> ultimoModal().getText().contains(trecho));
    }

    /** Clica em um botão pelo texto, procurando apenas dentro da janela aberta por último. */
    public void clicarNoModal(String texto) {
        By seletor = By.xpath(".//button[normalize-space(.)=" + literal(texto)
                + " or .//*[normalize-space(text())=" + literal(texto) + "]]");
        WebElement botao = wait.until(d -> ultimoModal().findElements(seletor).stream()
                .filter(WebElement::isDisplayed).findFirst().orElse(null));
        clicar(botao);
    }

    /**
     * Texto das opções destacadas em um grupo ("MODO DE JOGO", "DIFICULDADE"...).
     * Destacada é a opção cujo fundo difere do fundo neutro dos botões do cabeçalho.
     */
    public List<String> opcoesSelecionadas(String grupo) {
        visivel(By.xpath("//*[normalize-space(text())=" + literal(grupo) + "]"));
        @SuppressWarnings("unchecked")
        List<String> selecionadas = (List<String>) js("""
                const [grupo, neutro] = arguments;
                const visivel = e => { const r = e.getBoundingClientRect(); return r.width > 0 && r.height > 0; };
                const rotulos = [...document.querySelectorAll('div')]
                    .filter(e => visivel(e) && e.childElementCount === 0 && e.textContent.trim() === grupo);
                const rotulo = rotulos[rotulos.length - 1];
                // As opções de um grupo são botões irmãos na linha logo depois do rótulo.
                const primeira = [...document.querySelectorAll('button:not([aria-label])')]
                    .filter(visivel)
                    .find(b => rotulo.compareDocumentPosition(b) & Node.DOCUMENT_POSITION_FOLLOWING);
                const fundoNeutro = getComputedStyle(neutro).backgroundColor;
                return [...primeira.parentElement.children]
                    .filter(b => b.tagName === 'BUTTON' && visivel(b))
                    .filter(b => getComputedStyle(b).backgroundColor !== fundoNeutro)
                    .map(b => b.innerText.trim());
                """, grupo, visivel(PERFIL));
        return selecionadas;
    }

    public void escolherOpcao(String grupo, String opcao) {
        clicar(By.xpath("(//*[normalize-space(text())=" + literal(grupo) + "])[last()]"
                + "/following::button[normalize-space(.)=" + literal(opcao) + "][1]"));
    }

    public void aguardarOpcaoSelecionada(String grupo, String opcao) {
        wait.until(d -> opcoesSelecionadas(grupo).equals(List.of(opcao)));
    }

    protected WebElement visivel(By seletor) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(seletor));
    }

    protected void clicar(By seletor) {
        wait.until(ExpectedConditions.elementToBeClickable(seletor)).click();
    }

    protected void clicar(WebElement elemento) {
        wait.until(ExpectedConditions.elementToBeClickable(elemento)).click();
    }

    /** Clica se o elemento aparecer dentro do prazo; útil para atalhos opcionais. */
    protected boolean clicarSeAparecer(By seletor, Duration prazo) {
        try {
            new WebDriverWait(driver, prazo)
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.elementToBeClickable(seletor))
                    .click();
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    protected Object js(String script, Object... argumentos) {
        return ((JavascriptExecutor) driver).executeScript(script, argumentos);
    }

    protected static By botaoComTexto(String texto) {
        return By.xpath("//button[normalize-space(.)=" + literal(texto)
                + " or .//*[normalize-space(text())=" + literal(texto) + "]]");
    }

    /** Controles feitos com {@code div[tabindex="0"]}; escolhe o mais interno que contém o texto. */
    protected static By controleComTexto(String texto) {
        String temTexto = "[.//*[normalize-space(text())=" + literal(texto) + "]]";
        return By.xpath("//div[@tabindex='0']" + temTexto + "[not(.//div[@tabindex='0']" + temTexto + ")]");
    }

    /** Literal XPath que aceita aspas simples, como em "Shaquille O'Neal". */
    protected static String literal(String texto) {
        if (!texto.contains("'")) {
            return "'" + texto + "'";
        }
        if (!texto.contains("\"")) {
            return "\"" + texto + "\"";
        }
        return "concat('" + texto.replace("'", "', \"'\", '") + "')";
    }
}
