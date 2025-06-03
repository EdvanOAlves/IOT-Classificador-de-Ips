package br.dev.edvan.classificador_de_Ip.model;

import br.dev.edvan.classificador_de_Ip.utils.RedeChecker;

public class FichaDeRede {
	Rede rede;
	private String[] profileRede;
	private String[] detailsRede;
	private String errorMessage;

	public FichaDeRede(String ipCidr) {
		rede = new Rede(ipCidr);

		errorMessage = RedeChecker.checkRede(rede);
		if (errorMessage.equals("none")) {
			makeFichas();
		}
	}

	// MAKE FICHAS, o método que vai decidir o tipo de fichas que vai fazer e como
	// fazer, diferenciar se é sub-rede, etc.
	//
	private void makeFichas() {
		if (rede.getMask() == 32) { // Caso CIDR 32, é um caso único
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
		// aceitar (listFichas)

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
