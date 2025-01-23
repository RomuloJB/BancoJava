package servico;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import dao.ContaDAO;
import dao.DAOGenerico;
import dao.MovimentacaoDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.Movimentacao;
import util.TipoTransacao;
import util.Utils;
import util.ValidarCpf;
 
public class ContaServico implements ServicoBase<Conta> {
	ClienteServico clienteservico = new ClienteServico();
	MovimentacaoDAO mdao = new MovimentacaoDAO();
	Movimentacao mov = new Movimentacao();
	Cliente cliente = new Cliente();
	Utils util = new Utils();
	ContaDAO dao = new ContaDAO();
	
	public Conta inserir(Cliente cliente, Conta novaConta) {
		
		List<Conta> contas = cliente.getContas();
		if(contas == null) {
			contas = new ArrayList<>();
			cliente.setContas(contas);
		}

        if (contas.size() >= 3) {
            System.out.println("\nNão é possível criar uma nova conta. O cliente já possui o máximo de 3 contas.");
            return null;
        }

        contas.add(novaConta);
		novaConta.setCliente(cliente);
		dao.inserir(novaConta);
		
        System.out.println("\nConta " + novaConta.getNumeroConta() + " adicionada com sucesso para o cliente " + cliente.getNomeCliente());
        return novaConta;
	}

	public Conta getContaPorNumero(String numeroConta){
		return dao.getContaPorNumero(numeroConta);
	}

	public void listarContas(Cliente cliente) {
    	List<Conta> contas = cliente.getContas();
        System.out.println("\nContas do cliente " + cliente.getNomeCliente() + ":");
        
        for (Conta conta : contas) {
            System.out.println("\nConta número: " + conta.getNumeroConta());
        }
    }

	public Conta alterar(Conta conta) {
		return dao.alterar(conta);
	}
	
	public double verificarSaldo(Cliente cliente) {
		if(!ValidarCpf.validarCpf(cliente.getCpfCliente())) {
			System.out.println("\nCPF inválido");
		}
		
		List<Movimentacao> movs = mdao.buscarPorCpf(cliente.getCpfCliente());
		// troquei produtoDepositos = 1.0 para totalDepositos = 0.0
		double totalDepositos = 0.0;
		double totalSaques = 0.0;
		double totalPagamentos = 0.0;
		double totalPix = 0.0;

        for (Movimentacao m : movs) {
            TipoTransacao tipo = m.getTipoTransacao();
            if (tipo == TipoTransacao.DEPOSITO) {
				// troquei produtoDepositos *= m.getValorOperacao() para totalDepositos += m.getValorOperacao()
                totalDepositos += m.getValorOperacao();
            } else if (tipo == TipoTransacao.SAQUE) {
                totalSaques += m.getValorOperacao();
            } else if (tipo == TipoTransacao.PAGAMENTO) {
                totalPagamentos += m.getValorOperacao();
            } else if (tipo == TipoTransacao.PIX && m.getDescricao().contains("pagamento")) {
                totalPix += m.getValorOperacao();
            }
        }

		double saldo = totalDepositos - totalSaques - totalPagamentos - totalPix;
		saldo = Math.max(saldo, 0.0);

		if (saldo < 100.0) {
			util.enviarNotificacao(cliente.getCpfCliente(), saldo);
		}

		List<Conta> contas = cliente.getContas();
		for (Conta conta : contas) {
			conta.setSaldo(saldo);
		}
		
		return saldo;
	}

	public boolean limitePix(Movimentacao mov) {
		if (mov.getTipoTransacao().name().equalsIgnoreCase("PIX") && mov.getValorOperacao() > 300.00) {
			return false;
		} else {
			return true;
		}
	}

	public double calcularSaquesDiarios(Movimentacao mov) {
//ultima alteraçao tirei o ! de dentro desse if
		if (ValidarCpf.validarCpf(cliente.getCpfCliente())) {
			throw new IllegalArgumentException("\nCPF inválido");
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
		switch (mov.getTipoTransacao()) {
			case PAGAMENTO:
			case PIX:
				return 5.0;
			case SAQUE:
				return 2.0;
			default:
				return 0.0;
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
		List<Movimentacao> movs = mdao.buscarPorCpf(mov.getConta().getCliente().getCpfCliente());
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

	@Override
	public DAOGenerico<Conta> getDAO() {
		return dao;
	}

}


