#language: pt
@multiplayer
Funcionalidade: Preparação de partidas multiplayer
  Como jogador
  Quero configurar uma sala para jogar com meus amigos
  Para disputar um torneio entre nós

  # Nenhum cenário cria sala: todos param antes de "CRIAR SALA".

  Contexto:
    Dado que estou na página inicial do StreakBasket
    E escolho o modo "MULTIPLAYER"

  Cenário: Exibir as opções de entrada no multiplayer
    Então devo ver o texto "JOGUE COM AMIGOS."
    E o campo de nome na sala deve aceitar até 24 caracteres
    E o campo de código da sala deve aceitar até 16 caracteres
    E devo ver o botão "CRIAR UMA SALA"
    E devo ver o botão para entrar na sala

  Cenário: Exibir a configuração padrão de uma nova sala
    Quando abro a criação de sala
    Então devo ver as seguintes opções selecionadas:
      | grupo                 | opcao           |
      | MODO DE JOGO          | TEMPORADA ATUAL |
      | NÚMERO DE JOGADORES   | 4               |
      | ORDEM DO DRAFT        | ALTERNADO       |
      | TEMPO POR ESCOLHA     | 10s             |
      | PICKS DOS ADVERSÁRIOS | ANÔNIMOS        |
    E devo ver o botão "CRIAR SALA"

  Esquema do Cenário: Alterar "<grupo>" da sala para <opcao>
    Quando abro a criação de sala
    E escolho "<opcao>" em "<grupo>"
    Então somente "<opcao>" deve estar selecionada em "<grupo>"

    Exemplos:
      | grupo                 | opcao        |
      | MODO DE JOGO          | ALL-STAR     |
      | NÚMERO DE JOGADORES   | 8            |
      | NÚMERO DE JOGADORES   | 16           |
      | ORDEM DO DRAFT        | EM SEQUÊNCIA |
      | TEMPO POR ESCOLHA     | 30s          |
      | PICKS DOS ADVERSÁRIOS | VISÍVEIS     |

  Cenário: Voltar da criação de sala para as opções
    Quando abro a criação de sala
    E volto às opções do multiplayer
    Então devo ver o texto "SELECIONE UMA OPÇÃO"
    E devo ver o botão "CRIAR UMA SALA"
