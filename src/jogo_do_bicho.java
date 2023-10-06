import java.util.Scanner;
import java.io.IOException;
import java.lang.Thread;
import java.awt.Desktop;
import java.net.URI;

public class jogo_do_bicho {
    // backup da tabela principal para comparar as posições com a original:
    public static final String[][] CARTELABACKUP = { { "1", "2", "3", "4", "5" }, { "6", "7", "8", "9", "10" },
            { "11", "12", "13", "14", "15" }, { "16", "17", "18", "19", "20" },
            { "21", "22", "23", "24", "25" } };

    // função para "limpar" o console;
    public static void limparConsole() throws IOException, InterruptedException {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
    }

    // simula o system("pause") do C/C++:
    public static void systemPause() throws Exception {
        System.out.print("\n> Digite qualquer tecla para voltar para o menu principal\n--> ");
        System.in.read();
        limparConsole();
    }

    // cria um cooldown para executar o código:
    public static void sleepTimer() throws InterruptedException {
        try {
            for (int i = 0; i < 4; i++) {
                Thread.sleep(1000);
                System.out.print(". ");
            }
            limparConsole();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // função responsável por exibir o menu principal:
    public static void mostrarMenu() throws Exception {
        System.out.println("==================== MENU =====================");
        System.out.println("|                                             |");
        System.out.println("|   [1]. Apostar um número                    |");
        System.out.println("|   [2]. Trocar número de aposta              |");
        System.out.println("|   [3]. Mostrar números da cartela           |");
        System.out.println("|   [4]. Exibir total de apostas realizadas   |");
        System.out.println("|   [5]. Exibir total de dinheiro apostado    |");
        System.out.println("|   [6]. Relátorio completo                   |");
        System.out.println("|   [0]. Encerrar programa                    |");
        System.out.println("|                                             |");
        System.out.println("===============================================");
    }

    public static int opcaoUsuario() throws Exception {
        int opcao = 0;

        while (true) {
            try {
                mostrarMenu();

                Scanner input = new Scanner(System.in);
                System.out.print("--> ");
                opcao = input.nextInt();

                if (opcao >= 0 && opcao <= 6) {
                    break;
                } else {
                    System.out.println("\n> Opçao inválida! Digite apenas alguma das opçoes presentes no menu.");
                }
            } catch (Exception e) {
                System.out.println("\n> Opçao inválida! Digite apenas números.");
            }
            sleepTimer();
        }

        return opcao;
    }

    // função de apostar:
    public static int apostar(String[][] cartela, long[][] relatorio, int cont) throws Exception {
        if (cont >= 25) {
            limparConsole();
            System.out.println(">> As apostas acabaram, todos os números já foram escolhidos!");
            sleepTimer();
            return cont;
        }

        int numero, multiplicador, valorAposta;
        long cpf;

        cpf = verificaCPF(relatorio);
        numero = verificaNumero(cartela);
        multiplicador = multiplicaAposta();
        valorAposta = multiplicador * 2;

        relatorio[cont][0] = numero;
        relatorio[cont][1] = cpf;
        relatorio[cont][2] = multiplicador;
        relatorio[cont][3] = valorAposta;

        return cont += 1;
    }

    // função que verifica se o cpf informado pode realizar mais apostas:
    public static long verificaCPF(long[][] relatorio) throws Exception {
        limparConsole();

        long cpf;
        while (true) {
            try {
                int cont = 0;
                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite seu CPF\n--> ");
                cpf = input.nextLong();
                int lenCpf = Long.toString(cpf).length();

                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (j == 1) {
                            if (relatorio[i][j] == cpf) {
                                cont++;
                            }
                        }
                    }
                }

                if (lenCpf != 11) {
                    System.out.println("\n> CPF inválido! Digite um CPF válido.");
                } else {
                    if (cont >= 2) {
                        System.out.println("\n> Operaçao falhou! Número máximo de apostas exercido para este CPF.");
                    } else {
                        return cpf;
                    }
                }

            } catch (Exception e) {
                System.out.println("\n> CPF inválido! Digite apenas números.");
            }
            sleepTimer();
        }
    }

