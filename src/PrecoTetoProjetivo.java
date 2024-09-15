import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrecoTetoProjetivo {

    static class Resultado {
        String nomeAcao;
        BigDecimal cotacaoAtual;
        double dy12m;
        BigDecimal lpa;
        BigDecimal dpa;
        double payout;
        BigDecimal precoTetoPessimista;
        BigDecimal precoTetoRazoavel;
        BigDecimal precoTetoOtimista;
        BigDecimal mediaPrecoTeto;
        double porcentagemLucroPessimista;
        double porcentagemLucroRazoavel;
        double porcentagemLucroOtimista;

        Resultado(String nomeAcao, BigDecimal cotacaoAtual, double dy12m, BigDecimal lpa, BigDecimal dpa, double payout,
                  BigDecimal precoTetoPessimista, BigDecimal precoTetoRazoavel, BigDecimal precoTetoOtimista,
                  BigDecimal mediaPrecoTeto, double porcentagemLucroPessimista, double porcentagemLucroRazoavel,
                  double porcentagemLucroOtimista) {
            this.nomeAcao = nomeAcao;
            this.cotacaoAtual = cotacaoAtual;
            this.dy12m = dy12m;
            this.lpa = lpa;
            this.dpa = dpa;
            this.payout = payout;
            this.precoTetoPessimista = precoTetoPessimista;
            this.precoTetoRazoavel = precoTetoRazoavel;
            this.precoTetoOtimista = precoTetoOtimista;
            this.mediaPrecoTeto = mediaPrecoTeto;
            this.porcentagemLucroPessimista = porcentagemLucroPessimista;
            this.porcentagemLucroRazoavel = porcentagemLucroRazoavel;
            this.porcentagemLucroOtimista = porcentagemLucroOtimista;
        }

        // Impressão dos resultados no formato de tabela
        public void imprimirTabela() {
            DecimalFormat df = new DecimalFormat("R$ #,##0.00");
            System.out.printf("| %-10s | %-12s | %-6.2f%% | %-8s | %-8s | %-7.2f%% |\n",
                    nomeAcao,
                    df.format(cotacaoAtual),
                    dy12m * 100,
                    df.format(lpa),
                    df.format(dpa),
                    payout * 100);
        }

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("R$ #,##0.00");

            String resultadoPessimista = cotacaoAtual.compareTo(precoTetoPessimista) > 0 ? "Caro" : "Comprar";
            String resultadoRazoavel = cotacaoAtual.compareTo(precoTetoRazoavel) > 0 ? "Caro" : "Comprar";
            String resultadoOtimista = cotacaoAtual.compareTo(precoTetoOtimista) > 0 ? "Caro" : "Comprar";

            return String.format("Nome da ação: %s\nCotação Atual: %s\n" +
                            "Preço Teto Projetivo (Pessimista - %.2f%%): %s - %s\n" +
                            "Preço Teto Projetivo (Razoável - %.2f%%): %s - %s\n" +
                            "Preço Teto Projetivo (Otimista - %.2f%%): %s - %s\n" +
                            "Média dos Preços Tetos Projetivos: %s\n",
                    nomeAcao,
                    df.format(cotacaoAtual),
                    porcentagemLucroPessimista * 100, df.format(precoTetoPessimista), resultadoPessimista,
                    porcentagemLucroRazoavel * 100, df.format(precoTetoRazoavel), resultadoRazoavel,
                    porcentagemLucroOtimista * 100, df.format(precoTetoOtimista), resultadoOtimista,
                    df.format(mediaPrecoTeto));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Resultado> resultados = new ArrayList<>();
        int opcao = 0;

        while (opcao != 3) {
            System.out.println("Menu:");
            System.out.println("1. Calcular Preço Teto");
            System.out.println("2. Imprimir Últimos Resultados");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir nova linha

            switch (opcao) {
                case 1:
                    // Coleta de dados
                    System.out.print("Nome da ação: ");
                    String nomeAcao = scanner.nextLine();

                    System.out.print("Cotação atual: ");
                    double cotacaoAtual = scanner.nextDouble();

                    System.out.print("DY (12m): ");
                    double dy12m = scanner.nextDouble();

                    System.out.print("Payout (em %): ");
                    double payout = scanner.nextDouble() / 100.0; // converter para decimal

                    System.out.print("Número de papéis: ");
                    long numeroPapeis = scanner.nextLong();

                    System.out.print("Lucro projetivo: ");
                    double lucroProjetivo = scanner.nextDouble();

                    // Coleta das porcentagens de lucro desejado
                    System.out.print("Porcentagem de lucro desejado pessimista (em %): ");
                    double porcentagemLucroPessimista = scanner.nextDouble() / 100.0; // converter para decimal

                    System.out.print("Porcentagem de lucro desejado razoável (em %): ");
                    double porcentagemLucroRazoavel = scanner.nextDouble() / 100.0; // converter para decimal

                    System.out.print("Porcentagem de lucro desejado otimista (em %): ");
                    double porcentagemLucroOtimista = scanner.nextDouble() / 100.0; // converter para decimal

                    // Cálculo do LPA
                    BigDecimal lpa = BigDecimal.valueOf(lucroProjetivo).divide(BigDecimal.valueOf(numeroPapeis), 2, RoundingMode.HALF_UP);

                    // Cálculo do DPA
                    BigDecimal dpa = lpa.multiply(BigDecimal.valueOf(payout)).setScale(2, RoundingMode.HALF_UP);

                    // Cálculo dos Preços Tetos Projetivos
                    BigDecimal precoTetoPessimista = dpa.divide(BigDecimal.valueOf(porcentagemLucroPessimista), 2, RoundingMode.HALF_UP);
                    BigDecimal precoTetoRazoavel = dpa.divide(BigDecimal.valueOf(porcentagemLucroRazoavel), 2, RoundingMode.HALF_UP);
                    BigDecimal precoTetoOtimista = dpa.divide(BigDecimal.valueOf(porcentagemLucroOtimista), 2, RoundingMode.HALF_UP);

                    // Cálculo da média dos preços tetos
                    BigDecimal mediaPrecoTeto = precoTetoPessimista.add(precoTetoRazoavel).add(precoTetoOtimista)
                            .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

                    // Armazenar resultado
                    resultados.add(new Resultado(nomeAcao, BigDecimal.valueOf(cotacaoAtual), dy12m, lpa, dpa, payout,
                            precoTetoPessimista, precoTetoRazoavel, precoTetoOtimista, mediaPrecoTeto,
                            porcentagemLucroPessimista, porcentagemLucroRazoavel, porcentagemLucroOtimista));
                    System.out.println("Preço teto calculado e armazenado com sucesso!");
                    break;

                case 2:
                    // Imprimir resultados armazenados
                    if (resultados.isEmpty()) {
                        System.out.println("Nenhum resultado disponível para impressão.");
                    } else {
                        // Cabeçalho da tabela
                        System.out.println("| Ação       | Cotação     | DY     | LPA      | DPA      | Payout  |");
                        System.out.println("--------------------------------------------------------------------");
                        for (Resultado resultado : resultados) {
                            resultado.imprimirTabela(); // Imprime em formato de tabela
                            System.out.println(resultado); // Exibe detalhes completos
                        }
                    }
                    break;

                case 3:
                    System.out.println("Saindo do programa.");
                    break;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }

        // Fechar o scanner
        scanner.close();
    }
}
