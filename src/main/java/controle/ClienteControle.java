package controle;

import entidade.Conta;
import servico.ClienteServico;
import servico.ServicoBase;
import entidade.Cliente;

public class ClienteControle implements ControleBase<Cliente> {

    ClienteServico servico = new ClienteServico();

    public Cliente insert(Cliente cliente){
        return servico.inserir(cliente);
    }

    public Cliente getCliente(Long id) {
        return servico.getCliente(id);
    }

    public Cliente getClientePorCpf(String nextLine) {
        return servico.getClientePorCpf(nextLine);
    }
    
    @Override
    public ServicoBase<Cliente> getServico() {
        return servico;
    }
}
