# Testes de interface do StreakBasket

Exemplos de testes de interface para <https://streakbasket.vercel.app>, usando **Java 21, Maven, Selenium WebDriver, Cucumber e Gherkin em português**.

## Como as ferramentas se conectam

1. **Gherkin** descreve o comportamento esperado nos arquivos `.feature`, com `Dado`, `Quando` e `Então`.
2. **Cucumber** encontra o método Java correspondente a cada frase do cenário.
3. **Selenium** abre o Chrome e interage com a página por meio desses métodos.
4. **JUnit Platform** executa a suíte `CucumberTest`; o **Maven** compila, roda os testes e gera os resultados.

O Spring Boot já fazia parte do projeto. Os cenários do site não precisam subir uma aplicação Spring nem um servidor local. O teste original `TestsApplicationTests` foi mantido.

## Pré-requisitos

- **JDK 21 ou mais recente**, com `JAVA_HOME` apontando para essa instalação. O código é compilado para Java 21; a suíte também foi executada com JDK 27.
- Google Chrome instalado.
- Internet para acessar o site e, na primeira execução, baixar Maven, dependências e ChromeDriver.

O projeto compila para Java 21. Não exige Java 25. O Maven Wrapper (`mvnw.cmd` / `mvnw`) já está incluído, portanto não é necessário instalar Maven separadamente. O Selenium Manager, incluído no Selenium, resolve o ChromeDriver automaticamente.

Confira no PowerShell:

```powershell
java -version
./mvnw.cmd -version
```