    // função que que realiza a verificação e validação do numero escolhido:
    public static int verificaNumero(String[][] cartela) throws Exception {
        int numero, verificador = 0;

        while (true) {
            try {
                limparConsole();
                mostrarTabela(cartela);

                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite o número que você deseja apostar\n--> ");
                numero = input.nextInt();

                if (numero > 0 || numero <= 25) {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (String.valueOf(numero).equals(cartela[i][j])) {
                                cartela[i][j] = "X";
                                return numero;
                            } else if (String.valueOf(numero).equals(CARTELABACKUP[i][j]) && cartela[i][j] == "X") {
                                System.out.println("\n> Operaçao falhou! Esse numero já foi escolhido.");
                            }   
                        }
                    }
                } else {
                    System.out.println("\n> Operaçao falhou! Digite apenas números presentes na cartela.");
                }

            } catch (Exception e) {
                System.out.println("\n> Inválido! Digite apenas números.");
            }
            sleepTimer();
        }
    }

    // função que multiplicar o valor da aposta:
    public static int multiplicaAposta() throws Exception {
        int multiplicador;

        while (true) {
            limparConsole();
            try {
                Scanner input = new Scanner(System.in);
                System.out.println(
                        "<!> Valor da aposta = R$ 2,00 <!>\n> Você pode multiplicar até 5x o valor da aposta <\n");
                System.out.print("Quantas vezes você quer multiplicar a aposta?\n--> ");
                multiplicador = input.nextInt();

                if (multiplicador > 0 && multiplicador <= 5) {
                    limparConsole();
                    System.out.println("\n>> Aposta realizada com sucesso. Boa sorte!");
                    break;
                } else {
                    System.out.println("\n> Você só pode multiplicar de 1x até 5x o valor da aposta.");
                }
            } catch (Exception e) {
                System.out.println("\n> Inválido! Digite apenas númeoros.");
            }
            sleepTimer();
        }

        return multiplicador;
    }

    // função para o usuario trocar o numero que apostou:
    public static void trocarNumeroAposta(String[][] cartela, long[][] relatorio, int cont) throws Exception {
        if (cont == 0) {
            limparConsole();
            System.out.println(">> Operaçao indisponível! Nenhuma aposta foi realizada ainda.");
            sleepTimer();
            return;
        } else if (cont >= 25) {
            limparConsole();
            System.out.println(">> Operaçao indisponível! Todos os números já foram escolhidos!");
            sleepTimer();
            return;
        }

        int numeroEscolhido, novoNumero;
        long cpf;

        cpf = verificaCpfTrocaAposta(relatorio);
        numeroEscolhido = mostraNumerosVinculadoCpf(cartela, relatorio, cpf);
        novoNumero = novoNumeroAposta(cartela, relatorio, cpf, numeroEscolhido);

        limparConsole();
        System.out.println("\n>> Número trocado com sucesso. Boa sorte!");

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    if (relatorio[i][j] == numeroEscolhido) {
                        relatorio[i][j] = novoNumero;
                    }
                }
            }
        }
    }

    public static long verificaCpfTrocaAposta(long[][] relatorio) throws Exception {
        int verificador = 0;
        long cpf;

        while (true) {
            try {
                limparConsole();
                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite seu CPF\n--> ");
                cpf = input.nextLong();

                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (j == 1) {
                            if (relatorio[i][j] == cpf) {
                                return cpf;
                            } else {
                                verificador = -1;
                            }
                        }
                    }
                }

                if (verificador == -1) {
                    System.out.println("\n> Nao existe nenhuma aposta vinculada a este CPF! Por favor, digite outro.");
                }
            } catch (Exception e) {
                System.out.println("\n> CPF inválido! Digite apenas números.");
            }
            sleepTimer();
        }
    }

    public static int mostraNumerosVinculadoCpf(String[][] cartela, long[][] relatorio, long cpf) throws Exception {
        limparConsole();

        long[] numeros = new long[2];
        int cont = 0;

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 1) {
                    if (cpf == relatorio[i][j]) {
                        numeros[cont] = relatorio[i][j - 1];
                        cont++;
                    }
                }
            }
        }

        int numeroEscolhido = 0, verificador = 0;
        while (true) {
            try {
                System.out.print("> Seus números apostados\n=> ");
                for (int i = 0; i < cont; i++) {
                    if (i == 1) {
                        System.out.print(", ");
                    }
                    System.out.print(numeros[i]);
                }

                Scanner input = new Scanner(System.in);
                System.out.print("\n\n> Digite o número que deseja trocar\n--> ");
                numeroEscolhido = input.nextInt();

                for (int i = 0; i < cont; i++) {
                    if (numeroEscolhido == numeros[i]) {
                        return numeroEscolhido;
                    } else {
                        verificador = -1;
                    }
                }

                if (verificador == -1) {
                    System.out.println("\n> Número inválido! Você nao escolheu esse número antes.");
                }
            } catch (Exception e) {
                System.out.println("\n> Inválido! Digite apenas números.");
            }
            sleepTimer();
        }
    }

    public static int novoNumeroAposta(String[][] cartela, long[][] relatorio, long cpf, int numeroEscolhido)
            throws Exception {
        int novoNumero = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (String.valueOf(numeroEscolhido).equals(CARTELABACKUP[i][j])) {
                    cartela[i][j] = String.valueOf(numeroEscolhido); // a posição será a mesma nas duas matrizes da cartela.
                    novoNumero = verificaNumero(cartela);
                }

                // if (numeroEscolhido == Integer.parseInt(cartela[i][j]) + 1) {
                //     (cartela[i][j + 1]) = String.valueOf(numeroEscolhido);
                //     novoNumero = verificaNumero(cartela);
                // }
            }
        }

        return novoNumero;
    }

    // função responsável por exibir a cartela (atualizada) para o usuario:
    public static void mostrarTabela(String[][] cartela) throws Exception {
        limparConsole();
        System.out.print("============= TABELA ==============");
        for (int i = 0; i < 5; i++) {
            System.out.println("");
            System.out.print(" ");
            for (int j = 0; j < 5; j++) {
                System.out.print(cartela[i][j] + "\t");
            }
            System.out.println("");
        }
        System.out.println("===================================");
    }

    // função que mostra o total de dinheiro apostado:
    public static void mostrarTotalDinheiroApostado(long[][] relatorio) throws Exception {
        limparConsole();

        int soma = 0;
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 3) {
                    soma += relatorio[i][j];
                }
            }
        }

        if (soma == 0) {
            System.out.println(">> Nenhuma aposta foi realizada ainda.");
        } else {
            System.out.println(">> Total de dinheiro apostado: " + soma);
        }
    }

    // função que mostra o relatorio completo:
    public static void mostrarRelatorioCompleto(long[][] relatorio, int cont) throws Exception {
        limparConsole();

        if (cont == 0) {
            System.out.println(">> Relatório vazio.");
        } else {
            System.out.print("============= RELATORIO ==============");
            System.out.println("\n Num.\t    CPF\t\tMult.\tTotal");
            for (int i = 0; i < cont; i++) {
                System.out.print(" ");
                for (int j = 0; j < 4; j++) {
                    System.out.print(" " + relatorio[i][j] + "\t");
                }
                System.out.println("");
            }
            System.out.println("======================================");
        }
    }

    public static void florminenC() throws Exception {
        System.out.println("                       *:.=#                      ");
        System.out.println("                     *-... :=#                    ");
        System.out.println("                  #+:. :*%+. .=#                  ");
        System.out.println("              #*+-  ..*%%%%%+.. .=+#              ");
        System.out.println(" +-=++**++=:::.  ..=#%%%%%%%%%#=.. .:::-++***++-=+");
        System.out.println(" =.        ..:-*%%%%%#%%%#+:..*%%%%+-..         -+");
        System.out.println(" =. *%%%%%%%%%%%%%%*-  .:-=+%+:.-*%%%%%%%%%%%%: -+");
        System.out.println(" =: +%%%%%%%%%%%%%%%%+..*%%%#-.=#%%%%%%%%%%%%%. -*");
        System.out.println(" +: =%%%%%%%%%%%+=++:......:..:=+:+%%%%%%%%%%%..-%");
        System.out.println(" #:.-%%%%%%%%%%*-..=#*  =#**+-=.-*%%%%%%%%%%%* .= ");
        System.out.println(" %-.:#%%%%%%%%%%*+...-  -:::.:.:.-++#%%%%%%%%+ .+ ");
        System.out.println("  +..+%%%%%%%%+...-*%* .*%%%%+.*+..=%%%%%%%%%- :% ");
        System.out.println("  %:.-#%%%%%+.:**..+%+  ....+#=:%*.+%#%%%%%%*: =  ");
        System.out.println("   *..#%%%%-.+%%+..=#+.:*#.-##%%%%%#--#%%%%%= :%  ");
        System.out.println("   %- .::.. .:::.. ... .......:::::::::::::. .*   ");
        System.out.println("    +: =##= .*##=..*#+ :*#:+########+..:-*#: =    ");
        System.out.println("    %=..+##+..+#=..##= :#########*-..++:*#- :+    ");
        System.out.println("     #- .+###- .: -##- -####+--..  .#####=..+     ");
        System.out.println("      %- .*####*: :          -##########=  *      ");
        System.out.println("       #-..=###=.:###:.:*##############: .*       ");
        System.out.println("        #-..-##+=:.-=  .+############*. :+        ");
        System.out.println("          +. .*####=.....=##########=. :#         ");
        System.out.println("           *:..-###*=-##*=--=*####*. .=%          ");
        System.out.println("            #+ ..*###############-..:*            ");
        System.out.println("              %-. :*###########=...+              ");
        System.out.println("                *- .:*#######=...=*               ");
        System.out.println("                  #-. :*###=...+%                 ");
        System.out.println("                    *-..:-. :=#                   ");
        System.out.println("                      *=..:+#                     ");
        System.out.println("                        %%                        \n\n");
        System.out.println("\t\tMEU TRICOLOR AMO VOCE!!!\n\tRUMO AO PRIMEIRO TITULO DA LIBERTADORES!!!\n\n");
        sleepTimer();
    }

    public static void main(String[] args) throws Exception {
        limparConsole();

        String[][] cartela = { { "1", "2", "3", "4", "5" }, { "6", "7", "8", "9", "10" },
                { "11", "12", "13", "14", "15" }, { "16", "17", "18", "19", "20" },
                { "21", "22", "23", "24", "25" } };
        long[][] relatorioApostas = new long[25][4];
        // 0 = numero | 1 = cpf | 2 = multicplicador | 3 = valorTotal
        int cont = 0;

        int opcao;
        do {
            opcao = opcaoUsuario();

            if (opcao == 0) {
                limparConsole();
                System.out.println(">> Programa encerrado..\n\n");
                florminenC();
                String url = "https://www.youtube.com/watch?v=S9Um3d8pRZE&ab_channel=golaudio";
                URI uri = new URI(url);
                Desktop.getDesktop().browse(uri);
                break;
            } else if (opcao == 1) {
                cont = apostar(cartela, relatorioApostas, cont);
            } else if (opcao == 2) {
                trocarNumeroAposta(cartela, relatorioApostas, cont);
            } else if (opcao == 3) {
                mostrarTabela(cartela);
                systemPause();
            } else if (opcao == 4) {
                limparConsole();
                if (cont == 0) {
                    System.out.println(">> Nenhuma aposta foi realizada ainda.");
                } else {
                    System.out.println(">> Total de apostas realizadas: " + cont);
                }
            } else if (opcao == 5) {
                mostrarTotalDinheiroApostado(relatorioApostas);
            } else if (opcao == 6) {
                mostrarRelatorioCompleto(relatorioApostas, cont);
                systemPause();
            }

        } while (true);
    }
}
