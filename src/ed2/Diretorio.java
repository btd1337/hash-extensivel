package ed2;

import java.util.ArrayList;
import java.util.function.Function;

public class Diretorio {
	private ArrayList<Balde> pastas;
	private ArrayList<Balde> pastasAux;
	private int profundidadeGlobal;
	private Function<Integer,String> hash;
	private int tamanhoBaldes;
	static Function<Integer,String> extrairPseudoChaveDeChaveInteira = (chave) -> Integer.toBinaryString(chave);

	/**
	 * Cria diretório começando com 2 pastas
	 * @param tamanhoBaldes
	 * @param hash
	 */
	Diretorio(int tamanhoBaldes, Function<Integer,String> hash) {
		this.profundidadeGlobal = 1;
		this.tamanhoBaldes = tamanhoBaldes;
		this.hash = hash;
		pastas = new ArrayList<>(Constantes.NUM_MINIMO_PASTAS);
		pastasAux = new ArrayList<>(Constantes.NUM_MINIMO_PASTAS);

		for (int i = 0; i < Math.pow(2, profundidadeGlobal); i++) {
			this.pastas.add(new Balde(this.profundidadeGlobal, this.tamanhoBaldes, this.hash));
			this.pastasAux.add(new Balde(this.profundidadeGlobal, this.tamanhoBaldes, this.hash));
		}
	}

	/**
	 * Permite usuário criar diretorio com número de pastas específico
	 * @param tamanhoBaldes
	 * @param hash
	 * @param profundidadeGlobal
	 */
	Diretorio(int tamanhoBaldes, Function<Integer,String> hash, int profundidadeGlobal) {
		this.profundidadeGlobal = profundidadeGlobal;
		this.hash = hash;
		int numPastas = (int) Math.pow(2, profundidadeGlobal);
		pastas = new ArrayList<Balde>(numPastas);
		pastasAux = new ArrayList<>(Constantes.NUM_MINIMO_PASTAS);

		this.inicializaApontamentodePastasParaBalde(numPastas, Constantes.PROFUNDIDADE_INICIAL_BALDE);
	}

	public ArrayList<Balde> getPastas() {
		return pastas;
	}

	public int getProfundidadeGlobal() {
		return profundidadeGlobal;
	}

	private void inicializaApontamentodePastasParaBalde(int numPastas, int profundidadeBalde) {
		Balde balde = new Balde(0, this.tamanhoBaldes, this.hash);

		for (int i = 0; i < numPastas; i++){
			pastas.add(balde);
		}
	}

	/**
	 * Se número de elementos do balde for igual ao tamanho do balde
	 * duplica diretorio
	 * @param elemento
	 */
	public void adiciona(Elemento elemento) {
		String pseudoChave = this.hash.apply(elemento.getChave());
		int indiceBalde = this.determinaIndice(pseudoChave);

		if (this.pastas.get(indiceBalde).getElementos().size() == this.tamanhoBaldes) {
			this.duplicaDiretorio();
			// recalcula novo indice
			indiceBalde = this.determinaIndice(pseudoChave);
		}

		this.pastas.get(indiceBalde).adiciona(elemento);
	}

	/**
	 * Obtém o índice do diretório de um elemento através da sua pseudo-chave
	 * ---
	 * Funcionamento: Método obtém os N primeiros digitos de uma pseudo-chave, que devem
	 * estar em binário e converte ela para base decimal
	 */
	public int determinaIndice(String pseudoChave) {
		return Integer.parseInt(pseudoChave.substring(0, profundidadeGlobal), 2);
	}

	/**
	 * duplicar diretorio
	 *
	 * dobrar o tamanho do vetor
	 * profundidade global +1
	 *
	 * criar um novo balde pra cada novo diretorio criado
	 *
	 * percorrer o balde de cada diretorio e verificar se alguma das chaves deve ser movida pra outro balde
	 *
	 * adiciona no balde auxiliar da posição
	 *
	 * depois de terminar percorre os diretorios
	 *
	 * adiciona os elementos do balde auxiliar no balde principal
	 *
	 * limpa balde auxiliar
	 */
	public void duplicaDiretorio() {
		int numPastasAtual = (int) Math.pow(2, this.profundidadeGlobal);

		for (int i = 0; i < Math.pow(2, this.profundidadeGlobal); i++) {
			this.pastas.add(new Balde(this.profundidadeGlobal, this.tamanhoBaldes, this.hash));
			this.pastasAux.add(new Balde(this.profundidadeGlobal, this.tamanhoBaldes, this.hash));
		}

		this.profundidadeGlobal++;

		int indice;
		Elemento elemento;

		for (int i = 0; i < numPastasAtual; i++) {
			for ( int j = 0; j < this.pastas.get(i).getElementos().size(); ) {
				elemento = this.pastas.get(i).getElementos().get(j);
				String pseudoChave = this.hash.apply(elemento.getChave());
				indice = this.determinaIndice(pseudoChave);

				if (indice != i) {
					this.pastasAux.get(i).getElementos().add(elemento);
					this.pastas.get(i).getElementos().remove(j);
				} else {
					j++;
				}
			}
		}

		for (int i = 0; i < numPastasAtual; i++) {
			if (this.pastasAux.get(i).getElementos().size() > 0) {
				this.pastas.get(i).getElementos().addAll(this.pastasAux.get(i).getElementos());
				this.pastasAux.get(i).getElementos().clear();
			}
		}
	}
}
