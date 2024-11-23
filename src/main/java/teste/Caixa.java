package teste;
//import java.util.List;

public class Caixa<T> {
	
	private T item;
	
	public void colocar (T item) {
		this.item = item;
	}
	
	public T pegar() {
		return item;
	}
}
