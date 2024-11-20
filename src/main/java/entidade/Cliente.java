package entidade;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
    private List<Conta> contas;

    public Cliente() {
    }

    public Cliente(String nome, String cpf, String email, String telefone, String endereco, String cidade, String estado,
            String cep, String pais) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.pais = pais;
        this.contas = new ArrayList<>();
    }

    public String getNomeCliente() {
        return nome;
    }

    public void setNomeCliente(String nome) {
        this.nome = nome;
    }

    public String getCpfCliente() {
        return cpf;
    }

    public void setCpfCliente(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Conta> getContas() {
        return contas;
    }
    
    public boolean adicionarConta(Conta novaConta) {
        if (contas.size() > 3) {
            System.out.println("Não é possível criar uma nova conta. O cliente já possui o máximo de 3 contas.");
            return false;
        }
        contas.add(novaConta);
        System.out.println("Conta " + novaConta.getNumeroConta() + " adicionada com sucesso para o cliente " + nome);
        return true;
    }

    public void listarContas() {
        System.out.println("Contas do cliente " + nome + ":");
        for (Conta conta : contas) {
            System.out.println("- Conta número: " + conta.getNumeroConta());
        }
    }
}