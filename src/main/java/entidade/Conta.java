package entidade;

import servico.MovimentacaoServico;

public class Conta {
    private String numeroConta;
    private Cliente cliente;
    private MovimentacaoServico movimentacaoServico;
    private double saldo;

    public Conta(String numeroConta, Cliente cliente, MovimentacaoServico movimentacaoServico) {
        this.numeroConta = numeroConta;
        this.cliente = cliente;
        this.movimentacaoServico = movimentacaoServico;
        this.saldo = 0.0;

    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public double getSaldo(){
        return saldo;
    }

    public void atualizarSaldo(Movimentacao movimentacao) {
        this.saldo = movimentacaoServico.verificarSaldo(movimentacao);
    }
}