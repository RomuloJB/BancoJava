package visao;

import java.util.Date;

import controle.*;
import entidade.*;
import servico.*;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle controle = new MovimentacaoControle();
		MovimentacaoServico movimentacaoServico = new MovimentacaoServico();
		
		Cliente cliente = new Cliente(
			"José", 
			"04425225112",
			"teste@gmail.com",
			"999999999",
			"Rua 1",
			"Cidade 1",
			"Estado 1",
			"00000000",
			"Brasil");
		
		Conta conta1 = new Conta("001", cliente, movimentacaoServico);
        Conta conta2 = new Conta("002", cliente, movimentacaoServico);
        Conta conta3 = new Conta("003", cliente, movimentacaoServico);
        Conta conta4 = new Conta("004", cliente, movimentacaoServico); // vai dar erro

        cliente.adicionarConta(conta1);
        cliente.adicionarConta(conta2);
        cliente.adicionarConta(conta3);
        cliente.adicionarConta(conta4); // vai dar erro

        cliente.listarContas();
    
		Movimentacao mov = new Movimentacao();
		mov.setCpfCorrentista(cliente.getCpfCliente());
		mov.setDataTransacao(new Date());
		mov.setDescricao("Depósito de 500,00 no dia 03/10/24");
		mov.setNomeCorrentista(cliente.getNomeCliente());
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

