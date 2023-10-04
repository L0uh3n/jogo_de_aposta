import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/* Falta:
 * Deixar o output mais bonito e polido;
 * Organizar melhor o código e testar por erros/bugs;
 */

public class jogo_do_bicho {
    // gambiarra, ja que o "~" não é exibido no sysout;
    public static final char TIO = '~';

    // função para "limpar" o console;
    public static void limparConsole() throws IOException, InterruptedException {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
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
        Scanner input = new Scanner(System.in);
        int opcao = 0;

        while (true) {
            try {
                System.out.print(">> ");
                opcao = input.nextInt();

                if (opcao >= 0 && opcao <= 6) {
                    break;
                } else {
                    System.out.println("Opção inválida! Digite apenas alguma das opções presentes no menu.");
                }
            } catch (Exception e) {
                System.out.println("Opção inválida! Digite apenas números.");
            }
        }

        return opcao;
    }

    // função de apostar:
    public static int apostar(int[][] cartela, int[][] relatorio, int cont) throws Exception {
        limparConsole();

        Scanner input = new Scanner(System.in);
        int cpf, numero, multiplicador, vlorAposta;

        cpf = verificaCPF(relatorio);
        numero = verificaNumero(cartela);
        multiplicador = multiplicaAposta();
        vlorAposta = multiplicador * 2;

        relatorio[cont][0] = numero;
        relatorio[cont][1] = cpf;
        relatorio[cont][2] = multiplicador;
        relatorio[cont][3] = vlorAposta;

        return cont += 1;
    }

    // função que verifica se o cpf informado pode realizar mais apostas:
    public static int verificaCPF(int[][] relatorio) throws Exception {
        int cpf;

        while (true) {
            try {
                int cont = 0;

                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite seu CPF\n>> ");
                cpf = input.nextInt();

                for (int i = 0; i < 25; i++) {
                    for (int j = 0; j < 4; j++) {
                        if (j == 1) {
                            if (relatorio[i][j] == cpf) {
                                cont++;
                            }
                        }
                    }
                }

                if (cont >= 2) {
                    System.out.println("> Operação falhou! Número máximo de apostas execido para este CPF.");
                } else {
                    return cpf;
                }
            } catch (Exception e) {
                System.out.println("> CPF inválido! Digite apenas números.");
            }
        }
    }

