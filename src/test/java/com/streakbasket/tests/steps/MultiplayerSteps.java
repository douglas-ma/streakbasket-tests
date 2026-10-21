package com.streakbasket.tests.steps;

import com.streakbasket.tests.pages.MultiplayerPage;
import com.streakbasket.tests.support.Contexto;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultiplayerSteps {
    private final Contexto contexto;

    public MultiplayerSteps(Contexto contexto) {
        this.contexto = contexto;
    }

    private MultiplayerPage multiplayer() {
        return contexto.pagina(MultiplayerPage.class);
    }

    @Entao("o campo de nome na sala deve aceitar até {int} caracteres")
    public void verificarCampoNome(int limite) {
        assertEquals(String.valueOf(limite), multiplayer().campoNome().getDomAttribute("maxlength"));
    }

    @Entao("o campo de código da sala deve aceitar até {int} caracteres")
    public void verificarCampoCodigo(int limite) {
        assertEquals(String.valueOf(limite), multiplayer().campoCodigo().getDomAttribute("maxlength"));
    }

    @Entao("devo ver o botão para entrar na sala")
    public void verificarEntrarNaSala() {
        assertTrue(multiplayer().botaoEntrarNaSalaVisivel(), "O botão de entrar na sala deve estar visível");
    }

    @Quando("abro a criação de sala")
    public void abrirCriacao() {
        multiplayer().abrirCriacaoDeSala();
    }

    @Quando("volto às opções do multiplayer")
    public void voltar() {
        multiplayer().voltarAsOpcoes();
    }
}
