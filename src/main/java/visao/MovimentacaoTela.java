package visao;

import java.util.Date;

import controle.*;
import entidade.*;
import servico.*;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle controle = new MovimentacaoControle();
		MovimentacaoServico movimentacaoServico = new MovimentacaoServico();
		ContaControle contaControle = new ContaControle();
		
		Cliente cliente = new Cliente(
			"José", 
			"04425225112");
		
		Conta conta1 = new Conta("001", cliente, movimentacaoServico);
        Conta conta2 = new Conta("002", cliente, movimentacaoServico);
        Conta conta3 = new Conta("003", cliente, movimentacaoServico);
        Conta conta4 = new Conta("004", cliente, movimentacaoServico); // vai dar erro
        
        contaControle.adicionarConta(conta1);
        contaControle.adicionarConta(conta2);
        contaControle.adicionarConta(conta3);
        contaControle.adicionarConta(conta4); // vai dar erro

        contaControle.listarContas();
    
		Movimentacao mov = new Movimentacao();
		mov.setCpfCliente(cliente.getCpfCliente());
		mov.setDataTransacao(new Date());
		mov.setDescricao("Depósito de 500,00 no dia 03/10/24");
		mov.setNomeCliente(cliente.getNomeCliente());
		mov.setTipoTransacao("depósito");
		mov.setValorOperacao(500.0);
		
		controle.inserir(mov);

		for (Conta conta : cliente.getContas()) {
			conta.atualizarSaldo(mov);
			System.out.println("Saldo da conta " + conta.getNumeroConta() + ": " + conta.getSaldo());
		}

		
		ContaPoupanca contaPoupanca = new ContaPoupanca("001", cliente, movimentacaoServico, 0.005);
        contaPoupanca.depositar(1000.00);

        double rendimento = contaPoupanca.calcularRendimentoMensal();
        System.out.println("Rendimento mensal: " + rendimento);
    }
}

