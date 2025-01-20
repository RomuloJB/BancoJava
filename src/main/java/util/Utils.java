package util;

import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Movimentacao;

public class Utils {

	MovimentacaoDAO	mdao = new MovimentacaoDAO();
	Cliente cliente = new Cliente();

	public void enviarNotificacao(String cpfCorrentista, double saldo) {
		System.out.println("Notificação: O saldo do correntista com CPF " + cpfCorrentista
				+ " está abaixo de 100. Saldo atual: " + saldo);
	}

	public boolean verificarFraude(Movimentacao mov) {
		List<Movimentacao> movs = mdao.buscarPorCpf(cliente.getCpfCliente());
		double somaValores = 0.0;
		int cont = 0;

		for (Movimentacao m : movs) {
			somaValores += m.getValorOperacao();
			cont++;
		}

		double mediaValores = somaValores / cont;
		if (mov.getValorOperacao() > 3 * mediaValores) {
			return false;

		} else {
			return true;
		}
	}
}
