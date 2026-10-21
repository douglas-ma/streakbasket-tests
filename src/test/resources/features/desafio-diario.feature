#language: pt
@desafio-diario
Funcionalidade: Desafio diário
  Como jogador
  Quero descobrir o time e a temporada do dia a partir de pistas
  Para manter minha sequência de acertos

  # Os cenários não enviam palpites, para não gastar as tentativas do dia.

  Contexto:
    Dado que estou na página inicial do StreakBasket
    E abro o desafio diário

  Cenário: Começar o desafio com um jogador revelado
    Então devo ver o texto "QUAL É O TIME E A TEMPORADA?"
    E devo ver 1 jogador revelado e 4 ocultos
    E devo ver o texto "1 de 5 jogadores revelados"
    E o botão de palpite deve indicar "5 RESTANTES"

  Cenário: Não registrar palpite com os campos vazios
    Quando confirmo o palpite sem preencher os campos
    Então o botão de palpite deve indicar "5 RESTANTES"
    E devo ver o texto "Seu primeiro palpite aparecerá aqui."

  Esquema do Cenário: Sugerir franquias ao buscar por "<busca>"
    Quando digito "<busca>" no campo de time do palpite
    Então devo ver a sugestão "<franquia>"

    Exemplos:
      | busca  | franquia                 |
      | Bos    | BOS - Boston Celtics     |
      | Lakers | LAL - Los Angeles Lakers |
      | MIA    | MIA - Miami Heat         |

  Cenário: Aceitar no máximo quatro dígitos na temporada
    Então o campo de temporada deve aceitar no máximo 4 caracteres

  Cenário: Consultar as estatísticas do jogador revelado
    Quando abro as estatísticas do jogador revelado
    Então a janela deve conter "Time e temporada serão revelados ao terminar o desafio."
    E a janela deve conter as estatísticas "PTS, REB, AST, STL, BLK"
    Quando fecho as estatísticas do jogador
    Então as estatísticas devem estar fechadas

  Cenário: Consultar as regras do desafio diário
    Quando abro as instruções do desafio diário
    Então a janela deve conter "Verde: resposta certa."
    E a janela deve conter "Amarelo: temporada com diferença de até cinco anos."
    Quando fecho as instruções do desafio diário
    Então as instruções do desafio diário devem estar fechadas
