package controle;

import servico.ServicoBase;

public interface ControleBase<T> {

    ServicoBase<T> getServico();

    default T inserir(T entidade){
        return getServico().inserir(entidade);
    }
    default T alterar(T entidade){
        return getServico().alterar(entidade);
    }
   default void excluir(Long id){
        getServico().excluir(id);  
    }
}