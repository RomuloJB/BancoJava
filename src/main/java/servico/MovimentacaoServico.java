package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Movimentacao;

public class MovimentacaoServico {
	MovimentacaoDAO dao = new MovimentacaoDAO();

	public Movimentacao inserir(Movimentacao movimentacao) {
		movimentacao.setDescricao("Operação de " + movimentacao.getTipoTransacao());
		movimentacao.setDataTransacao(new Date());
		Movimentacao movimentacaoBanco = dao.inserir(movimentacao);
		return movimentacaoBanco;
	}

	public static boolean validarCpf(String CPF) {
		if (CPF == null || CPF.length() != 11 || CPF.matches("(\\d)\\1{10}")) {
			return false;
		}

		try {
			int sum = 0;
			for (int i = 0; i < 9; i++) {
				sum += Character.getNumericValue(CPF.charAt(i)) * (10 - i);
			}

			int firstDigit = 11 - (sum % 11);
			if (firstDigit >= 10) {
				firstDigit = 0;
			}

			sum = 0;
			for (int i = 0; i < 10; i++) {
				sum += Character.getNumericValue(CPF.charAt(i)) * (11 - i);
			}

			int secondDigit = 11 - (sum % 11);
			if (secondDigit >= 10) {
				secondDigit = 0;
			}

			return firstDigit == Character.getNumericValue(CPF.charAt(9)) &&
					secondDigit == Character.getNumericValue(CPF.charAt(10));
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public double verificarSaldo(Movimentacao mov) {
		if(!validarCpf(mov.getCpfCorrentista())) {
			throw new IllegalArgumentException("CPF inválido");
		}
		List<Movimentacao> movs = dao.buscarPorCpf(mov.getCpfCorrentista());
		double produtoDepositos = 1.0;
		double totalSaques = 0.0;
		double totalPagamentos = 0.0;
		double totalPix = 0.0;

		for (Movimentacao m : movs) {
			if (m.getTipoTransacao().equals("depósito")) {
				produtoDepositos *= m.getValorOperacao();
			} else if (m.getTipoTransacao().equals("saque")) {
				totalSaques += m.getValorOperacao();
			} else if (m.getTipoTransacao().equals("pagamento")) {
				totalPagamentos += m.getValorOperacao();
			} else if (m.getTipoTransacao().equals("PIX") && m.getDescricao().contains("pagamento")) {
				totalPix += m.getValorOperacao();
			}
		}

		double saldo = produtoDepositos - totalSaques - totalPagamentos - totalPix;
		saldo = Math.max(saldo, 0.0);

		if (saldo < 100.0) {
			enviarNotificacao(mov.getCpfCorrentista(), saldo);
		}

		return saldo;
	}

	private void enviarNotificacao(String cpfCorrentista, double saldo) {
		// lógica de envio de notificação
		System.out.println("Notificação: O saldo do correntista com CPF " + cpfCorrentista
				+ " está abaixo de 100. Saldo atual: " + saldo);
	}

	public boolean limitePix(Movimentacao mov) {
		if (mov.getTipoTransacao().equals("PIX") && mov.getValorOperacao() > 300.00) {
			return false;
		} else {
			return true;
		}
	}

	public double calcularSaquesDiarios(Movimentacao mov) {
		if (!validarCpf(mov.getCpfCorrentista())) {
			throw new IllegalArgumentException("CPF inválido");
		}
		List<Movimentacao> movs = dao.buscarPorCpf(mov.getCpfCorrentista());
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicioDoDia = hoje.atStartOfDay();
	
		double totalSaques = 0.0;
		for (Movimentacao m : movs) {
			if (m.getTipoTransacao().equals("saque") &&
					m.getDataTransacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
							.isAfter(inicioDoDia)) {
				totalSaques += m.getValorOperacao();
			}
		}
	
		return totalSaques;
	}
	
	public boolean limiteSaquesDiarios(Movimentacao mov) {
		if (mov.getTipoTransacao().equals("saque")) {
			double totalSaques = calcularSaquesDiarios(mov); // Passa o objeto completo
			if (totalSaques + mov.getValorOperacao() > 5000.00) {
				return false;
			}
		}
		return true;
	}
	

	public double adicionarTarifa(Movimentacao mov) {
		if (mov.getTipoTransacao().equals("pagamento") || mov.getTipoTransacao().equals("PIX")) {
			return mov.getValorOperacao() + 5.0;
		} else if (mov.getTipoTransacao().equals("saque")) {
			return mov.getValorOperacao() + 2.0;
		} else {
			return mov.getValorOperacao();
		}
	}

	public boolean horarioPix(Movimentacao mov) {
		LocalDateTime agora = LocalDateTime.now();
		int hora = agora.getHour();
		if (mov.getTipoTransacao().equals("PIX") && (hora < 6 || hora > 22)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean limitarOperacoes(Movimentacao mov) {
		List<Movimentacao> movs = dao.buscarPorCpf(mov.getCpfCorrentista());
		LocalDate hoje = LocalDate.now();
		int cont = 0;

		for (Movimentacao m : movs) {
			LocalDate dataTransacao = m.getDataTransacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (dataTransacao.equals(hoje)) {
				cont++;
			}

			if (cont > 10) {
				return false;
			}
		}
		return true;
	}

	public boolean verificarFraude(Movimentacao mov) {
		List<Movimentacao> movs = dao.buscarPorCpf(mov.getCpfCorrentista());
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

	public List<Movimentacao> consultarExtratoMensal(String cpfCorrentista, int mes, int ano) {
		List<Movimentacao> movs = dao.buscarPorCpf(cpfCorrentista);
		List<Movimentacao> extratoMensal = new ArrayList<>();

		LocalDate inicioMes = LocalDate.of(ano, mes, 1);
		LocalDate fimMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

		for (Movimentacao m : movs) {
			LocalDate dataTransacao = m.getDataTransacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (!dataTransacao.isBefore(inicioMes) && !dataTransacao.isAfter(fimMes)) {
				extratoMensal.add(m);
			}
		}

		return extratoMensal;
	}

	public List<Movimentacao> consultarExtratoPeriodico(String cpfCorrentista, LocalDate dataInicio, LocalDate dataFim) {
		List<Movimentacao> movs = dao.buscarPorCpf(cpfCorrentista);
		List<Movimentacao> extratoPeriodico = new ArrayList<>();
	
		for (Movimentacao m : movs) {
			LocalDate dataTransacao = m.getDataTransacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			if (!dataTransacao.isBefore(dataInicio) && !dataTransacao.isAfter(dataFim)) {
				extratoPeriodico.add(m);
			}
		}
	
		return extratoPeriodico;
	}
}