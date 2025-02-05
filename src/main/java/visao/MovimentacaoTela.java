package visao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import controle.*;
import entidade.*;
import servico.*;
import util.TipoTransacao;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle movControle = new MovimentacaoControle();
		ContaControle contaControle = new ContaControle();
		ClienteControle clienteControle = new ClienteControle();
		ContaServico contaServico = new ContaServico();
		MovimentacaoServico movServico = new MovimentacaoServico();
		
		
		// teste de inserção de cliente
		Cliente cliente = clienteControle.getClientePorCpf("12345678909");
		if(cliente == null) {
			cliente = new Cliente();
			cliente.setNomeCliente("João");
			cliente.setCpfCliente("12345678909");
			clienteControle.inserir(cliente);
		}

		// teste de inserção de contas (vai dar erro se tiver mais de 3 contas)
		Conta conta1 = contaControle.getContaPorNumero("001");
		if(conta1 == null){
			conta1 = new Conta();
			conta1.setNumeroConta("001");
			conta1.setCliente(cliente);
			conta1.setDataAbertura(new Date());
			conta1.setContaTipo(ContaTipo.CONTA_CORRENTE);
			contaControle.adicionarConta(cliente, conta1);
		}
		
		Conta conta2 = contaControle.getContaPorNumero("002");
		if(conta2 == null){
			conta2 = new Conta();
			conta2.setNumeroConta("002");
			conta2.setCliente(cliente);
			conta2.setDataAbertura(new Date());
			conta2.setContaTipo(ContaTipo.CONTA_CORRENTE);
			contaControle.adicionarConta(cliente, conta2);
		}
		
		Conta conta3 = contaControle.getContaPorNumero("003");
		if(conta3 == null){
			conta3 = new Conta();
			conta3.setNumeroConta("003");
			conta3.setCliente(cliente);
			conta3.setDataAbertura(new Date());
			conta3.setContaTipo(ContaTipo.CONTA_POUPANCA);
			contaControle.adicionarConta(cliente, conta3);
		}
		
/*
		Conta conta4 = new Conta();
		conta4.setNumeroConta("004");
		conta4.setCliente(cliente);
		conta4.setDataAbertura(new Date());
		conta4.setContaTipo(ContaTipo.CONTA_CORRENTE);
        contaControle.adicionarConta(cliente, conta4); // vai dar erro
        
        contaControle.listarContas(cliente);
		
		

		// teste de inserção de movimentação
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(5000.0);
		mov.setDataTransacao(new Date());
		mov.setDescricao("Depósito de 5000,00 no dia 17/10/24");
		mov.setTipoTransacao(TipoTransacao.DEPOSITO);
		mov.setConta(conta3); 
		
		movControle.inserir(mov);

// limitar operacoes diarias
		 if (contaServico.limitarOperacoes(mov)) {
            movControle.inserir(mov);
        } else {
            System.out.println("\nLimite de 10 operações diárias excedido.");
        }


// teste de SAQUE, PAGAMENTO com TARIFA e LIMITE DE SAQUE
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(500.0);
		mov.setDataTransacao(new Date());
		mov.setDescricao("Saque de 500,00 no dia 17/10/24");
		mov.setTipoTransacao(TipoTransacao.SAQUE);
		mov.setConta(conta3);
		
		if(contaServico.limiteSaquesDiarios(mov) == false) {
			System.out.println("\nLimite de saques diarios atingido (R$5000), tente novamente amanha.");
		} else {
			movControle.inserir(mov);
		}

*/
// teste de SAQUE SEM LIMITE
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(24998.0);
		mov.setDataTransacao(new Date());
		mov.setDescricao("Saque de 24998,00 no dia 17/10/24");
		mov.setTipoTransacao(TipoTransacao.SAQUE);
		mov.setConta(conta2);
		
		movControle.inserir(mov);
		

/*
// teste CASHBACK para CARTAO DE DEBITO
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(100.0);
		mov.setDataTransacao(new Date());
		mov.setDescricao("Pagamento de 100,00 no DEBITO dia 17/10/24");
		mov.setTipoTransacao(TipoTransacao.DEBITO);
		mov.setConta(conta2);
		
		movServico.inserirCashback(mov);
		movControle.inserir(mov);

		
// teste PIX transacao, limite etc...
		Movimentacao mov = new Movimentacao();
		mov.setValorOperacao(500.00);
		mov.setDataTransacao(new Date());
		mov.setDescricao("PIX de 500,00 no dia 17/10/24");
		mov.setTipoTransacao(TipoTransacao.PIX);
		mov.setConta(conta1);

		if(contaServico.limitePix(mov) == true && contaServico.horarioPix(mov) == true) {
			System.out.println("\nPIX realizado com sucesso");
			movControle.inserir(mov);
			} else {
				System.out.println("\nLimite de PIX excedido");
				}
		
		
/*
// teste de cálculo de rendimento

		ContaPoupanca contaPoupanca = new ContaPoupanca("003", cliente, ContaTipo.CONTA_POUPANCA, 0.005);
		
        contaPoupanca.depositar(1000.00);

        double rendimento = contaPoupanca.calcularRendimentoMensal();
        System.out.printf("\nRendimento mensal: R$ %.2f%n", rendimento);



// teste de extrato mensal

		List<Movimentacao> extratoMensal = movServico.consultarExtratoMensal("12345678909", 1, 2025);
		System.out.println("\nExtrato Mensal:");
		for(Movimentacao m : extratoMensal) {
			System.out.println("\nData: " + m.getDataTransacao() + " - Descrição: " + m.getDescricao() + " - Valor: " + m.getValorOperacao());
		}
		// teste de extrato por período
		LocalDate dataInicio = LocalDate.of(2025, 1, 19);
		LocalDate dataFim = LocalDate.of(2025, 1, 21);
		List<Movimentacao> extratoPeriodico = movServico.consultarExtratoPeriodico("12345678909", dataInicio, dataFim);
		System.out.println("\nExtrato do Período entre " + dataInicio + " e " + dataFim + ":");
		for(Movimentacao m : extratoPeriodico) {
			System.out.println("\nData: " + m.getDataTransacao() + " - Descrição: " + m.getDescricao() + " - Valor: " + m.getValorOperacao());
    	}
		*/

	}
}
