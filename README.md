# WePayU

Sistema de folha de pagamento desenvolvido para a disciplina de **Programação Orientada a Objetos (Programação 2)** do Instituto de Computação da UFAL, em Java.

O sistema gerencia empregados de uma empresa (horistas, assalariados e comissionados), registra cartões de ponto, vendas e taxas sindicais, e calcula/gera a folha de pagamento de acordo com a agenda de pagamento de cada empregado. Toda a interação acontece pela classe `Facade`, que é exercitada pelos testes de aceitação do **EasyAccept**.

## O que o sistema faz

Cada funcionalidade corresponde a uma user story (US) da especificação. Todas as entradas chegam como texto pela `Facade`, são validadas e, em caso de erro, geram uma exceção com a mensagem exata esperada pelos testes.

### US1 – Adição de empregados

- `criarEmpregado(nome, endereco, tipo, salario)` para **horista** e **assalariado**.
- `criarEmpregado(nome, endereco, tipo, salario, comissao)` para **comissionado**.
- Cada empregado recebe um id gerado pelo sistema (sequencial), que é devolvido pela operação.
- Para o horista, `salario` é o valor da hora. Para o assalariado e o comissionado, é o salário mensal. A comissão é um percentual (ex.: `0,05`).
- Números aceitam vírgula decimal e saem formatados com duas casas (`1300,00`).
- Validações: nome, endereço, salário e comissão não podem ser vazios; salário e comissão devem ser numéricos e não negativos; o tipo precisa existir e ser compatível com os parâmetros (passar comissão para horista gera "Tipo nao aplicavel.").
- Consultas: `getAtributoEmpregado(emp, atributo)`, `getEmpregadoPorNome(nome, indice)` (o i-ésimo empregado com aquele nome) e `getNumeroDeEmpregados()`.

### US2 – Remoção de empregados

- `removerEmpregado(emp)` remove o empregado pelo id.
- Erros para id vazio ("Identificacao do empregado nao pode ser nula.") ou inexistente ("Empregado nao existe.").

### US3 – Cartão de ponto

- `lancaCartao(emp, data, horas)` registra as horas de um dia, apenas para **horistas**.
- Até 8 horas por dia contam como horas normais; o que passar disso é hora extra.
- `getHorasNormaisTrabalhadas` e `getHorasExtrasTrabalhadas` somam as horas entre `dataInicial` (inclusiva) e `dataFinal` (exclusiva).
- Valida datas (formato `d/M/aaaa`, data inicial não posterior à final) e horas positivas.

### US4 – Resultado de venda

- `lancaVenda(emp, data, valor)` registra uma venda, apenas para **comissionados**.
- `getVendasRealizadas(emp, dataInicial, dataFinal)` soma as vendas do período.
- O valor deve ser positivo.

### US5 – Taxa de serviço do sindicato

- `lancaTaxaServico(membro, data, valor)` lança uma taxa de serviço para um membro do sindicato, identificado pelo **id no sindicato** (não pelo id do empregado).
- `getTaxasServico(emp, dataInicial, dataFinal)` soma as taxas do período.
- Erros para membro inexistente, empregado não sindicalizado e valor não positivo.

### US6 – Alteração de detalhes do empregado

`alteraEmpregado` permite mudar:

- `nome`, `endereco`, `salario` e `comissao` (esta só para comissionados);
- `tipo` (horista, assalariado ou comissionado, informando o novo salário ou a comissão quando necessário). O empregado mantém o id, o sindicato, o método de pagamento e o histórico;
- `metodoPagamento`: `emMaos`, `correios` ou `banco` (com banco, agência e conta corrente);
- `sindicalizado`: `true` (com `idSindicato` e `taxaSindical`) ou `false`. Dois empregados não podem ter o mesmo id de sindicato;
- `agendaPagamento`: qualquer agenda cadastrada (ver US9/US10).

Cada alteração tem suas validações (valores nulos, não numéricos, negativos, atributo inexistente, atributo que não se aplica ao tipo).

### US7 – Folha de pagamento

- `totalFolha(data)` devolve o total bruto da folha naquela data, sem alterar nada.
- `rodaFolha(data, saida)` gera o arquivo da folha e registra o pagamento.
- Só entram na folha os empregados cujo dia de pagamento cai na data (segundo a agenda de cada um).
- Cálculo do salário bruto:
  - **Horista**: horas normais × valor da hora + horas extras × valor da hora × 1,5, no período desde o último pagamento.
  - **Assalariado**: salário mensal (ou proporcional, se estiver numa agenda semanal).
  - **Comissionado**: parte fixa proporcional à agenda (em agendas semanais, salário × 12 × semanas / 52) + comissão sobre as vendas do período.
