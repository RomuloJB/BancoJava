package servico;

import util.ValidarCpf;
import dao.ClienteDAO;
import dao.DAOGenerico;
import entidade.Cliente;

public class ClienteServico implements ServicoBase<Cliente> {

    ClienteDAO dao = new ClienteDAO();
	
    public Cliente inserir(Cliente cliente){
        if(!ValidarCpf.validarCpf(cliente.getCpfCliente())) {
            System.out.println("CPF inválido");
            return null;
        }
        return dao.inserir(cliente);
    }

    public Cliente getCliente(Long id) {
        Cliente cliente = dao.getCliente(id);
        if(cliente == null) {
            System.out.println("Cliente não encontrado");
            return null;
        }
        return cliente;
    }

    public Cliente getClientePorCpf(String nextLine) {
        return dao.getClientePorCpf(nextLine);
    }

    @Override
    public DAOGenerico<Cliente> getDAO() {
        return dao;
    }
}
