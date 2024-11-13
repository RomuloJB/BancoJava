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

		Conta conta = new Conta("12345", cliente, movimentacaoServico);
		
		Movimentacao mov = new Movimentacao();
		mov.setCpfCorrentista(cliente.getCpf());
		mov.setDataTransacao(new Date());
		mov.setDescricao("Depósito de 500,00 no dia 03/10/24");
		mov.setNomeCorrentista(cliente.getNome());
		mov.setTipoTransacao("depósito");
		mov.setValorOperacao(500.0);
		
		controle.inserir(mov);

		conta.atualizarSaldo(mov);
		System.out.println("Saldo da conta: " + conta.getSaldo());
	}

	

}