    // função que que realiza a verificação e validação do numero escolhido:
    public static int verificaNumero(int[][] cartela) throws Exception {
        mostrarTabela(cartela);
        int numero, verificador = 0;

        while (true) {
            try {
                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite o número que você deseja apostar\n>> ");
                numero = input.nextInt();

                if (numero <= 0 || numero > 25) {
                    System.out.println("> Operação falhou! Digite apenas números presentes na cartela.");
                } else {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            if (numero == cartela[i][j]) {
                                cartela[i][j] = 0;
                                return numero;
                            } else {
                                verificador = -1;
                            }
                        }
                    }
                }

                if (verificador == 0) {
                    System.out.println("> Operação falhou! Esse numero já foi escolhido.");
                }
            } catch (Exception e) {
                System.out.println("> Inválido! Digite apenas números.");
            }
        }
    }

    // função que multiplicar o valor da aposta:
    public static int multiplicaAposta() throws Exception {
        int multiplicador;

        while (true) {
            try {
                Scanner input = new Scanner(System.in);
                System.out.println("Valor da aposta = R$ 2,00;\n> Você pode multiplicar até 5x o valor da aposta <");
                System.out.print("Quantas vezes você quer multiplicar a aposta?\n>> ");
                multiplicador = input.nextInt();

                if (multiplicador > 0 && multiplicador <= 5) {
                    System.out.println("> Aposta realizada com sucesso. Boa sorte!");
                    break;
                } else {
                    System.out.println("> Você só pode multiplicar até 5x o valor da aposta.");
                }
            } catch (Exception e) {
                System.out.println("> Inválido! Digite apenas númeoros.");
            }
        }

        return multiplicador;
    }

    // função para o usuario trocar o numero que apostou: !!!
    public static void trocarNumeroAposta(int[][] cartela, int[][] relatorio) throws Exception {
        int cpf, numeroEscolhido, novoNumero;

        cpf = verificaCpfTrocaAposta(relatorio);
        numeroEscolhido = mostraNumerosVinculadoCpf(cartela, relatorio, cpf);
        // atualizaCartela();
        novoNumero = novoNumeroAposta(cartela, relatorio, cpf, numeroEscolhido);

        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 4; j++) {
                if (numeroEscolhido == relatorio[i][0]) {
                    relatorio[i][j] = novoNumero;
                }
            }
        }
    }

    public static int verificaCpfTrocaAposta(int[][] relatorio) throws Exception {
        int cpf, verificador = 0;

        while (true) {
            try {
                Scanner input = new Scanner(System.in);
                System.out.print("\n> Digite seu CPF\n>> ");
                cpf = input.nextInt();

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
                    System.out.println("> Não existe nenhuma aposta vinculada a este CPF! Por favor, digite outro.");
                }
            } catch (Exception e) {
                System.out.println("> CPF inválido! Digite apenas números.");
            }
        }
    }

    public static int mostraNumerosVinculadoCpf(int[][] cartela, int[][] relatorio, int cpf) throws Exception {
        int[] numeros = new int[2];
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

        System.out.print("Seus números apostados\n>> ");
        for (int i = 0; i < cont; i++) {
            System.out.print(numeros[i] + " ");
        }

        int numeroEscolhido = 0;
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("\n> Digite o número que deseja trocar\n>> ");
            numeroEscolhido = input.nextInt();

            for (int i = 0; i < cont; i++) {
                if (numeroEscolhido == numeros[i]) {
                    break;
                } else {
                    System.out.println("> Número inválido! Você não escolheu esse número antes.");
                }
            }
        } catch (Exception e) {
            System.out.println("> Inválido! Digite apenas números.");
        }

        return numeroEscolhido;
    }

    public static int novoNumeroAposta(int[][] cartela, int[][] relatorio, int cpf, int numeroEscolhido) throws Exception {
        int novoNumero = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numeroEscolhido == (cartela[i][j] + 1)) {
                    cartela[i][j + 1] = numeroEscolhido;
                    novoNumero = verificaNumero(cartela);
                }
            }
        }

        return novoNumero;
    }

    // função responsável por exibir a cartela (atualizada) para o usuario:
    public static void mostrarTabela(int[][] cartela) throws Exception {
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
    public static void mostrarTotalDinheiroApostado(int[][] relatorio) throws Exception {
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
    public static void mostrarRelatorioCompleto(int[][] relatorio, int cont) throws Exception {
        if (cont == 0) {
            System.out.println("> Reltorio vazio.");
        } else {
            System.out.print("========= RELATORIO ==========");
            System.out.println("\n Num.\t CPF\tMult.\tTotal");
            for (int i = 0; i < cont; i++) {
                System.out.print(" ");
                for (int j = 0; j < 4; j++) {
                    System.out.print(" " + relatorio[i][j] + "\t");
                }
                System.out.println("");
            }
            System.out.println("==============================");
        }

    }

    public static void main(String[] args) throws Exception {
        limparConsole();
        // System.out.println(tio + " ã â é à");

        int[][] cartela = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 14, 15 }, { 16, 17, 18, 19, 20 },
                { 21, 22, 23, 24, 25 } };
        int[][] relatorioApostas = new int[25][4];
        // 0 = numero | 1 = cpf | 2 = multicplicador | 3 = valorTotal
        int cont = 0;

        int opcao;
        do {
            mostrarMenu();
            opcao = opcaoUsuario();

            if (opcao == 0) {
                System.out.println("Programa encerrado!");
                break;
            } else if (opcao == 1) {
                cont = apostar(cartela, relatorioApostas, cont);
            } else if (opcao == 2) {
                trocarNumeroAposta(cartela, relatorioApostas);
            } else if (opcao == 3) {
                mostrarTabela(cartela);
            } else if (opcao == 4) {
                if (cont == 0) {
                    System.out.println(">> Nenhuma aposta foi realizada ainda.");
                } else {
                    System.out.println(">> Total de apostas realizadas: " + cont);
                }
            } else if (opcao == 5) {
                mostrarTotalDinheiroApostado(relatorioApostas);
            } else if (opcao == 6) {
                mostrarRelatorioCompleto(relatorioApostas, cont);
            }

        } while (true);
    }
}
