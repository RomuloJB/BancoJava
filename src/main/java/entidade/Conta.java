package entidade;

import java.sql.Date;
import javax.persistence.*;

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

    public Conta() {
    }

    public Conta(String numeroConta, Cliente cliente, ContaTipo contaTipo) {
        this.numeroConta = numeroConta;
        this.cliente = cliente;
        this.contaTipo = contaTipo;
        this.dataAbertura = new Date(System.currentTimeMillis());
        this.saldo = 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public ContaTipo getContaTipo() {
        return contaTipo;
    }

    public void setContaTipo(ContaTipo contaTipo) {
        this.contaTipo = contaTipo;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}