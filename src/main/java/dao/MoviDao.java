package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import entidade.Movimentacao;

public class MoviDao extends DAOGenerico<Movimentacao> {

	public MoviDao() {
		super(Movimentacao.class);
	}
	
	public List<Movimentacao> buscarPorCpf(String cpf){
		EntityManager em = getEntityManager();
		try {
			Query query = em.createQuery("from Movimentacao where conta.cliente.cpf='"+ cpf +"'");
			return query.getResultList();
		}finally {
			em.close();
		}
	}
}
