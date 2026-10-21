#language: pt
@perfil
Funcionalidade: Validação do acesso ao perfil
  Como visitante do site
  Quero ser avisado quando os dados de acesso estiverem inválidos
  Para corrigi-los antes de entrar

  # Apenas senhas com menos de 6 caracteres: o próprio formulário barra o envio e nada chega ao servidor.
  # Um e-mail malformado com senha longa (ex.: "jogador@" / "123456") passa pela validação local e é
  # enviado à autenticação, por isso não entra nestes exemplos.

  Contexto:
    Dado que estou na página inicial do StreakBasket
    E abro meu perfil

  Esquema do Cenário: Recusar dados de acesso inválidos (<caso>)
    Quando preencho o e-mail "<email>" e a senha "<senha>"
    E envio o formulário com "ENTRAR E SINCRONIZAR"
    Então a janela deve conter "Informe um e-mail válido e uma senha com pelo menos 6 caracteres."

    Exemplos:
      | caso                   | email               | senha |
      | campos vazios          |                     |       |
      | senha vazia            | jogador@exemplo.com |       |
      | senha curta            | jogador@exemplo.com | 123   |
      | e-mail sem arroba      | jogador.exemplo.com | 12345 |

  Cenário: Alternar para a criação de conta
    Quando escolho a aba "CRIAR CONTA" do perfil
    Então devo ver o botão de envio "CRIAR CONTA"

  Cenário: Recusar dados inválidos na criação de conta
    Quando escolho a aba "CRIAR CONTA" do perfil
    E preencho o e-mail "sem-arroba" e a senha "123"
    E envio o formulário com "CRIAR CONTA"
    Então a janela deve conter "Informe um e-mail válido e uma senha com pelo menos 8 caracteres."

  Cenário: Fechar o perfil
    Quando fecho o perfil
    Então o formulário de acesso deve estar fechado
