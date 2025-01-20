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
//passo 1 confere o cpf do cliente
		if(cliente.getCpfCliente() == null || cliente.getCpfCliente().isEmpty()) {
			throw new IllegalArgumentException("CPF inválido");
		}

// passo 2 define o inicio e o fim do mes anterior p calcular o cashback
		LocalDate inicioMesAnterior = LocalDate.now().minusMonths(1).withDayOfMonth(1);
		LocalDate fimMesAnterior = inicioMesAnterior.withDayOfMonth(inicioMesAnterior.lengthOfMonth());
		
//passo 3 busca as transacoes do cliente	
		List<Movimentacao> transacoes = dao.buscarPorCpf(cliente.getCpfCliente());
	    double totalCompras = 0.0;
	
// passo 3 verifica quais transacoes foram feitas no periodo a ser calculado
	    for (Movimentacao transacao : transacoes) {
	        LocalDate dataTransacao = transacao.getDataTransacao()
	                .toInstant()
	                .atZone(ZoneId.systemDefault())
	                .toLocalDate();
	
// passo 4 verifica se as transacoes foram de debito
	        if (transacao.getTipoTransacao().name().equalsIgnoreCase("CARTAO DE DEBITO") && (dataTransacao.isEqual(inicioMesAnterior) || dataTransacao.isAfter(inicioMesAnterior)) && dataTransacao.isBefore(fimMesAnterior.plusDays(1))) {
	            totalCompras += transacao.getValorOperacao();
	        }
	    }
//passo 5 calcula o cashback
    double cashback = totalCompras * 0.002;

// passo 6 adiciona o cashback no saldo do cliente
    if (cashback > 0) {
        List<Conta> contas = dao.buscarContaPorCpf(cliente.getCpfCliente());
		for(Conta conta : contas){
			double novoSaldo = conta.getSaldo() + cashback;
			conta.setSaldo(novoSaldo);
		}
// passo 7 mensagens de conclusao
        System.out.printf("Cashback de R$%.2f aplicado na conta do cliente %s%n", cashback, cliente.getCpfCliente());
    } else {
        System.out.println("Nenhum cashback a aplicar para este cliente.");
        
    	}
	}
}
