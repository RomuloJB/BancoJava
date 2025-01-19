package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.mysql.cj.xdevapi.Client;

import entidade.Cliente;
import entidade.Movimentacao;

public class ClienteDAO extends DAOGenerico<Cliente> {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public ClienteDAO() {
		super(Cliente.class);
	}

	public Cliente inserir(Cliente cliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(cliente);
		em.getTransaction().commit();
		em.close();
		return cliente;
	}
	
	public Cliente excluir(Cliente cliente) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.remove(cliente);
		em.getTransaction().commit();
		em.close();
		return cliente;
	}
	
	public Cliente alterar (Cliente cliente){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(cliente);
		em.getTransaction().commit();
		em.close();
		return cliente;
	}

	public Cliente getCliente(Long id) {
		EntityManager em = getEntityManager();
		try {
			Cliente cliente = em.createQuery("FROM Client WHERE id = '" + id + "'", Cliente.class).getSingleResult();
			em.close();
			return cliente;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Cliente getClientePorCpf(String nextLine) {
		EntityManager em = getEntityManager();
		try {
			Cliente cliente = em.createQuery("FROM Cliente WHERE cpf = '" + nextLine + "'", Cliente.class).getSingleResult();
			em.close();
			return cliente;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
