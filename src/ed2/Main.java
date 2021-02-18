package ed2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int tamanhoBaldes;
		int numBitsPseudoChaves;
		int numElementos;
		Random gerador = new Random(12);
		ArrayList<Elemento> elementos = new ArrayList<Elemento>();

		try {
			System.out.printf("Informe o tamanho a ser utilizado para os baldes: ");
			tamanhoBaldes = scanner.nextInt();

			System.out.printf("\nInforme o número de bits a ser usado para as pseudo-chaves: ");
			numBitsPseudoChaves = scanner.nextInt();

			System.out.printf("\nInforme o número de elementos de teste: ");
			numElementos = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Valor informado é inválido. Por favor digite um número inteiro positivo");
			return;
		}


		Diretorio diretorio = new Diretorio(tamanhoBaldes, Diretorio.extrairPseudoChaveDeChaveInteira);
		elementos = Utils.gerarElementosAleatorios(numElementos, numBitsPseudoChaves, gerador);

		for (Elemento elemento: elementos) {
			diretorio.adiciona(elemento);
		}

		System.out.println(diretorio.obterEstatisticas());
	}
}
