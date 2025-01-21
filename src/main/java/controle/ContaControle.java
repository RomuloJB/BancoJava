package controle;

import entidade.Conta;
import servico.ContaServico;
import java.util.List;
import entidade.Cliente;

public class ContaControle {

    ContaServico contaServico = new ContaServico();

    public Conta adicionarConta(Cliente cliente, Conta novaConta) {
        return contaServico.inserir(cliente, novaConta);
    }

    public void listarContas(Cliente cliente) {
        contaServico.listarContas(cliente);
    }
}

