#language: pt
@smoke
Funcionalidade: Acesso às funcionalidades básicas do StreakBasket
  Como visitante do site
  Quero conhecer as opções e acessar as telas iniciais
  Para começar a jogar sem precisar de uma conta

  Contexto:
    Dado que estou na página inicial do StreakBasket

  @inicio
  Cenário: Exibir a página inicial
    Então devo ver o título "Streak Basket" no navegador
    E devo ver a marca "STREAK BASKET"
    E devo ver o botão "ESCOLHER MODO"
    E devo ver o botão "COMO JOGAR"

  @ajuda
  Cenário: Consultar as instruções do jogo
    Quando abro as instruções de como jogar
    Então devo ver as etapas de sorteio, escalação e simulação nas instruções

  @ajuda
  Cenário: Fechar as instruções e voltar à página inicial
    Quando abro as instruções de como jogar
    E confirmo que entendi as instruções
    Então as instruções devem ser fechadas
    E devo ver o botão "ESCOLHER MODO"

  @modos
  Esquema do Cenário: Abrir a preparação de um modo de jogo
    Quando escolho o modo "<modo>"
    Então devo ver a preparação "<preparacao>"
    E as cinco posições do quinteto devem estar vazias
    E devo ver o botão "CONFIRMAR E BUSCAR FRANQUIA"

    Exemplos:
      | modo          | preparacao            |
      | MODO STREAK   | PREPARE SEU DRAFT     |
      | TEMPORADA NBA | PREPARE SUA TEMPORADA |

  @perfil
  Cenário: Exibir o formulário de acesso ao perfil
    Quando abro meu perfil
    Então devo ver o formulário de acesso com e-mail e senha

  @ranking
  Cenário: Consultar a estrutura do ranking geral
    Quando abro o ranking geral
    Então devo ver o painel do ranking com filtros de modo e dificuldade
