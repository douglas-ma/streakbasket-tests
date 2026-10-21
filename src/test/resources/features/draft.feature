#language: pt
@draft
Funcionalidade: Draft e simulação no Modo Streak
  Como jogador
  Quero sortear franquias e escalar um quinteto
  Para disputar partidas simuladas

  Contexto:
    Dado que estou na página inicial do StreakBasket
    E escolho o modo "MODO STREAK"

  Cenário: Exigir modo de jogo e dificuldade antes de buscar a franquia
    Então nenhuma opção deve estar selecionada em "MODO DE JOGO"
    E nenhuma opção deve estar selecionada em "DIFICULDADE"
    E o botão "CONFIRMAR E BUSCAR FRANQUIA" deve estar desabilitado
    Quando escolho "TEMPORADA ATUAL" em "MODO DE JOGO"
    Então o botão "CONFIRMAR E BUSCAR FRANQUIA" deve estar desabilitado
    Quando escolho "ROOKIE" em "DIFICULDADE"
    Então o botão "CONFIRMAR E BUSCAR FRANQUIA" deve estar habilitado

  Esquema do Cenário: Destacar a opção <opcao> em <grupo>
    Quando escolho "<opcao>" em "<grupo>"
    Então somente "<opcao>" deve estar selecionada em "<grupo>"

    Exemplos:
      | grupo        | opcao           |
      | MODO DE JOGO | TEMPORADA ATUAL |
      | MODO DE JOGO | ALL-STAR        |
      | DIFICULDADE  | ROOKIE          |
      | DIFICULDADE  | ALL-STAR        |
      | DIFICULDADE  | HOF             |

  Cenário: Sortear a primeira franquia
    Quando preparo o draft em "TEMPORADA ATUAL" na dificuldade "ROOKIE"
    Então devo ver o elenco de uma franquia sorteada
    E devo ver o texto "SORTEIOS RESTANTES: 3/3"
    E devo ver o texto "DIFICULDADE · Rookie (Fácil)"

  Cenário: Sortear outro time gasta um sorteio
    Quando preparo o draft em "TEMPORADA ATUAL" na dificuldade "ROOKIE"
    E sorteio outro time
    Então devo ver o elenco de uma franquia sorteada
    E devo ver o texto "SORTEIOS RESTANTES: 2/3"

  Cenário: Escalar o primeiro jogador do quinteto
    Quando preparo o draft em "TEMPORADA ATUAL" na dificuldade "ROOKIE"
    E escalo o primeiro jogador do elenco sorteado
    Então o jogador escalado deve ocupar sua posição na quadra
    E devo ver o texto "1/5"
    E devem restar 4 posições vazias

  @e2e
  Cenário: Montar o quinteto completo e simular uma partida
    Quando preparo o draft em "TEMPORADA ATUAL" na dificuldade "ROOKIE"
    E monto o quinteto completo
    Então devo ver o texto "EQUIPE FECHADA!"
    E devo ver o texto "5/5"
    Quando busco um adversário
    E simulo a partida
    Então devo ver o placar final com os quatro quartos
