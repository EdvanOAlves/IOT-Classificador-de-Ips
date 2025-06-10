package br.dev.edvan.classificador_de_Ip.model;

import java.util.ArrayList;

import br.dev.edvan.classificador_de_Ip.utils.RedeChecker;

public class FichaDeRede {
	Rede rede;
	private String[] profileRede;
	private String[] detailsRede;
	private ArrayList<String> errorMessages;

	public FichaDeRede(String ipCidr) {
		rede = new Rede(ipCidr);

		errorMessages = RedeChecker.checkRede(rede);
		if (errorMessages.get(0).equals("none")) {
			makeFichas();
		}
	}

	// MAKE FICHAS, o metodo que vai decidir o tipo de fichas que vai fazer e como
	// fazer, diferenciar, considerar a existencia de sub-redes, etc.
	//
	private void makeFichas() {
		if (rede.getMask() == 32) { // Caso CIDR 32, e um caso unico
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
			detailsRede[0] = "A implementar metodo de super redes";

		}

	}

	// GETS DE SAIDA
	public String[] getProfileRede() {
		return profileRede;
	}

	public String[] getDetailsRede() {
		return detailsRede;
	}

	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	// CONSTRUCAO PRIMEIRA AREA
	private String[] getClassificacaoVisualDeRede() { // Montando a ficha da primeira Area
		String[] classificacaoVisual = new String[6];

		classificacaoVisual[0] = "ip: " + rede.getIp();
		classificacaoVisual[1] = "Classe: " + rede.getClasse();
		classificacaoVisual[2] = "Mascara (Decimal): " + rede.getDecimalMask();
		classificacaoVisual[3] = "Mascara (Binario): " + rede.getBinaryMask();
		classificacaoVisual[4] = "Quantidade de ips disponiveis: " + rede.getAvaliableIps();
		classificacaoVisual[5] = "Numero de Redes: 1"; // TODO: Isso aqui tinha que ser uma variavel em Rede

		return classificacaoVisual;
	}

	private String[] getClassificacaoVisualDeSubRede() {
		String[] classificacaoVisual = new String[6];

		classificacaoVisual[0] = "ip: " + rede.getIp();
		classificacaoVisual[1] = "Classe: " + rede.getClasse();
		classificacaoVisual[2] = "Mascara (Decimal): " + rede.getDecimalMask();
		classificacaoVisual[3] = "Mascara (Binario): " + rede.getBinaryMask();
		classificacaoVisual[4] = "Quantidade de ips disponiveis: " + rede.getAvaliableIps();
		classificacaoVisual[5] = "Numero de SubRedes: " + rede.getQuantSubRede();

		return classificacaoVisual;
	}

	// CONSTRUCAO SEGUNDA AREA
	public String[] getFichaRede() { // Sem Sub-Redes
		String[] listFicha;

		listFicha = new String[6];
		listFicha[0] = "REDE";
		listFicha[1] = "Endereco de rede: " + rede.getNetIp();
		listFicha[2] = "Primeiro ip valido: " + rede.getHostStart();
		listFicha[3] = "Ultimo ip valido: " + rede.getHostEnd();
		listFicha[4] = "Endereco de broadcast: " + rede.getBroadcastIp();
		listFicha[5] = " ";

		return listFicha;
	}

	public String[] getFichasSubRedeC() { // Para nossas benditas Redes de CIDR 24+

		// Para fazer as fichas de multiplas subredes fiz um array de arrays
		// (buildFichasRedeC)
		// Esse metodo chama essa funcao e converte pra um array unico, pro ListPane
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
		listFichas[1] = "Endereco de rede: " + rede.getIp();
		listFichas[2] = "Endereco de broadcast: " + rede.getIp();
		listFichas[3] = "Primeiro ip valido: N/A";
		listFichas[4] = "Ultimo ip valido: N/A";
		listFichas[5] = " ";
		return listFichas;

	}

	private String[][] buildFichasSubRedeC() {
		String[][] fichasSubRede = new String[rede.getQuantSubRede() + 1][6];
		// Criando uma lista de todas as redes
		// o primeiro parametro vai ditar quantas redes, logo quantas fichas
		// o segundo serve para formatar a ficha, 1 titulo, 3 campos de valores, uma linha
		// vazia

		for (int i = 0; i < rede.getQuantSubRede(); i++) {
			String[] ipSplit = rede.getIpSplit();
			String ipMasked = String.format("%s.%s.%s.", ipSplit[0], ipSplit[1], ipSplit[2]);

			int[] octetosDeRede = rede.getOctetosDeRede();
			int[] octetosDeBroadcast = rede.getOctetosDeBroadcast();
			int[] rangeStarts = rede.getRangeStarts();
			int[] rangeEnds = rede.getRangeEnds();

			fichasSubRede[i][0] = "SUB-REDE: " + (i + 1);
			fichasSubRede[i][1] = "Endereco de rede: " + ipMasked + "" + octetosDeRede[i];
			fichasSubRede[i][2] = "Primeiro ip valido: " + ipMasked + rangeStarts[i];
			fichasSubRede[i][3] = "Ultimo ip valido: " + ipMasked + rangeEnds[i];
			fichasSubRede[i][4] = "Endereco de broadcast: " + ipMasked + octetosDeBroadcast[i];
			fichasSubRede[i][5] = " ";

		}
		return fichasSubRede;

	}
}
