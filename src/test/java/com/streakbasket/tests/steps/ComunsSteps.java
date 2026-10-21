package com.streakbasket.tests.steps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.streakbasket.tests.pages.BasePage;
import com.streakbasket.tests.pages.StreakBasketPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Passos que servem a qualquer tela: textos, janelas, endereço e grupos de opções. */
public class ComunsSteps {
    private final Contexto contexto;

    public ComunsSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private BasePage pagina() {
        return contexto.pagina(StreakBasketPage.class);
    }

    @Entao("devo ver o texto {string}")
    public void verificarTexto(String texto) {
        assertTrue(pagina().textoNaPagina(texto), "O texto deve aparecer na página: " + texto);
    }

    @Entao("a janela deve conter {string}")
    public void verificarTextoDaJanela(String texto) {
        pagina().aguardarTextoNoModal(texto);
        assertTrue(pagina().textoDoUltimoModal().contains(texto), "A janela deve conter: " + texto);
    }

    @Entao("a janela deve conter as estatísticas {string}")
    public void verificarEstatisticas(String siglas) {
        String janela = pagina().textoDoUltimoModal();
        assertAll(Arrays.stream(siglas.split(",\\s*"))
                .map(sigla -> (Executable) () -> assertTrue(janela.contains(sigla), "Falta a estatística " + sigla)));
    }

    @Quando("clico em {string} na janela")
    public void clicarNaJanela(String texto) {
        pagina().clicarNoModal(texto);
    }

    @Entao("devo estar no endereço {string}")
    public void verificarEndereco(String caminho) {
        pagina().aguardarUrlTerminadaEm(caminho);
        assertTrue(pagina().urlAtual().replaceAll("/+$", "").endsWith(caminho),
                "Endereço atual: " + pagina().urlAtual());
    }

    @Entao("o botão {string} deve estar desabilitado")
    public void verificarBotaoDesabilitado(String texto) {
        assertFalse(pagina().botaoHabilitado(texto), "O botão deve estar desabilitado: " + texto);
    }

    @Entao("o botão {string} deve estar habilitado")
    public void verificarBotaoHabilitado(String texto) {
        assertTrue(pagina().botaoHabilitado(texto), "O botão deve estar habilitado: " + texto);
    }

    @Quando("escolho {string} em {string}")
    public void escolherOpcao(String opcao, String grupo) {
        pagina().escolherOpcao(grupo, opcao);
    }

    @Entao("somente {string} deve estar selecionada em {string}")
    public void verificarOpcaoUnica(String opcao, String grupo) {
        pagina().aguardarOpcaoSelecionada(grupo, opcao);
        assertEquals(List.of(opcao), pagina().opcoesSelecionadas(grupo));
    }

    @Entao("nenhuma opção deve estar selecionada em {string}")
    public void verificarNenhumaOpcao(String grupo) {
        assertEquals(List.of(), pagina().opcoesSelecionadas(grupo));
    }

    @Entao("devo ver as seguintes opções selecionadas:")
    public void verificarOpcoes(List<Map<String, String>> opcoes) {
        assertAll(opcoes.stream().map(linha -> (Executable) () -> assertEquals(
                List.of(linha.get("opcao")), pagina().opcoesSelecionadas(linha.get("grupo")),
                "Opção selecionada em " + linha.get("grupo"))));
    }
}
