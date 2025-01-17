package entidade;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import servico.ContaServico;
import servico.MovimentacaoServico;

@Entity
public class Conta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataAbertura;
	@Enumerated(EnumType.STRING)
	private ContaTipo contaTipo;
	
    private String numeroConta;
    private double saldo;
    MovimentacaoServico movimentacaoServico = new MovimentacaoServico();
    

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

    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
}