Ambos devem indicar Java 21. Se houver outro Java no `PATH`, ajuste **nesta sessão do PowerShell**. Exemplo com a instalação encontrada nesta máquina:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot'
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
```

Em outra máquina, substitua o caminho pela pasta do JDK 21 instalado. Na IDE, selecione também JDK 21 para o projeto e para a execução do Maven.

## Executar

Na raiz do projeto:

```powershell
./mvnw.cmd clean test
```

O `clean` recompila tudo, inclusive se houver arquivos antigos compilados com outra versão do Java. Por padrão o Chrome roda em **headless**, sem abrir uma janela. Para acompanhar o navegador:

```powershell
./mvnw.cmd test '-Dheadless=false'
```

Para executar somente Cucumber, sem o teste original de contexto Spring:

```powershell
./mvnw.cmd test '-Dtest=CucumberTest'
```

Para executar somente os cenários de ajuda:

```powershell
./mvnw.cmd test '-Dtest=CucumberTest' '-Dcucumber.filter.tags=@ajuda'
```

Para conferir a sintaxe dos cenários e a correspondência dos passos **sem abrir o navegador**:

```powershell
./mvnw.cmd test '-Dtest=CucumberTest' '-Dcucumber.execution.dry-run=true'
```

O dry-run não valida o funcionamento do site. Para isso, execute os testes normalmente.

No Linux/macOS, use `./mvnw` no lugar de `./mvnw.cmd`.

Os exemplos acima são para o **PowerShell**, onde as aspas simples evitam que `-D...=...` seja interpretado. No **Prompt de Comando (cmd.exe)** as aspas simples não funcionam: o Maven recebe o texto com as aspas e falha com `Unknown lifecycle phase "'-Dheadless=false'"`. No cmd, use aspas duplas ou nenhuma:

```bat
mvnw.cmd test -Dheadless=false
mvnw.cmd test -Dtest=CucumberTest "-Dcucumber.filter.tags=@draft and not @e2e"
```

## Cenários incluídos

Os cenários estão em `src/test/resources/features/`, um arquivo por área do site. Cada linha de `Exemplos` de um `Esquema do Cenário` é uma execução separada.

| Arquivo / tag | Comportamento verificado | Execuções |
| --- | --- | --- |
| `streakbasket.feature` · `@smoke` | Página inicial, instruções, preparação dos modos Streak e Temporada NBA, formulário de perfil e ranking geral | 7 |
| `ajustes.feature` · `@ajustes` | Tema escuro, tema mantido após recarregar, volta ao claro; tradução para EN e ES e volta ao PT | 6 |
| `informacoes.feature` · `@informacoes` | Janela "Sobre o projeto" (autor e links) e fechamento; "Meu ranking" vazio em cada modo e fechamento | 6 |
| `draft.feature` · `@draft` | Botão de busca bloqueado até escolher modo e dificuldade; destaque da opção escolhida; sorteio da franquia; "Sortear outro time" gasta um sorteio; escalação do primeiro jogador | 9 |
| `draft.feature` · `@e2e` | Fluxo completo: monta o quinteto, busca adversário, simula a partida e confere o placar dos quatro quartos | 1 |
| `desafio-diario.feature` · `@desafio-diario` | 1 jogador revelado e 4 ocultos; palpite vazio não gasta tentativa; sugestões de franquia ao digitar; limite de 4 dígitos na temporada; estatísticas do jogador revelado; regras do desafio | 8 |
| `multiplayer.feature` · `@multiplayer` | Campos de nome e código; configuração padrão da sala; troca de cada opção da sala; volta às opções | 9 |
| `franquias.feature` · `@franquias` | Catálogo `/teams`; navegação franquia → temporada → elenco; elenco acessado direto pelo endereço; estatísticas de um jogador; trilha de navegação | 6 |
| `perfil.feature` · `@perfil` | Mensagens de validação ao entrar e ao criar conta; troca de aba; fechamento do perfil | 7 |

Para rodar uma área, filtre pela tag, por exemplo `'-Dcucumber.filter.tags=@draft and not @e2e'`.

Validação realizada no site em 21/10/2026, com JDK 27 e Chrome 155 headless: **59 cenários Cucumber passaram** (cerca de 9 minutos). Incluindo o teste Spring original, o Maven terminou com **60 testes, 0 falhas e 0 erros**.

### O que os testes enviam ao servidor

- **Perfil:** só senhas com menos de 6 caracteres (8 para criar conta), que o próprio formulário recusa antes de enviar. Um e-mail malformado com senha longa, como `jogador@` / `123456`, passa pela validação local e vai para a autenticação; por isso não entra nos exemplos.
- **Desafio diário:** nenhum palpite é confirmado, para não gastar as tentativas do dia. O cenário "palpite vazio" verifica justamente que nada é registrado.
- **Multiplayer:** nenhuma sala é criada nem procurada; os cenários param antes de "CRIAR SALA".
- **Draft e simulação:** rodam no navegador; o progresso fica no `localStorage` do perfil temporário, descartado ao fim de cada cenário. O cenário `@e2e` depende de sorteios aleatórios e leva cerca de 40 s.
- **Rankings:** a interface é verificada, não os resultados vindos do backend.

## Arquivos principais

```text
src/test/
├── java/com/streakbasket/tests/
│   ├── CucumberTest.java                 # Suíte JUnit que descobre o Gherkin
│   ├── TestsApplicationTests.java        # Teste Spring original
│   ├── pages/                            # Page Objects: seletores, cliques e esperas Selenium
│   │   ├── BasePage.java                 # Esperas, janelas, grupos de opções e seletores comuns
│   │   ├── StreakBasketPage.java         # Página inicial, instruções, ranking, perfil e "Sobre"
│   │   ├── AjustesPage.java              # Tema e idioma
│   │   ├── DraftPage.java                # Sorteio, escalação e simulação do Modo Streak
│   │   ├── DesafioDiarioPage.java        # Desafio diário
│   │   ├── MultiplayerPage.java          # Entrada e criação de sala
│   │   ├── FranquiasPage.java            # Guia de elencos (/teams)
│   │   └── PerfilPage.java               # Validação do formulário de acesso
│   ├── steps/                            # Passos Gherkin e assertivas, um arquivo por área
│   │   ├── Hooks.java                    # Abre e fecha o Chrome; captura de tela nas falhas
│   │   ├── ComunsSteps.java              # Textos, janelas, endereço e opções selecionadas
│   │   └── ...Steps.java
│   └── support/
│       ├── BrowserFactory.java           # Criação e configuração do Chrome
│       └── Contexto.java                 # Navegador e páginas do cenário (PicoContainer)
└── resources/
    ├── features/*.feature                # Cenários em português
    └── junit-platform.properties        # Relatórios e execução do Cucumber
```

Os seletores priorizam `aria-label`, texto dos botões e o papel `dialog`; evitam as classes CSS geradas pelo site. As esperas são explícitas (`WebDriverWait`), sem `Thread.sleep`. Cada cenário abre um Chrome com perfil temporário e o fecha ao terminar, isolando idioma, tema, login e progresso entre testes. O navegador solicita português do Brasil para estabilizar os textos verificados.

O `cucumber-picocontainer` cria um `Contexto` por cenário e o entrega pelo construtor a `Hooks` e a todas as classes de passos. Assim, passos de arquivos diferentes usam o mesmo navegador.

Detalhes do site que os Page Objects tratam:

- O site é feito com React Native Web. As telas internas (draft, desafio, multiplayer) também são `role="dialog"`; por isso `BasePage.ultimoModal()` usa a janela visível aberta por último.
- Alguns controles são `div[tabindex="0"]` e não `button` (`INICIAR PARTIDA`, `BUSCAR ADVERSÁRIO`, `Sortear Outro Time`); `controleComTexto` os localiza.
- A opção escolhida em um grupo (modo, dificuldade, número de jogadores...) só é indicada pela cor de fundo. `opcoesSelecionadas` compara cada opção com o fundo neutro dos botões do cabeçalho, sem fixar cores no código.
- O draft sorteia franquias aleatórias. Os passos não esperam um time específico: escalam o jogador que couber em uma posição vazia.

## Relatórios

Após uma execução normal:

- `target/cucumber-reports/cucumber.html`: relatório para abrir no navegador.
- `target/cucumber-reports/cucumber.json`: resultado para outras ferramentas.
- `target/cucumber-reports/cucumber.xml`: resultado JUnit para integração contínua.
- `target/surefire-reports/`: resultados do Maven/JUnit.
- `target/screenshots/`: capturas dos cenários que falharem; também são anexadas ao relatório Cucumber.

Os relatórios são locais. A publicação de resultados no serviço do Cucumber está desativada. O Jackson, com escopo de teste no `pom.xml`, permite que o Cucumber 8 escreva os relatórios HTML e JSON.

## Configurações opcionais

| Propriedade | Padrão | Finalidade |
| --- | --- | --- |
| `headless` | `true` | Usar `false` para exibir o Chrome |
| `base.url` | `https://streakbasket.vercel.app` | Endereço de outro ambiente do mesmo site |
| `timeout.seconds` | `20` | Limite de espera pelos elementos |

Exemplo de espera maior:

```powershell
./mvnw.cmd test '-Dtimeout.seconds=30'
```

## Adicionar um cenário

1. Acrescente o comportamento no `.feature` da área (ou crie um novo em `src/test/resources/features/`).
2. Reaproveite os passos existentes (veja `ComunsSteps`) ou crie métodos anotados com `@Dado`, `@Quando` e `@Entao` na classe de passos da área. Classes novas recebem o `Contexto` pelo construtor.
3. Coloque novos seletores e ações no Page Object da tela, estendendo `BasePage`; obtenha-o com `contexto.pagina(MinhaPage.class)`.
4. No passo `Então`, compare o resultado observado com o esperado usando as assertivas do JUnit.
5. Execute o cenário e confira o relatório. Mudanças no texto, idioma ou estrutura do site podem exigir atualização dos seletores e das expectativas.

Exemplo já incluído:

```gherkin
Cenário: Consultar as instruções do jogo
  Dado que estou na página inicial do StreakBasket
  Quando abro as instruções de como jogar
  Então devo ver as etapas de sorteio, escalação e simulação nas instruções
```

No arquivo real, o `Dado` compartilhado está no `Contexto`.

## Referências oficiais

- [Gherkin](https://cucumber.io/docs/gherkin/reference/)
- [Cucumber com JUnit Platform](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine)
- [Selenium Manager](https://www.selenium.dev/documentation/selenium_manager/)
