package br.dev.edvan.classificador_de_Ip.utils;

import java.util.ArrayList;

import br.dev.edvan.classificador_de_Ip.model.Rede;

public class RedeChecker {

	public static ArrayList<String> checkInput(String ipCidr) {
		ArrayList<String> errorMessages = new ArrayList<String>();

		if (isEmpty(ipCidr)) {
			errorMessages.add("Campo vazio");
		}

		if (hasLetters(ipCidr)) {
			errorMessages.add("Caracteres invalidos (contem letras)");
		}

		if (!checkCharCount(ipCidr, '.', 3)) {
			errorMessages.add("Octetos nao estao devidamente separados por 3 pontos(.)");
		}

		if (!checkCharCount(ipCidr, '/', 1)) {
			errorMessages.add("Mascara CIDR nao esta devidamente indicada por uma barra (/)");
			
		}

		if (hasInvalidChars(ipCidr)) {
			errorMessages.add("Caracteres invalidos detectados (Simbolos especiais)");
		}

		if (errorMessages.size() == 0) {
			errorMessages.add("none");
		}
		return errorMessages;
	}

	private static boolean isEmpty(String ipCidr) {
		return (ipCidr.length() == 0);
	}

	private static boolean hasLetters(String ipCidr) {
		for (char c : ipCidr.toCharArray()) {
			if (Character.isLetter(c)) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasInvalidChars(String ipCidr) {
		return !ipCidr.matches("[0-9./]+$");
	}

	// Para verificar redes impossiveis
	public static ArrayList<String> checkRede(Rede rede) {
		ArrayList<String> errorMessages = new ArrayList<>();

		if (isEmptyMask(rede)) {
			errorMessages.add("Mascara vazia");
		}

		if (hasEmptyOctets(rede)) {
			errorMessages.add("Faltam valores de octetos");

		}

		if (hasOutOfRangeOctets(rede)) {
			errorMessages.add("Existe um ou mais octetos alem do limite (0 a 255)");
		}

		if (hasInvalidMask(rede)) {
			errorMessages.add("Mascara CIDR alem do limite (0 a 32)");
		}
		
		if (errorMessages.size() == 0) {
			errorMessages.add("none");
		}
		return errorMessages;

	}

	private static boolean isEmptyMask(Rede rede) {
		return rede.getIpIsolated().length < 2;
	}

	private static boolean hasEmptyOctets(Rede rede) {
		return rede.getIpSplit().length < 4;
	}

	private static boolean hasOutOfRangeOctets(Rede rede) {
		for (int i = 0; i < rede.getIpSplit().length; i++) {
			if (Integer.parseInt(rede.getIpSplit()[i]) > 255 || Integer.parseInt(rede.getIpSplit()[i]) < 0) {
				return true;
			}
		}
		return false;
	}

	private static boolean hasInvalidMask(Rede rede) {
		return rede.getMask() < 0 || rede.getMask() > 32;
	}

	private static boolean checkCharCount(String string, char targetChar, int expectedAmmount) {
		// Metodo para verificar a contagem de algum caracter especifico
		// Recebe um String a verificar, um char pra fazer a contagem e um int que indica a
		// quantidade certa desse char no String, retorna true se estiver de acordo com a contagem,
		// se nao, retorna false.
		int charCount = 0;
		for (int i = 0; i < string.length(); i++) { // Verificacao do String
			if (string.charAt(i) == targetChar) { // contagem de pontos
				charCount++;
			}
		}

		return (charCount == expectedAmmount);

	}

}
