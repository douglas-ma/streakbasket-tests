#language: pt
@franquias
Funcionalidade: Guia de elencos das franquias
  Como fã de basquete
  Quero consultar as franquias, suas temporadas e elencos
  Para planejar meu draft

  Cenário: Listar as franquias do catálogo
    Dado que estou na página inicial do StreakBasket
    Quando abro o guia de franquias
    Então devo estar no endereço "/teams"
    E devo ver pelo menos 30 franquias
    E o catálogo deve conter as franquias:
      | ATL - Atlanta Hawks         |
      | BOS - Boston Celtics        |
      | CHI - Chicago Bulls         |
      | GSW - Golden State Warriors |
      | LAL - Los Angeles Lakers    |
      | SAS - San Antonio Spurs     |

  Cenário: Navegar do catálogo até o elenco de uma temporada
    Dado que estou na página inicial do StreakBasket
    Quando abro o guia de franquias
    E abro a franquia "BOS - Boston Celtics"
    Então devo estar no endereço "/teams/BOS"
    E devo ver as temporadas "2026, 2008, 1986"
    Quando abro a temporada "2008"
    Então devo estar no endereço "/teams/BOS/2008"
    E devo ver o texto "BOS - Boston Celtics · 2008"
    E devo ver o texto "15 JOGADORES"

  Esquema do Cenário: Consultar o elenco de <franquia> em <temporada> pelo endereço
    Quando acesso o endereço "/teams/<franquia>/<temporada>"
    Então o elenco deve conter os jogadores:
      | nome        | posicao | overall |
      | <jogador 1> | <pos 1> | <ovr 1> |
      | <jogador 2> | <pos 2> | <ovr 2> |

    Exemplos:
      | franquia | temporada | jogador 1   | pos 1 | ovr 1 | jogador 2        | pos 2 | ovr 2 |
      | BOS      | 2008      | Paul Pierce | SF    | 90    | Kevin Garnett    | PF    | 94    |
      | LAL      | 2001      | Kobe Bryant | SG    | 95    | Shaquille O'Neal | C     | 96    |

  Cenário: Consultar as estatísticas de um jogador do elenco
    Quando acesso o endereço "/teams/BOS/2008"
    E abro as estatísticas de "Kevin Garnett"
    Então a janela deve conter "BOS - Boston Celtics · Temporada 2008"
    E a janela deve conter as estatísticas "PTS, REB, AST, STL, BLK"
    Quando fecho as estatísticas do elenco
    Então devo ver o texto "15 JOGADORES"

  Cenário: Voltar pela trilha de navegação
    Quando acesso o endereço "/teams/BOS/2008"
    E volto pela trilha para "BOS"
    Então devo estar no endereço "/teams/BOS"
    Quando volto pela trilha para "FRANQUIAS"
    Então devo estar no endereço "/teams"
    E devo ver pelo menos 30 franquias
