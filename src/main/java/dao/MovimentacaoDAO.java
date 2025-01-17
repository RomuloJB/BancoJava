package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Conta;
import entidade.Movimentacao;
//import servico.Conta;

public class MovimentacaoDAO {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public Movimentacao inserir(Movimentacao movimentacao) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(movimentacao);
		em.getTransaction().commit();
		em.close();
		return movimentacao;
	}

	public Movimentacao alterar(Movimentacao movimentacao) {
		Movimentacao movimentacaoBanco = null;
		if (movimentacao.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			movimentacaoBanco = buscarPorId(movimentacao.getId());

			if (movimentacaoBanco != null) {
				movimentacaoBanco.setDescricao(movimentacao.getDescricao());
				em.merge(movimentacaoBanco);
			}

			em.getTransaction().commit();
			em.close();
		}
		return movimentacaoBanco;
	}

	public void excluir(Long id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Movimentacao movimentacao = em.find(Movimentacao.class, id);
		if (movimentacao != null) {
			em.remove(movimentacao);
		}
		em.getTransaction().commit();
		em.close();
	}

	public List<Movimentacao> listarTodos() {
		EntityManager em = emf.createEntityManager();
		// hql: hibernate query language
		List<Movimentacao> movimentacoes = em.createQuery("from Movimentacao").getResultList();
		em.close();
		return movimentacoes;
	}
	// buscar todas as contas de acordo com o CPF
	public List<Conta> buscarContaPorCpf(String cpf) {
	    EntityManager em = emf.createEntityManager();
	    Query query = em.createQuery("from Conta where cpfCliente = :cpf");
	    query.setParameter("cpf", cpf);
	    List<Conta> contas = query.getResultList();
	    em.close();
	    return contas;
	}
	// buscar todas as contas de acordo com o tipo da transação
	public List<Conta> buscarContaPorTransacao(String tipoTransacao){
	    EntityManager em = emf.createEntityManager();
	    Query query = em.createQuery(
	        "select distinct c from Conta c join c.movimentacoes m where m.tipoTransacao = :tipoTransacao"
	    );
	    query.setParameter("tipoTransacao", tipoTransacao);
	    List<Conta> contas = query.getResultList();
	    em.close();
	    return contas;
	}

	public List<Movimentacao> buscarPorCpf(String cpf) {
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("from Movimentacao where cpfCliente='"+cpf+"'");
			em.close();
			return query.getResultList();
	}

	public Movimentacao buscarPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		Movimentacao movimentacao = em.find(Movimentacao.class, id);
		em.close();
		return movimentacao;
		// return em.find(Movimentacao.class, id);
	}

}
