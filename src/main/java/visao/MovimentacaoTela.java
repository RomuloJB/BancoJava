package visao;

import java.util.Date;

import controle.*;
import entidade.*;
import servico.*;
import util.TipoTransacao;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle controle = new MovimentacaoControle();
		MovimentacaoServico movimentacaoServico = new MovimentacaoServico();
		ContaControle contaControle = new ContaControle();
		ContaServico contaServico = new ContaServico();

// teste de inserção de cliente
		ClienteControle clienteControle = new ClienteControle();
		Cliente cliente = new Cliente();
		cliente.setNomeCliente("João");
		cliente.setCpfCliente("12345678900");
		clienteControle.inserir(cliente);
				
// teste de inserção de contas (vai dar erro se tiver mais de 3 contas)
		Conta conta1 = new Conta();
		conta1.setNumeroConta("001");
		conta1.setCliente(cliente);
		conta1.setContaTipo(ContaTipo.CONTA_CORRENTE);
        contaControle.adicionarConta(cliente, conta1);

		Conta conta2 = new Conta();
		conta1.setNumeroConta("002");
		conta1.setCliente(cliente);
		conta1.setContaTipo(ContaTipo.CONTA_CORRENTE);
        contaControle.adicionarConta(cliente, conta2);

		Conta conta3 = new Conta();
		conta1.setNumeroConta("003");
		conta1.setCliente(cliente);
		conta1.setContaTipo(ContaTipo.CONTA_POUPANCA);
        contaControle.adicionarConta(cliente, conta3);

		Conta conta4 = new Conta();
		conta1.setNumeroConta("004");
		conta1.setCliente(cliente);
		conta1.setContaTipo(ContaTipo.CONTA_CORRENTE);
        contaControle.adicionarConta(cliente, conta4); // vai dar erro
        
        contaControle.listarContas(cliente);
    
// teste de inserção de movimentação
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(500.0);
		mov.setDataTransacao(new Date());
		mov.setDescricao("Depósito de 500,00 no dia 03/10/24");
		mov.setTipoTransacao(TipoTransacao.DEPOSITO);
		mov.setConta(conta1);
		
		controle.inserir(mov);

// teste de atualização de saldo

/*
for (Conta conta : cliente.getContas()) {
			contaServico.atualizarSaldo(conta1, 500.00);
			System.out.println("Saldo da conta " + conta.getNumeroConta() + ": " + conta.getSaldo());
		}
*/
		
// teste de cálculo de rendimento
		ContaPoupanca contaPoupanca = new ContaPoupanca("003", cliente, movimentacaoServico, 0.005);
        contaPoupanca.depositar(1000.00);

        double rendimento = contaPoupanca.calcularRendimentoMensal();
        System.out.println("Rendimento mensal: " + rendimento);
    }


}

