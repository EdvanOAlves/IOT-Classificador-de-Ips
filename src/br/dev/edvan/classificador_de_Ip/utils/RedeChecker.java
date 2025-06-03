package br.dev.edvan.classificador_de_Ip.utils;

import java.util.ArrayList;

import br.dev.edvan.classificador_de_Ip.model.Rede;

public class RedeChecker {

	public static ArrayList<String> checkInput(String ipCidr) {
		ArrayList<String> errorMessages = new ArrayList<>();

		if (isEmpty(ipCidr)) {
			errorMessages.add("Campo vazio");
		}

		if (hasLetters(ipCidr)) {
			errorMessages.add("Caracteres inválidos (contém letras)");
		}

		if (!checkCharCount(ipCidr, '.', 3)) {
			errorMessages.add("Octetos não estão devidamente separados por 3 pontos(.)");
		}

		if (!checkCharCount(ipCidr, '/', 1)) {
			errorMessages.add("Máscara CIDR não está devidamente indicada por uma barra (/)");
			
		}

		if (hasInvalidChars(ipCidr)) {
			errorMessages.add("Caracteres inválidos detectados (Símbolos especiais)");
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

	// Para verificar redes impossíveis
	public static ArrayList<String> checkRede(Rede rede) {
		ArrayList<String> errorMessages = new ArrayList<>();

		if (isEmptyMask(rede)) {
			errorMessages.add("Máscara vazia");
		}

		if (hasEmptyOctets(rede)) {
			errorMessages.add("Faltam valores de octetos");

		}

		if (hasOutOfRangeOctets(rede)) {
			errorMessages.add("Existe um ou mais octetos além do limite (0 a 255)");
		}

		if (hasInvalidMask(rede)) {
			errorMessages.add("Máscara CIDR além do limite (0 a 32)");
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
		// Função para verificar a contagem de algum caracter específico
		// Recebe um String a verificar, um char pra fazer a contagem e qual seria a
		// quantidade certa
		// desse char no String, retorna true se estiver de acordo com a contagem, false
		// em caso contrário.
		int charCount = 0;
		for (int i = 0; i < string.length(); i++) { // Verificação do String
			if (string.charAt(i) == targetChar) { // contagem de pontos
				charCount++;
			}
		}

		return (charCount == expectedAmmount);

	}

}
