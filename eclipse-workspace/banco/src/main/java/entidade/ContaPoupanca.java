package entidade;

import servico.MovimentacaoServico;

public class ContaPoupanca extends Conta {
    private double taxaJurosMensal;

    public ContaPoupanca(String numero, Cliente cliente, MovimentacaoServico movimentacaoServico,   double taxaJurosMensal) {
        super(numero, cliente, movimentacaoServico);
        this.taxaJurosMensal = taxaJurosMensal;
    }

    public double calcularRendimentoMensal() {
        double saldoAtual = getSaldo();
        double rendimento = saldoAtual * Math.pow(1 + taxaJurosMensal, 1) - saldoAtual;
        return rendimento;
    }

    public void depositar(double d) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'depositar'");
    }
}
