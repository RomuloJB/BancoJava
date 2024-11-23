package teste;

public class CaixaTeste {
	public static void main(String[] args) {
		
		Caixa<String> caixa1 = new Caixa<>();
		caixa1.colocar("Computador");
		System.out.println(caixa1.pegar());
		
		Caixa<Integer> caixa2 = new Caixa<>();
		caixa2.colocar(10);
		System.out.println(caixa2.pegar());
	}
}
