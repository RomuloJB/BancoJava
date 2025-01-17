package controle;

import entidade.Conta;
import servico.ClienteServico;

public class ClienteControle {

    ClienteServico servico = new ClienteServico();

    public Cliente insert(Cliente cliente){
        return servico.insert(cliente);
    }

    
}
