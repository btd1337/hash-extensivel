import java.util.ArrayList;
import java.util.Random;

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
}
