package controle;

import entidade.Conta;
import java.util.List;
import entidade.Cliente;

public class ContaControle {

	public boolean adicionarConta(Cliente cliente, Conta novaConta) {
		
		List<Conta> contas = cliente.getContas();
        if (contas.size() > 3) {
            System.out.println("Não é possível criar uma nova conta. O cliente já possui o máximo de 3 contas.");
            return false;
        }
        
        contas.add(novaConta);
        System.out.println("Conta " + novaConta.getNumeroConta() + " adicionada com sucesso para o cliente " + cliente.getNomeCliente());
        return true;
    
	}
    public void listarContas(Cliente cliente) {
    	List<Conta> contas = cliente.getContas();
        System.out.println("Contas do cliente " + cliente.getNomeCliente() + ":");
        
        for (Conta conta : contas) {
            System.out.println("- Conta número: " + conta.getNumeroConta());
        }
    }
}
