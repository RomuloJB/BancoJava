package controle;

import entidade.Conta;
import servico.ContaServico;
import java.util.List;
import entidade.Cliente;

public class ContaControle {

    ContaServico contaServico = new ContaServico();

    public boolean adicionarConta(Cliente cliente, Conta novaConta) {
        return contaServico.adicionarConta(cliente, novaConta);
    }

    public void listarContas(Cliente cliente) {
        contaServico.listarContas(cliente);
    }
}

