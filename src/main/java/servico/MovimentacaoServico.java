package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.MovimentacaoDAO;
import dao.DAOGenerico;
import dao.MoviDao;
import entidade.Cliente;
import entidade.Movimentacao;

public class MovimentacaoServico implements ServicoBase<Movimentacao> {
	MovimentacaoDAO dao = new MovimentacaoDAO();
	MoviDao daog = new MoviDao();
	Movimentacao mov = new Movimentacao();

	@Override
	public Movimentacao inserir(Movimentacao movimentacao) {
		movimentacao.setDescricao("Operação de " + movimentacao.getTipoTransacao());
		movimentacao.setDataTransacao(new Date());
		Movimentacao movimentacaoBanco = daog.inserir(movimentacao);
		return movimentacaoBanco;
	}

	public Movimentacao inserirCashback (Movimentacao cashback) {
		return dao.inserir(cashback);
	}

	public Movimentacao buscarPorId (Long id) {
		return dao.buscarPorId(id);
	}

	public List<Movimentacao> buscarPorTipoTransacao(String tipoTransacao) {
		return dao.buscarPorTipoTransacao(tipoTransacao);
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

	@Override
	public DAOGenerico<Movimentacao> getDAO() {
		return daog;
	}
}