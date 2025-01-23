package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.MovimentacaoDAO;
import dao.DAOGenerico;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.Movimentacao;
import util.TipoTransacao;

public class MovimentacaoServico implements ServicoBase<Movimentacao> {
	MovimentacaoDAO dao = new MovimentacaoDAO();
	Movimentacao mov = new Movimentacao();
	ContaServico contaServico = new ContaServico();

	@Override
	public Movimentacao inserir(Movimentacao mov) {
		
        double tarifa = adicionarTarifa(mov);
        double valorTotalOperacao = mov.getValorOperacao() + tarifa;
        Conta conta = mov.getConta();

        if (mov.getTipoTransacao() != TipoTransacao.DEPOSITO && valorTotalOperacao > conta.getSaldo()) {
            System.out.println("Transação não pode ser concluída. Saldo insuficiente.");
            return null;
        }

        mov.setDescricao("\nOperação de " + mov.getTipoTransacao());
        mov.setDataTransacao(new Date());
        Movimentacao movimentacaoBanco = dao.inserir(mov);
        atualizarSaldoConta(mov);
        return movimentacaoBanco;
    }

	public Movimentacao inserirCashback (Movimentacao cashback) {
		if (cashback.getId() != null) {
            cashback = dao.buscarPorId(cashback.getId());
        }
		return dao.inserir(cashback);
	}
	
	public void atualizarSaldoConta(Movimentacao mov) {
		Conta conta = mov.getConta();
		double novoSaldo = conta.getSaldo();

		if(mov.getTipoTransacao() == TipoTransacao.DEPOSITO) {
			novoSaldo += mov.getValorOperacao();
		} else {
			novoSaldo -= mov.getValorOperacao();
		}
		conta.setSaldo(novoSaldo);
		contaServico.alterar(conta);
	}
	
    public double adicionarTarifa(Movimentacao mov) {
        switch (mov.getTipoTransacao()) {
            case PAGAMENTO:
            case PIX:
                return 5.0; // Tarifa fixa para pagamento e PIX
            case SAQUE:
                return 2.0; // Tarifa fixa para saque
            default:
                return 0.0; // Sem tarifa para outras transações
        }
    }
	/*
	    private double adicionarTarifa(Movimentacao mov) {
        Conta conta = mov.getConta();
        double tarifa = contaServico.adicionarTarifa(mov);

        if (tarifa > 0) {
            conta.setSaldo(conta.getSaldo() - tarifa);
            contaServico.alterar(conta);
			return tarifa;
        }
    } 
		
	*/


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
		return dao;
	}
}