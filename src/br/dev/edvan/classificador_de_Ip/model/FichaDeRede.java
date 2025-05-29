package br.dev.edvan.classificador_de_Ip.model;

public class FichaDeRede {
	Rede rede;
	private String[] profileRede;
	private String[] detailsRede;
	private String errorMessage;

	public FichaDeRede(String ipCidr) {
		errorMessage = "none";
		checkInput(ipCidr); // Para fazer a verificação de erros antes da criação da rede
		if (errorMessage.equals("none")) {
			rede = new Rede(ipCidr);
			checkRede(rede); // para fazer a verificação se é uma rede válida
			makeFichas();
		}
	}

	// Check input, só uma função para detectar os erros de input
	// TODO: Montar uma classe pra fazer isso de maneira mais independente com
	// funções mais organizadas
	public void checkInput(String ipCidr) {

		// Campo vazio? //
		if (ipCidr.length() == 0) {
			errorMessage = "Preencha o campo";
		}

		// Letras? //
		for (char c : ipCidr.toCharArray()) {
			if (Character.isLetter(c)) {
				if (errorMessage.equals("none")) {
					// Coloquei essa verificação em todos, só pra ter uma hierarquia nas mensagens
					// de erro
					errorMessage = "Formato inválido (contém letras)";
				}
			}
		}

		// Existencia de elementos chave (/ e .)
		if ((ipCidr.indexOf("/")) == -1 || (ipCidr.indexOf(".")) == -1) {
			if (errorMessage.equals("none")) {
				errorMessage = "Formato inválido, siga o exemplo: (192.168.10.0/24)";
			}

		}

		// Contagem de elementos chave //
		if (!checkCharCount(ipCidr, '.', 3)) {
			if (errorMessage.equals("none")) {
				errorMessage = "Insira 4 octetos devidamente separados por 3 pontos(.)";
			}
		}
		if (!checkCharCount(ipCidr, '/', 1)) {
			if (errorMessage.equals("none")) {
				errorMessage = "Insira uma máscara CIDR indicada por apenas uma barra (/)";
			}
		}
		
		//	Símbolos especiais	//
		{
			if (!ipCidr.matches("[0-9./]+$")){
				if (errorMessage.equals("none")) {
					errorMessage = "Insira apenas caracteres válidos (números, pontos e barra)";
				}
				
			}
		}
		
	}

	public void checkRede(Rede rede) {
		{ // Octetos vazios? //
			if (rede.getIpSplit().length < 4) { // Resolvendo bug de input "10.../25", octetos vazios basicamente
				errorMessage = "Formato inválido, faltam valores de octetos";
			}
		}
		{// Verificando se tá no range de 0 e 255
			for (int i = 0; i < rede.getIpSplit().length; i++) {
				if (Integer.parseInt(rede.getIpSplit()[i]) > 255 || Integer.parseInt(rede.getIpSplit()[i]) < 0) {
					errorMessage = "Formato inválido, octetos devem estar entre 0 e 255";
				}
			}
		}

	}

	private boolean checkCharCount(String string, char targetChar, int expectedAmmount) {
		// Função para verificar a contagem de algum caracter específico
		// Recebe um String a verificar, um char pra fazer a contagem e qual seria a
		// quantidade certa
		// desse char no String, retorna true se estiver tudo certo, false se estiver
		// tudo errado
		int charCount = 0;
		for (int i = 0; i < string.length(); i++) { // Verificação do String
			if (string.charAt(i) == targetChar) { // contagem de pontos
				charCount++;
			}
		}
		if (charCount != expectedAmmount) {
			return false;
		} else {
			return true;
		}

	}

	// MAKE FICHAS, o método que vai decidir o tipo de fichas que vai fazer e como
	// fazer, diferenciar se é sub-rede, etc.
	//
	private void makeFichas() {
		if (rede.getMask() == 32) { // Caso CIDR 32, é uma exceção
			profileRede = getClassificacaoVisualDeRede();
			detailsRede = getFichaRede32();

		} else if (rede.getMask() % 8 == 0) { // Caso sem sub-rede
			profileRede = getClassificacaoVisualDeRede();
			detailsRede = getFichaRede();
		}

		else if (rede.getMask() > 24) { // Caso de sub-rede com CIDR acima de 24,
			profileRede = getClassificacaoVisualDeSubRede();
			detailsRede = getFichasSubRedeC();
		}

		else { // Caso de Sub-rede com CIDR abaixo de 24
			profileRede = getClassificacaoVisualDeSubRede();
			detailsRede = new String[1];

		}

	}

	// GETS DE SAIDA
	public String[] getProfileRede() {
		return profileRede;
	}

