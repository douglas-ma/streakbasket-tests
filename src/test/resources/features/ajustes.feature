#language: pt
@ajustes
Funcionalidade: Ajustes de aparência e idioma
  Como visitante do site
  Quero trocar o tema e o idioma da interface
  Para jogar do jeito que me for mais confortável

  Contexto:
    Dado que estou na página inicial do StreakBasket
    E abro o menu de ajustes

  @tema
  Cenário: Aplicar o tema escuro
    Quando escolho o tema "ESCURO"
    Então o fundo da página deve ficar escuro

  @tema
  Cenário: Manter o tema escolhido depois de recarregar a página
    Quando escolho o tema "ESCURO"
    E recarrego a página
    Então o fundo da página deve ficar escuro

  @tema
  Cenário: Voltar ao tema claro
    Quando escolho o tema "ESCURO"
    E escolho o tema "CLARO"
    Então o fundo da página deve ficar claro

  @idioma
  Esquema do Cenário: Traduzir a interface para <idioma>
    Quando escolho o idioma "<idioma>"
    Então devo ver o botão "<escolher modo>"
    E devo ver o botão "<como jogar>"

    Exemplos:
      | idioma | escolher modo | como jogar  |
      | EN     | CHOOSE MODE   | HOW TO PLAY |
      | ES     | ELEGIR MODO   | CÓMO JUGAR  |

  @idioma
  Cenário: Voltar ao português depois de trocar o idioma
    Quando escolho o idioma "EN"
    E escolho o idioma "PT"
    Então devo ver o botão "ESCOLHER MODO"
    E devo ver o botão "COMO JOGAR"
