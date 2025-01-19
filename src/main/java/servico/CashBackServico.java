package servico;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Movimentacao;
import entidade.Conta;

public class CashBackServico {
	
	private MovimentacaoDAO dao;

	ContaServico contaServico = new ContaServico();
	
	public CashBackServico(MovimentacaoDAO dao) {
		this.dao = dao;	
	}
	
	public void calcularEaplicarCashback(Cliente cliente) {
		if(cliente.getCpfCliente() == null || cliente.getCpfCliente().isEmpty()) {
			throw new IllegalArgumentException("CPF inválido");
		}
	
		LocalDate inicioMesAnterior = LocalDate.now().minusMonths(1).withDayOfMonth(1);
		LocalDate fimMesAnterior = inicioMesAnterior.withDayOfMonth(inicioMesAnterior.lengthOfMonth());
		
		List<Movimentacao> transacoes = dao.buscarPorCpf(cliente.getCpfCliente());
	    double totalCompras = 0.0;
	
	    for (Movimentacao transacao : transacoes) {
	        LocalDate dataTransacao = transacao.getDataTransacao()
	                .toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDate();
	
	        if (transacao.getTipoTransacao().name().equalsIgnoreCase("CARTAO DE DEBITO") && (dataTransacao.isEqual(inicioMesAnterior) || dataTransacao.isAfter(inicioMesAnterior)) && dataTransacao.isBefore(fimMesAnterior.plusDays(1))) {
	            totalCompras += transacao.getValorOperacao();
	        }
	    }
    double cashback = totalCompras * 0.002;

    if (cashback > 0) {
        List<Conta> contas = dao.buscarContaPorCpf(cliente.getCpfCliente());
		for(Conta conta : contas){
			contaServico.atualizarSaldo(conta, cashback);
		}

        System.out.printf("Cashback de R$%.2f aplicado na conta do cliente %s%n", cashback, cliente.getCpfCliente());
    } else {
        System.out.println("Nenhum cashback a aplicar para este cliente.");
        
    	}
	}
}
