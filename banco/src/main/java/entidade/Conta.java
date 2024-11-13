package entidade;

import servico.MovimentacaoServico;

public class Conta {
    private String numero;
    private Cliente cliente;
    private MovimentacaoServico movimentacaoServico;
    private double saldo;

    public Conta(String numero, Cliente cliente, MovimentacaoServico movimentacaoServico) {
        this.numero = numero;
        this.cliente = cliente;
        this.movimentacaoServico = movimentacaoServico;
        this.saldo = 0.0;

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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