package ed2;

import java.util.ArrayList;
import java.util.Random;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.generate;

public class Utils {
	public static String gerarBinarioStringAleatório(int numBits, Random gerador) {
		String binarioString = "";
		for (int i=0; i < numBits; i++) {
			binarioString = binarioString.concat(Integer.toString(gerador.nextInt(2)));
		}

		return binarioString;
	}

	public static String gerarBinarioStringAleatórioComPadrao(int numBits, String padraoDeBits, Random gerador) {
		String binarioString = padraoDeBits;

		for (int i=0; i < numBits - padraoDeBits.length(); i++) {
			binarioString = binarioString.concat(Integer.toString(gerador.nextInt(2)));
		}

		return binarioString;
	}

	public static ArrayList<Elemento> gerarElementosAleatorios(int numElementos, int numBitsPseudoChaves, Random gerador) {
		ArrayList<Elemento> elementos = new ArrayList<>();

		int chave;
		for (int i=0; i<numElementos; i++) {
			chave = converteBinarioStringParaInteiro(Utils.gerarBinarioStringAleatório(numBitsPseudoChaves, gerador));
			elementos.add(new Elemento(chave));
		}

		return elementos;
	}

	public static int converteBinarioStringParaInteiro(String binario) {
		return Integer.parseInt(binario, 2);
	}

	/**
	 * Garante que o valor binário em string gerado terá o número de casas decimais definido pelo usuário
	 * @param valor
	 * @param numDigitos
	 * @return
	 */
	public static String converteInteiroParaBinarioString(int valor, int numDigitos) {
		String novoValor = Integer.toBinaryString(valor);
		return String.format("%0" + numDigitos + "d", Integer.parseInt(novoValor));
	}

	public static String adicionaNCaracteresNoInicioDeString(String stringOriginal, String caracter, int numCaracteres) {
		String aux = generate(() -> "0").limit(10).collect(joining());
		return aux.concat(stringOriginal);
	}
}
