package servico;

import dao.DAOGenerico;

public interface ServicoBase <T>{
	DAOGenerico<T> getDAO();
	
	default T inserir(T entidade) {
		return getDAO().inserir(entidade);
	}

	default T alterar(T entidade){
		return getDAO().alterar(entidade);
	}

	default T excluir(Long id){
		return getDAO().excluir(id);
	}
}