- Descontos para sindicalizados: taxa sindical diária × dias do período + taxas de serviço do período.
- Se os descontos forem maiores que o salário bruto, o líquido fica zero e o restante vira **dívida**, cobrada no próximo pagamento.
- O arquivo segue o layout dos arquivos esperados em `ok/`: seções de horistas, assalariados e comissionados, ordenadas por nome, com totais por seção e total geral. A coluna "Metodo" mostra "Em maos", "Correios, <endereço>" ou "<banco>, Ag. <agência> CC <conta>".

### US8 – Undo/redo

- `undo()` desfaz o último comando que alterou o sistema e `redo()` refaz o último desfeito.
- Um novo comando limpa a pilha de redo.
- Erros "Nao ha comando a desfazer." e "Nao ha comando a refazer.".
- Depois de `encerrarSistema()`, qualquer comando gera "Nao pode dar comandos depois de encerrarSistema.".

### US9 – Agenda de pagamento

- Cada empregado tem uma agenda. As padrões são:
  - horista: `semanal 5` (toda sexta-feira);
  - assalariado: `mensal $` (último dia útil do mês);
  - comissionado: `semanal 2 5` (sexta-feira, a cada duas semanas).
- A agenda pode ser trocada com `alteraEmpregado(emp, "agendaPagamento", descricao)`, desde que esteja cadastrada.

### US10 – Criação de agendas de pagamento

- `criarAgendaDePagamentos(descricao)` cadastra uma nova agenda. Formatos aceitos:
  - `mensal N`: todo dia N do mês (1 a 28);
  - `mensal $`: último dia útil do mês;
  - `semanal D`: toda semana no dia D (1 = segunda ... 7 = domingo);
  - `semanal S D`: a cada S semanas (1 a 52) no dia D.
- Erros "Descricao de agenda invalida" e "Agenda de pagamentos ja existe".

### Operações do sistema

- `zerarSistema()` apaga todos os dados.
- `encerrarSistema()` grava o estado em `dados.xml`. Na próxima execução, a `Facade` carrega esse arquivo e continua de onde parou.

## Como rodar

Requisitos: **JDK 11 ou superior** e o `lib/easyaccept.jar` (já incluído no repositório).

### Pelo IntelliJ

1. Abra a pasta do projeto no IntelliJ.
2. Adicione `lib/easyaccept.jar` como biblioteca do módulo (File → Project Structure → Libraries), se ainda não estiver.
3. Rode a classe `Main` (`src/Main.java`). Ela executa, em ordem, todos os scripts de `tests/us1.txt` até `tests/us10_1.txt`.

O diretório de trabalho deve ser a raiz do projeto, porque os testes usam caminhos relativos (`tests/...`, `ok/...`).

### Pela linha de comando

Na raiz do projeto (Windows, PowerShell ou cmd):

```bat
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out -cp lib\easyaccept.jar @fontes.txt
java -cp "out;lib\easyaccept.jar" Main
```

Linux/macOS:

```sh
find src -name "*.java" > fontes.txt
javac -encoding UTF-8 -d out -cp lib/easyaccept.jar @fontes.txt
java -cp "out:lib/easyaccept.jar" Main
```

Cada script do EasyAccept imprime quantos testes passaram. As folhas geradas pelo `rodaFolha` (`folha-AAAA-MM-DD.txt`) são comparadas com os arquivos esperados da pasta `ok/`.

> O estado do sistema é salvo em `dados.xml` ao chamar `encerrarSistema`. Esse arquivo e as folhas geradas na raiz estão no `.gitignore`.

## Estrutura de pacotes

```
src/
├── Main.java                         # roda todos os testes do EasyAccept
└── br/ufal/ic/p2/wepayu/
    ├── Facade.java                   # única porta de entrada usada pelos testes
    ├── services/                     # regras de aplicação (casos de uso)
    │   ├── Sistema.java              # estado, undo/redo e encerramento
    │   ├── Comando / ComandoComRetorno
    │   ├── EmpregadoFactory.java     # cria o subtipo certo de Empregado
    │   ├── GerenciaEmpregado / GerenciaAlteracao
    │   ├── GerenciaCartaoDePonto / GerenciaVendas / GerenciaSindicato
    │   ├── GerenciaAgenda / GerenciaFolha
    ├── models/                       # domínio
    │   ├── Empregado (abstrata) → Horista, Assalariado, Comissionado
    │   ├── Filiacao (abstrata)  → NaoSindicalizado, MembroSindicato
    │   ├── MetodoPagamento (abstrata) → EmMaos, Correios, Banco
    │   ├── Historico, Periodo
    │   ├── Lancamento → CartaoDePonto, LancamentoMonetario → Venda, TaxaServico
    │   ├── agenda/                   # AgendaPagamento (interface) e implementações
    │   └── folha/                    # Contracheque e RelatorioFolha
    ├── conversao/                    # conversão/formatação de números, datas e booleanos
    ├── persistencia/                 # DadosSistema e Persistencia (XML)
    └── exception/                    # hierarquia de exceções a partir de WePayUException
```

