package entidade;

public class ContaPoupanca extends Conta {
    private double taxaJurosMensal;

    public ContaPoupanca(String numeroConta, Cliente cliente, ContaTipo contaTipo, double taxaJurosMensal) {
        super(numeroConta, cliente, contaTipo);
        this.taxaJurosMensal = taxaJurosMensal;
    }

    public double calcularRendimentoMensal() {
        double saldoAtual = getSaldo();
        double rendimento = saldoAtual * Math.pow(1 + taxaJurosMensal, 1) - saldoAtual;
        return rendimento;
    }

    public void depositar(double valor) {
        double novoSaldo = getSaldo() + valor;
        setSaldo(novoSaldo);
    }

    public double getTaxaJurosMensal() {
        return taxaJurosMensal;
    }

    public void setTaxaJurosMensal(double taxaJurosMensal) {
        this.taxaJurosMensal = taxaJurosMensal;
    }
}