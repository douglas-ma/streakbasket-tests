#language: pt
@informacoes
Funcionalidade: Informações do projeto e ranking pessoal
  Como visitante do site
  Quero saber quem criou o jogo e consultar meus recordes
  Para acompanhar minha evolução

  Contexto:
    Dado que estou na página inicial do StreakBasket

  @sobre
  Cenário: Consultar as informações sobre o projeto
    Quando abro as informações sobre o projeto
    Então a janela deve conter "Desenvolvido em 2026 por Douglas Moura"
    E devo ver o link "GitHub de Douglas Moura"
    E devo ver o link "LinkedIn de Douglas Moura"

  @sobre
  Cenário: Fechar as informações sobre o projeto
    Quando abro as informações sobre o projeto
    E clico em "Entendi" na janela
    Então nenhuma janela deve estar aberta

  @meu-ranking
  Esquema do Cenário: Consultar o ranking pessoal sem campanhas salvas em <modo>
    Quando abro meu ranking
    E clico em "<modo>" na janela
    Então a janela deve conter "<mensagem>"

    Exemplos:
      | modo            | mensagem                               |
      | TEMPORADA ATUAL | Nenhum recorde salvo neste modo ainda. |
      | ALL-STAR        | Nenhum recorde salvo neste modo ainda. |
      | TEMPORADA NBA   | Nenhuma temporada concluída ainda.     |

  @meu-ranking
  Cenário: Fechar o ranking pessoal
    Quando abro meu ranking
    E clico em "Fechar" na janela
    Então nenhuma janela deve estar aberta
