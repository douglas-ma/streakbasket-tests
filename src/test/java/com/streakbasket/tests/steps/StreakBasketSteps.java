package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.StreakBasketPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreakBasketSteps {
    private final Contexto contexto;

    public StreakBasketSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private StreakBasketPage pagina() {
        return contexto.pagina(StreakBasketPage.class);
    }

    @Dado("que estou na página inicial do StreakBasket")
    public void abrirPaginaInicial() {
        pagina().abrir();
    }

    @Entao("devo ver o título {string} no navegador")
    public void verificarTitulo(String titulo) {
        assertEquals(titulo, pagina().titulo());
    }

    @Entao("devo ver a marca {string}")
    public void verificarMarca(String marca) {
        assertTrue(pagina().textoVisivel(marca), "A marca deve estar visível");
    }

    @Entao("devo ver o botão {string}")
    public void verificarBotao(String texto) {
        assertTrue(pagina().botaoVisivel(texto), "O botão deve estar visível: " + texto);
    }

    @Quando("abro as instruções de como jogar")
    public void abrirInstrucoes() {
        pagina().abrirInstrucoes();
    }

    @Entao("devo ver as etapas de sorteio, escalação e simulação nas instruções")
    public void verificarInstrucoes() {
        String instrucoes = pagina().textoDoModal();
        assertAll(
                () -> assertTrue(instrucoes.contains("01 / SORTEIE")),
                () -> assertTrue(instrucoes.contains("02 / ESCALE")),
                () -> assertTrue(instrucoes.contains("03 / SIMULE")),
                () -> assertTrue(instrucoes.contains("PG, SG, SF, PF e C")));
    }

    @Quando("confirmo que entendi as instruções")
    public void fecharInstrucoes() {
        pagina().fecharInstrucoes();
    }

    @Entao("as instruções devem ser fechadas")
    public void verificarFechamento() {
        assertTrue(pagina().modalFechado(), "O modal de instruções deve estar fechado");
    }

    @Quando("escolho o modo {string}")
    public void escolherModo(String modo) {
        pagina().escolherModo(modo);
    }

    @Entao("devo ver a preparação {string}")
    public void verificarPreparacao(String titulo) {
        assertTrue(pagina().textoVisivel(titulo), "O modo escolhido deve abrir sua preparação");
    }

    @Entao("as cinco posições do quinteto devem estar vazias")
    public void verificarQuintetoVazio() {
        for (String posicao : new String[] {"PG", "SG", "SF", "PF", "C"}) {
            assertTrue(pagina().posicaoVazia(posicao), "A posição deve começar vazia: " + posicao);
        }
    }

    @Quando("abro meu perfil")
    public void abrirPerfil() {
        pagina().abrirPerfil();
    }

    @Entao("devo ver o formulário de acesso com e-mail e senha")
    public void verificarFormulario() {
        assertAll(
                () -> assertTrue(pagina().textoDoModal().contains("MEU PERFIL")),
                () -> assertEquals("email", pagina().campoEmail().getDomAttribute("type")),
                () -> assertEquals("password", pagina().campoSenha().getDomAttribute("type")),
                () -> assertEquals("", pagina().campoEmail().getDomProperty("value")),
                () -> assertEquals("", pagina().campoSenha().getDomProperty("value")),
                () -> assertTrue(pagina().botaoVisivel("ENTRAR E SINCRONIZAR")));
    }

    @Quando("abro o ranking geral")
    public void abrirRanking() {
        pagina().abrirRanking();
    }

    @Entao("devo ver o painel do ranking com filtros de modo e dificuldade")
    public void verificarRanking() {
        String ranking = pagina().textoDoModal();
        assertAll(
                () -> assertTrue(ranking.contains("RANKING GERAL")),
                () -> assertTrue(ranking.contains("TEMPORADA ATUAL")),
                () -> assertTrue(ranking.contains("TEMPORADA NBA")),
                () -> assertTrue(ranking.contains("ROOKIE")),
                () -> assertTrue(ranking.contains("ALL-STAR")),
                () -> assertTrue(ranking.contains("HOF")));
    }

    @Quando("abro meu ranking")
    public void abrirMeuRanking() {
        pagina().abrirMeuRanking();
    }

    @Quando("abro as informações sobre o projeto")
    public void abrirSobre() {
        pagina().abrirSobre();
    }

    @Entao("devo ver o link {string}")
    public void verificarLink(String rotulo) {
        assertTrue(pagina().linkVisivel(rotulo), "O link deve estar visível: " + rotulo);
    }

    @Entao("nenhuma janela deve estar aberta")
    public void verificarSemJanela() {
        pagina().aguardarModalFechado();
        assertTrue(pagina().modalFechado(), "Nenhuma janela deve continuar aberta");
    }

    @Quando("recarrego a página")
    public void recarregar() {
        pagina().recarregar();
    }
}
