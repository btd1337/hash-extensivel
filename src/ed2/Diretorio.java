package ed2;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Diretorio {
	private ArrayList<Balde> pastas;
	private ArrayList<Balde> pastasAux;
	private int profundidadeGlobal;
	private Function<Integer,String> hash;
	private int tamanhoBaldes;
	private int numElementos;

	private int duplicacoesPorBaldeCheio = 0;
	private int duplicacoesPorFatorDeCarga = 0;

	static Function<Integer, String> extrairPseudoChaveDeChaveInteira = (chave) ->  Integer.toBinaryString(chave) ;


	/**
	 * Cria diretório começando com 2 pastas
	 * @param tamanhoBaldes
	 * @param hash
	 */
	Diretorio(int tamanhoBaldes, Function<Integer,String> hash) {
		this.profundidadeGlobal = 1;
		this.numElementos = 0;
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
		this.numElementos = 0;
		this.hash = hash;
		int numPastas = (int) Math.pow(2, profundidadeGlobal);
		pastas = new ArrayList<Balde>(numPastas);
		pastasAux = new ArrayList<>(Constantes.NUM_MINIMO_PASTAS);

		this.inicializaApontamentodePastasParaBalde(numPastas, Constantes.PROFUNDIDADE_INICIAL_BALDE);
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

		// Se encher o balde ou extrapolar o fator de carga então duplicar diretorios
		boolean isBaldeCheio = this.pastas.get(indiceBalde).getElementos().size() == this.tamanhoBaldes ? true : false;
		boolean iraExcederFatorDeCarga = this.iraExcederFatorDeCarga();

		if ( isBaldeCheio || iraExcederFatorDeCarga) {
			this.duplicaDiretorio();
			this.incrementaEstatisticaDuplicacaoDiretorio(isBaldeCheio, iraExcederFatorDeCarga);
			// recalcula novo indice
			indiceBalde = this.determinaIndice(pseudoChave);
		}

		this.pastas.get(indiceBalde).adiciona(elemento);
		this.numElementos++;
	}

	/**
	 * Obtém o índice do diretório de um elemento através da sua pseudo-chave
	 * ---
	 * Funcionamento: Método obtém os N primeiros digitos de uma pseudo-chave, que devem
	 * estar em binário e converte ela para base decimal
	 */
	public int determinaIndice(String pseudoChave) {

		if (pseudoChave.length() < profundidadeGlobal) {
			pseudoChave = Utils.adicionaNCaracteresNoInicioDeString(
					pseudoChave, "0", profundidadeGlobal - pseudoChave.length()
			);
		}
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

	private boolean iraExcederFatorDeCarga() {
		double fatorDeCargaPosInsercao  = (this.numElementos + 1) / this.pastas.size();
		return fatorDeCargaPosInsercao > Constantes.FATOR_DE_CARGA_PADRAO ? true : false;
	}

	private void incrementaEstatisticaDuplicacaoDiretorio(boolean isBaldeCheio, boolean iraExcederFatorDeCarga) {
		if (isBaldeCheio) {
			this.duplicacoesPorBaldeCheio++;
		}

		if (iraExcederFatorDeCarga) {
			this.duplicacoesPorFatorDeCarga++;
		}
	}

	public String obterEstatisticas() {
		return String.format(
				"Número de Pastas: %d | Número de Elementos: %d | Número de Duplicação por balde cheio: %d | Número de Duplicação por fator de carga: %d",
				this.pastas.size(), this.numElementos, this.duplicacoesPorBaldeCheio, this.duplicacoesPorFatorDeCarga
		);
	}

	public void printaDiretorio() {
		String binarioString;
		System.out.println("Profundidade: " + this.profundidadeGlobal);
		for (int i=0; i < this.pastas.size(); i++) {
			binarioString = Utils.converteInteiroParaBinarioString(i, this.profundidadeGlobal);
			System.out.printf("Diretório " + i + "(" + binarioString + ") => ");
			for (Elemento elemento:
			     this.pastas.get(i).getElementos()) {

				binarioString = Utils.converteInteiroParaBinarioString(elemento.getChave(), this.profundidadeGlobal);
				System.out.printf(binarioString + " ");
			}
			System.out.printf("\n");
		}
	}
}