## Principais decisões de design

### Fachada fina

A `Facade` não tem regra de negócio: ela só repassa cada chamada para o serviço responsável (`GerenciaEmpregado`, `GerenciaFolha`, etc.). Toda operação que altera o estado passa por `Sistema.executar(...)`, o que centraliza o controle de undo/redo e de sistema encerrado.

### Polimorfismo em vez de checagem de tipo

Cada comportamento que depende do tipo fica na própria classe:

- `Empregado` é abstrata e cada subtipo implementa `getTipo()`, `agendaPadrao()` e `criarContracheque(Periodo)`. O cálculo do salário bruto de horista, assalariado e comissionado fica em cada subclasse.
- Operações que só valem para um tipo usam métodos sobrescritos: `Empregado.comoHorista()` e `comoComissionado()` lançam exceção por padrão, e só `Horista`/`Comissionado` sobrescrevem retornando `this`. Assim, lançar um cartão para um assalariado gera "Empregado nao eh horista." sem nenhum `if` de tipo.
- `getAtributo("comissao")` é sobrescrito apenas em `Comissionado`.
- `MetodoPagamento` (`EmMaos`, `Correios`, `Banco`) sabe descrever a si mesmo na folha e só `Banco` responde `banco`, `agencia` e `contaCorrente`.
- Na folha, cada `Contracheque` se adiciona ao `RelatorioFolha` pelo método `adicionarEm(relatorio)` (despacho duplo), e o relatório tem uma sobrecarga de `adicionar` para cada tipo de contracheque. É assim que cada empregado vai para a seção certa da folha sem checagem de tipo.

### Null Object na filiação sindical

Em vez de guardar `null` quando o empregado não é sindicalizado, ele tem uma `Filiacao` do tipo `NaoSindicalizado`. Ela responde `ehSindicalizado() = false`, desconto zero, e lança `EmpregadoNaoEhSindicalizadoException` quando alguém pede dados sindicais. `MembroSindicato` guarda o id, a taxa sindical e as taxas de serviço. Isso elimina verificações de `null` espalhadas pelo código.

### Agendas de pagamento por interface

`AgendaPagamento` é uma interface com quatro responsabilidades: descrever a agenda, dizer se uma data é dia de pagamento, calcular o período pago e calcular o salário proporcional. As implementações são:

- `AgendaSemanal` (`semanal 5`, `semanal 2 5`, ...)
- `AgendaMensal` (`mensal $`, último dia útil do mês)
- `AgendaMensalDiaFixo` (`mensal 15`, ...)

`FabricaAgenda` interpreta o texto da descrição e devolve a implementação certa. Cada tipo de empregado tem sua agenda padrão (`agendaPadrao()`), e novas agendas podem ser criadas na US10 sem mudar o código das classes de empregado.

### Undo/redo com Memento

O `Sistema` guarda snapshots do estado (`DadosSistema`) serializados em XML antes de cada comando. Desfazer restaura o snapshot anterior e empilha o atual na pilha de refazer, e vice-versa. Se um comando falhar com `WePayUException`, o estado anterior é restaurado, então uma operação nunca fica pela metade. Os comandos são passados como lambdas das interfaces funcionais `Comando` e `ComandoComRetorno`.

### Persistência em XML

`Persistencia` usa `XMLEncoder`/`XMLDecoder` com `PersistenceDelegate`s próprios, para que as classes do domínio possam ter atributos `final` e construtores com validação (sem precisar de setters públicos para tudo). `BigDecimal` e `LocalDate` são gravados como texto, e as agendas são gravadas pela descrição e recriadas pela `FabricaAgenda`. O mesmo mecanismo serve para salvar em `dados.xml` e para os snapshots do undo/redo.

### Valores monetários com `BigDecimal`

Salários, comissões, vendas e taxas usam `BigDecimal` para evitar erros de arredondamento de `double`. A formatação (vírgula decimal, duas casas) fica concentrada em `ConversorNumero`.

### Exceções organizadas por categoria

Todas as exceções herdam de `WePayUException` e ficam em subpacotes por categoria (`nulo`, `numerico`, `negativo`, `naoencontrado`, `tipo`, `data`, `agenda`, `sindicato`, `pagamento`, `sistema`, `persistencia`). Cada exceção já carrega a mensagem exata esperada pelos testes.

## Autor

Arthur, para a disciplina de Programação 2 (POO), IC/UFAL.
