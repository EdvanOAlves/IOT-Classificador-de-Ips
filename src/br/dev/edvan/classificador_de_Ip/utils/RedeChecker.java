package br.dev.edvan.classificador_de_Ip.utils;

import java.util.ArrayList;

import br.dev.edvan.classificador_de_Ip.model.Rede;

public class RedeChecker {

	public static String checkInput(String ipCidr) {
		ArrayList<String> errorMessages = new ArrayList<>();

		if (isEmpty(ipCidr)) {
			errorMessages.add("Preencha o campo");
		}

		if (hasLetters(ipCidr)) {
			errorMessages.add("Formato inválido (contém letras)");
		}

		if (!checkCharCount(ipCidr, '.', 3)) {
			errorMessages.add("Insira 4 octetos devidamente separados por 3 pontos(.)");
		}

		if (!checkCharCount(ipCidr, '/', 1)) {
			errorMessages.add("Insira uma máscara CIDR indicada por apenas uma barra (/)");
			
		}

		if (hasInvalidChars(ipCidr)) {
			errorMessages.add("Insira apenas caracteres válidos (números, pontos e barra)");
		}

		errorMessages.add("none");
		return errorMessages.getFirst();
		// TODO: No momento isso retorna uma lista de erros mas pro nosso programa só o primeiro importa.
		// poderia ser um return do String ao invés do errorMessages.add em cada checagem, mas dessa 
		// forma fica em aberto para implementar a funcionalidade de retornar uma lista com todos os 
		// erros detectados para o usuário
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
	public static String checkRede(Rede rede) {
		ArrayList<String> errorMessages = new ArrayList<>();

		if (isEmptyMask(rede)) {
			errorMessages.add("Preencha o campo");
		}

		if (hasEmptyOctets(rede)) {
			errorMessages.add("Formato inválido, faltam valores de octetos");

		}

		if (hasOutOfRangeOctets(rede)) {
			errorMessages.add("Formato inválido, octetos devem estar entre 0 e 255");
		}

		if (hasInvalidMask(rede)) {
			errorMessages.add("A máscara CIDR deve estar entre 0 e 32");
		}
		
		errorMessages.add("none");
		return errorMessages.getFirst();

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
