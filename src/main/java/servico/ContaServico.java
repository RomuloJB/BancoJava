package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import dao.MoviDao;
import entidade.Cliente;
import entidade.Conta;
import entidade.Movimentacao;
import util.ValidarCpf;
 
public class ContaServico {
	ClienteServico clienteservico = new ClienteServico();
	MoviDao mdao = new MoviDao();
	Movimentacao mov = new Movimentacao();
	Cliente cliente = new Cliente();

	public Conta inserir(Cliente cliente, Conta novaConta) {
        List<Conta> contas = cliente.getContas();
        if (contas.size() >= 3) {
            return null;
        }
        contas.add(novaConta);
        return novaConta;
    }
	
	public boolean adicionarConta(Cliente cliente, Conta novaConta) {
		
		List<Conta> contas = cliente.getContas();
        if (contas.size() > 3) {
            System.out.println("Não é possível criar uma nova conta. O cliente já possui o máximo de 3 contas.");
            return false;
        }
        
        contas.add(novaConta);
        System.out.println("Conta " + novaConta.getNumeroConta() + " adicionada com sucesso para o cliente " + cliente.getNomeCliente());
        return true;
    
	}

	public void listarContas(Cliente cliente) {
    	List<Conta> contas = cliente.getContas();
        System.out.println("Contas do cliente " + cliente.getNomeCliente() + ":");
        
        for (Conta conta : contas) {
            System.out.println("- Conta número: " + conta.getNumeroConta());
        }
    }
/*
  public void atualizarSaldo(Conta conta, double valor){
		double novoSaldo = conta.getSaldo() + valor;
		conta.setSaldo(novoSaldo);
	}
*/
    
	
	public double verificarSaldo(Cliente cliente) {
		if(!ValidarCpf.validarCpf(cliente.getCpfCliente())) {
			throw new IllegalArgumentException("CPF inválido");
		}
		
		List<Movimentacao> movs = mdao.buscarPorCpf(cliente.getCpfCliente());
		double produtoDepositos = 1.0;
		double totalSaques = 0.0;
		double totalPagamentos = 0.0;
		double totalPix = 0.0;

		for (Movimentacao m : movs) {
			if (m.getTipoTransacao().name().equalsIgnoreCase("DEPOSITO")) {
				produtoDepositos *= m.getValorOperacao();
			} else if (m.getTipoTransacao().name().equalsIgnoreCase("SAQUE")) {
				totalSaques += m.getValorOperacao();
			} else if (m.getTipoTransacao().name().equalsIgnoreCase("PAGAMENTO")) {
				totalPagamentos += m.getValorOperacao();
			} else if (m.getTipoTransacao().name().equalsIgnoreCase("PIX") && m.getDescricao().contains("pagamento")) {
				totalPix += m.getValorOperacao();
			}
		}

		double saldo = produtoDepositos - totalSaques - totalPagamentos - totalPix;
		saldo = Math.max(saldo, 0.0);

		if (saldo < 100.0) {
			enviarNotificacao(cliente.getCpfCliente(), saldo);
		}

		return saldo;
	}
// transferir para util
	private void enviarNotificacao(String cpfCorrentista, double saldo) {
		System.out.println("Notificação: O saldo do correntista com CPF " + cpfCorrentista
				+ " está abaixo de 100. Saldo atual: " + saldo);
	}
//

	public boolean limitePix(Movimentacao mov) {
		if (mov.getTipoTransacao().name().equalsIgnoreCase("PIX") && mov.getValorOperacao() > 300.00) {
			return false;
		} else {
			return true;
		}
	}

	public double calcularSaquesDiarios(Movimentacao mov) {
		if (!ValidarCpf.validarCpf(cliente.getCpfCliente())) {
			throw new IllegalArgumentException("CPF inválido");
		}
		List<Movimentacao> movs = mdao.buscarPorCpf(cliente.getCpfCliente());
		LocalDate hoje = LocalDate.now();
		LocalDateTime inicioDoDia = hoje.atStartOfDay();
	
		double totalSaques = 0.0;
		for (Movimentacao m : movs) {
			if (m.getTipoTransacao().name().equalsIgnoreCase("saque") &&
					m.getDataTransacao().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
							.isAfter(inicioDoDia)) {
				totalSaques += m.getValorOperacao();
			}
		}
	
		return totalSaques;
	}
	
	public boolean limiteSaquesDiarios(Movimentacao mov) {
		if (mov.getTipoTransacao().name().equalsIgnoreCase("saque")) {
			double totalSaques = calcularSaquesDiarios(mov);
			if (totalSaques + mov.getValorOperacao() > 5000.00) {
				return false;
			}
		}
		return true;
	}
	
	public double adicionarTarifa(Movimentacao mov) {
		if (mov.getTipoTransacao().name().equalsIgnoreCase("pagamento") || mov.getTipoTransacao().name().equalsIgnoreCase("PIX")) {
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
		if (mov.getTipoTransacao().name().equalsIgnoreCase("PIX") && (hora < 6 || hora > 22)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean limitarOperacoes(Movimentacao mov) {
		List<Movimentacao> movs = mdao.buscarPorCpf(cliente.getCpfCliente());
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

}


