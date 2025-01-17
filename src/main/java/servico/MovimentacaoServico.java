package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.MovimentacaoDAO;
import dao.MoviDao;
import entidade.Cliente;
import entidade.Movimentacao;

public class MovimentacaoServico {
	MovimentacaoDAO dao = new MovimentacaoDAO();
	MoviDao daoComGenerico = new MoviDao();
	Movimentacao mov = new Movimentacao();

	public Movimentacao inserir(Movimentacao movimentacao) {
		movimentacao.setDescricao("Operação de " + movimentacao.getTipoTransacao());
		movimentacao.setDataTransacao(new Date());
		Movimentacao movimentacaoBanco = daoComGenerico.inserir(movimentacao);
		return movimentacaoBanco;
	}

	
	public boolean verificarFraude(Cliente cliente) {
		List<Movimentacao> movs = dao.buscarPorCpf(cliente.getCpfCliente());
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