	public String[] getDetailsRede() {
		return detailsRede;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	// CONSTRUÇÃO PRIMEIRA AREA
	private String[] getClassificacaoVisualDeRede() { // Montando a ficha da primeira Area
		String[] classificacaoVisual = new String[6];

		classificacaoVisual[0] = "ip: " + rede.getIp();
		classificacaoVisual[1] = "Classe: " + rede.getClasse();
		classificacaoVisual[2] = "Mascara (Decimal): " + rede.getDecimalMask();
		classificacaoVisual[3] = "Mascara (Binario): " + rede.getBinaryMask();
		classificacaoVisual[4] = "Quantidade de ips disponiveis: " + rede.getAvaliableIps();
		classificacaoVisual[5] = "Número de Redes: 1"; // TODO: Isso aqui tinha que ser uma variavel em Rede

		return classificacaoVisual;
	}

	private String[] getClassificacaoVisualDeSubRede() {
		String[] classificacaoVisual = new String[6];

		classificacaoVisual[0] = "ip: " + rede.getIp();
		classificacaoVisual[1] = "Classe: " + rede.getClasse();
		classificacaoVisual[2] = "Mascara (Decimal): " + rede.getDecimalMask();
		classificacaoVisual[3] = "Mascara (Binario): " + rede.getBinaryMask();
		classificacaoVisual[4] = "Quantidade de ips disponiveis: " + rede.getAvaliableIps();
		classificacaoVisual[5] = "Número de SubRedes: " + rede.getQuantSubRede();

		return classificacaoVisual;
	}

	// CONSTRUÇÃO SEGUNDA AREA
	public String[] getFichaRede() { // Sem Sub-Redes
		String[] listFicha;

		listFicha = new String[6];
		listFicha[0] = "REDE";
		listFicha[1] = "Endereço de rede: " + rede.getNetIp();
		listFicha[2] = "Primeiro ip válido: " + rede.getHostStart();
		listFicha[3] = "Último ip válido: " + rede.getHostEnd();
		listFicha[4] = "Endereço de broadcast: " + rede.getBroadcastIp();
		listFicha[5] = " ";

		return listFicha;
	}

	public String[] getFichasSubRedeC() { // Para nossas benditas Redes de CIDR 24+

		// Para fazer as fichas de várias subredes fiz um array de arrays
		// (buildFichasRedeC)
		// Esse método chama essa função e converte pra um array único, pro ListPane
		// aceitar

		String[][] fichasSubRedeC = buildFichasSubRedeC();
		String[] listFichas = new String[fichasSubRedeC.length * fichasSubRedeC[0].length];
		int k = 0;
		for (int i = 0; i < fichasSubRedeC.length; i++) {
			for (int j = 0; j < 6; j++) {
				listFichas[k] = fichasSubRedeC[i][j];
				k++;

			}
		}
		return listFichas;
	}

	private String[] getFichaRede32() {
		String[] listFichas = new String[6];

		listFichas[0] = "SUB-REDE";
		listFichas[1] = "Endereço de rede: " + rede.getIp();
		listFichas[2] = "Endereço de broadcast: " + rede.getIp();
		listFichas[3] = "Primeiro ip válido: N/A";
		listFichas[4] = "Último ip válido: N/A";
		listFichas[5] = " ";
		return listFichas;

	}

	private String[][] buildFichasSubRedeC() {
		String[][] fichasSubRede = new String[rede.getQuantSubRede() + 1][6];
		// Criando uma lista de todas as redes
		// o primeiro parâmetro vai ditar quantas redes, logo quantas fichas
		// o segundo é só pra formatar a ficha, 1 titulo, 3 campos de valores, uma linha
		// vazia

		for (int i = 0; i < rede.getQuantSubRede(); i++) {
			String[] ipSplit = rede.getIpSplit();
			String ipMasked = String.format("%s.%s.%s.", ipSplit[0], ipSplit[1], ipSplit[2]);

			int[] octetosDeRede = rede.getOctetosDeRede();
			int[] octetosDeBroadcast = rede.getOctetosDeBroadcast();
			int[] rangeStarts = rede.getRangeStarts();
			int[] rangeEnds = rede.getRangeEnds();

			fichasSubRede[i][0] = "SUB-REDE: " + (i + 1);
			fichasSubRede[i][1] = "Endereço de rede: " + ipMasked + "" + octetosDeRede[i];
			fichasSubRede[i][2] = "Primeiro ip válido: " + ipMasked + rangeStarts[i];
			fichasSubRede[i][3] = "Último ip válido: " + ipMasked + rangeEnds[i];
			fichasSubRede[i][4] = "Endereço de broadcast: " + ipMasked + octetosDeBroadcast[i];
			fichasSubRede[i][5] = " ";

		}
		return fichasSubRede;

	}
}
