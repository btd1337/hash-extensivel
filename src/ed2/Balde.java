package ed2;

import java.util.ArrayList;
import java.util.function.Function;

public class Balde {
	private int profundidadeLocal;
	private ArrayList<Elemento> elementos;
	private int tamanho;
	private Function<Integer,String> hash;

	Balde(int profundidadeLocal, int tamanho, Function<Integer,String> hash) {
		this.profundidadeLocal = profundidadeLocal;
		this.tamanho = tamanho;
		this.elementos = new ArrayList<>(tamanho);
		this.hash = hash;
	}

	public Elemento busca(String pseudoChave) {
		String pseudoChaveElementoAtual;
		for (Elemento elemento: elementos) {
			pseudoChaveElementoAtual = this.hash.apply(elemento.getChave());

			if (pseudoChaveElementoAtual.equals(pseudoChave)) {
				return elemento;
			}
		}

		return null;
	}

	public boolean remove(String pseudoChave) {

		String pseudoChaveElementoAtual;
		for (int i = 0 ; i < elementos.size(); i++) {
			pseudoChaveElementoAtual = this.hash.apply(elementos.get(i).getChave());

			if (pseudoChaveElementoAtual.equals(pseudoChave)) {
				elementos.remove(i);
				return true;
			}
		}

		return false;
	}

	public boolean adiciona(Elemento elemento) {
		this.elementos.add(elemento);
		return true;
	}

	public boolean isCheio() {
		return this.elementos.size() == tamanho;
	}

	public boolean isVazio() {
		return this.elementos.isEmpty();
	}

	public ArrayList<Elemento> getElementos() {
		return elementos;
	}

}
