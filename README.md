# Testes básicos do StreakBasket

Exemplos de testes de interface para <https://streakbasket.vercel.app>, usando **Java 21, Maven, Selenium WebDriver, Cucumber e Gherkin em português**.

## Como as ferramentas se conectam

1. **Gherkin** descreve o comportamento esperado nos arquivos `.feature`, com `Dado`, `Quando` e `Então`.
2. **Cucumber** encontra o método Java correspondente a cada frase do cenário.
3. **Selenium** abre o Chrome e interage com a página por meio desses métodos.
4. **JUnit Platform** executa a suíte `CucumberTest`; o **Maven** compila, roda os testes e gera os resultados.

O Spring Boot já fazia parte do projeto. Os cenários do site não precisam subir uma aplicação Spring nem um servidor local. O teste original `TestsApplicationTests` foi mantido.

## Pré-requisitos

- **JDK 21**, com `JAVA_HOME` apontando para essa instalação.
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

## Cenários incluídos

| Tag | Comportamento verificado |
| --- | --- |
| `@inicio` | Título, marca e botões principais da página inicial |
| `@ajuda` | Abertura das instruções e conteúdo das três etapas do jogo |
| `@ajuda` | Fechamento das instruções pelo botão ENTENDI |
| `@modos` | Preparação do Modo Streak com cinco posições vazias |
| `@modos` | Preparação da Temporada NBA com cinco posições vazias |
| `@perfil` | Formulário com e-mail e senha vazios, e senha do tipo `password` |
| `@ranking` | Painel do ranking e opções de modo e dificuldade |

São **sete execuções de cenários**: o `Esquema do Cenário` é executado uma vez por linha da tabela `Exemplos`. Todos também têm a tag `@smoke`.

Validação realizada no site com JDK 21 e Chrome headless: **7 cenários Cucumber passaram**. Incluindo o teste Spring original, o Maven terminou com **8 testes, 0 falhas e 0 erros**. A correspondência dos passos também foi conferida com dry-run.

O teste de perfil apenas abre e verifica o formulário. Os exemplos não enviam credenciais nem criam contas, salas ou partidas no servidor. Os testes de modo param na preparação do draft, antes da busca de franquias. O ranking verifica a interface; não verifica os resultados retornados pelo backend.

## Arquivos principais

```text
src/test/
├── java/com/streakbasket/tests/
│   ├── CucumberTest.java                 # Suíte JUnit que descobre o Gherkin
│   ├── TestsApplicationTests.java        # Teste Spring original
│   ├── pages/StreakBasketPage.java       # Seletores, cliques e esperas Selenium
│   ├── steps/StreakBasketSteps.java      # Passos Gherkin, hooks e assertivas
│   └── support/BrowserFactory.java       # Criação e configuração do Chrome
└── resources/
    ├── features/streakbasket.feature     # Cenários em português
    └── junit-platform.properties        # Relatórios e execução do Cucumber
```

Os seletores priorizam `aria-label`, texto dos botões e o papel `dialog`; evitam as classes CSS geradas pelo site. As esperas são explícitas (`WebDriverWait`), sem `Thread.sleep`. Cada cenário abre um Chrome com perfil temporário e o fecha ao terminar, isolando idioma, login e progresso entre testes. O navegador solicita português do Brasil para estabilizar os textos verificados.

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

1. Acrescente o comportamento em `streakbasket.feature`.
2. Reaproveite os passos existentes ou crie métodos anotados com `@Dado`, `@Quando` e `@Entao` em `StreakBasketSteps`.
3. Coloque novos seletores e ações em `StreakBasketPage`.
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
