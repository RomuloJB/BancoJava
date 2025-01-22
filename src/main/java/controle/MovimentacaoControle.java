package controle;

import java.time.LocalDate;
import java.util.List;

import entidade.Conta;
import entidade.Movimentacao;
import servico.ContaServico;
import servico.MovimentacaoServico;
import util.TipoTransacao;
import util.Utils;

public class MovimentacaoControle {
	
	MovimentacaoServico servico = new MovimentacaoServico();
	Utils util = new Utils();
	ContaServico contaServico = new ContaServico();
	
	public void inserir(Movimentacao movimentacao) {
		servico.inserir(movimentacao);
		atualizarSaldoConta(movimentacao);
		adicionarTarifa(movimentacao);
	}
	
	private void atualizarSaldoConta(Movimentacao mov) {
		Conta conta = mov.getConta();
		double novoSaldo = conta.getSaldo();

		if(mov.getTipoTransacao() == TipoTransacao.DEPOSITO) {
			novoSaldo += mov.getValorOperacao();
		} else if(mov.getTipoTransacao() == TipoTransacao.SAQUE || mov.getTipoTransacao() == TipoTransacao.PAGAMENTO || mov.getTipoTransacao() == TipoTransacao.PIX || mov.getTipoTransacao() == TipoTransacao.DEBITO || mov.getTipoTransacao() == TipoTransacao.CREDITO) {
			novoSaldo -= mov.getValorOperacao();
		}

		conta.setSaldo(novoSaldo);
		contaServico.alterar(conta);
		
	}

	
    private void adicionarTarifa(Movimentacao mov) {
        Conta conta = mov.getConta();
        double tarifa = contaServico.adicionarTarifa(mov);

        if (tarifa > 0) {
            conta.setSaldo(conta.getSaldo() - tarifa);
            contaServico.alterar(conta);
        }
    }


	public void extrato(String cpfCorrentista, int mes, int ano) {
		servico.consultarExtratoMensal(cpfCorrentista, mes, ano);
	}

	public List<Movimentacao> consultarExtratoPeriodico(String cpfCorrentista, LocalDate dataInicio, LocalDate dataFim) {
		return servico.consultarExtratoPeriodico(cpfCorrentista, dataInicio, dataFim);
	}

	public void enviarNotificacao(String cpfCorrentista, double saldo) {
		util.enviarNotificacao(cpfCorrentista, saldo);;
	}
}
