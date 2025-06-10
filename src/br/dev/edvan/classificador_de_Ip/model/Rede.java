package br.dev.edvan.classificador_de_Ip.model;

public class Rede {
	private String ip;
	private int octetLenght;
	private int firstOctet;
	private int mask;
	private String[] ipIsolated = new String[2];
	private String[] ipSplit;

	int[] rangeStarts;
	int[] rangeEnds;

	public Rede(String ip) { // Metodo construtor,
		setIp(ip); // Guardando o input inicial
		isolateIp(); //
		splitIp(); // Separando o array de ips
	}

	private void isolateIp() {
		ipIsolated = ip.split("/"); // Separando ip do CIDR
	}

	private void splitIp() {
		if (ipIsolated.length < 2) {
			ipIsolated = new String[]{ipIsolated[0], "-1"}; // Caso o CIDR esteja vazio, isso encaminha pro erro certo
		}
		ipSplit = (ipIsolated[0]).split("\\."); // Separando ip por octetos
		mask = Integer.parseInt(ipIsolated[1]);

	}

	// GETS E SETS

	private void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		// get do ip input inicial
		return ip;
	}

	public String[] getIpIsolated() {
		// get do ip separado entre endereco e CIDR
		return ipIsolated;
	}

	public int getTamanhoDeRede() {
		int brokenCidr = 0;
		if (mask % 8 == 0) {

		} else if (mask > 24) {
			brokenCidr = mask - 24;

		} else if (mask > 16) {
			brokenCidr = mask - 16;
		} else if (mask > 8) {
			brokenCidr = mask - 8;
		} else {
			brokenCidr = mask;
		}

		return (int) (Math.pow(2, 8 - brokenCidr));
		// 2^<bits disponiveis>
		// <bits disponíveis> = 8-<Mascara do Octeto>
	}

	public String[] getIpSplit() {
		return ipSplit;
	}

	public int getMask() {
		return mask;
	}

	// Gets de valores convertidos, públicos para acesso da ficha que leva a
	// interface Grafica

	public char getClasse() {
		char ipClasse = 'z'; // Valor inicial alerta de falhas
		int firstOctet = Integer.parseInt(ipSplit[0]);

		if (firstOctet <= 127) {
			ipClasse = 'A';
		} else if (firstOctet <= 191) {
			ipClasse = 'B';
		} else if (firstOctet <= 223) {
			ipClasse = 'C';
		} else if (firstOctet <= 239) {
			ipClasse = 'D';
		} else if (firstOctet <= 255) {
			ipClasse = 'E';
		}
		return ipClasse;
	}

	public String getDecimalMask() { // metodo publico para retornar uma String da mascara
		int[] splitDecimalMask = extractDecimalMask();
		String decimalMask = String.format("%d.%d.%d.%d", splitDecimalMask[0], splitDecimalMask[1], splitDecimalMask[2],
				splitDecimalMask[3]);
		return decimalMask;
	}

	public String getBinaryMask() {
		String[] splitBinaryMask = extractBinaryMask();
		String binaryMask = String.format("%s %s %s %s", splitBinaryMask[0], splitBinaryMask[1], splitBinaryMask[2],
				splitBinaryMask[3]);
		return binaryMask;
	}

	public int getAvaliableIps() { // Calculando a quantidade de enderecos disponiveis
		double avaliableIps = Math.pow(2.0, (32 - mask));
		return ((int) avaliableIps - 2);
	}

	// Funcoes privadas para auxiliar os gets publicos:
	private int[] extractDecimalMask() {
		int[] splitDecimalMask = new int[4]; // Iniciando array

		int maskRef = mask; // Fazendo uma referencia da mascara para nao alterar o valor original
		for (int i = 0; i < 4; i++) { // Loop que vai montar o array de octetos
			if (maskRef == 0) {
				splitDecimalMask[i] = 0;
			} else if (maskRef > 0 && maskRef < 8) {
				double disponiveisDecimal = Math.pow(2.0, (8 - maskRef));
				splitDecimalMask[i] = (int) (256 - disponiveisDecimal);
				maskRef = 0;
			} else if (maskRef >= 8) {
				splitDecimalMask[i] = 255;
				maskRef -= 8;
			}
		}
		return splitDecimalMask;
	}

	private String[] extractBinaryMask() { // Conversao da mascara para binario
		String[] splitBinaryMask = new String[4];
		int maskRef = mask;
		for (int i = 0; i < 4; i++) {
			if (maskRef > 8) { // Caso Octeto Cheio
				splitBinaryMask[i] = "11111111";
				maskRef -= 8;
			} else if (maskRef > 0) { // Caso Octeto de subclasse
				String subClassBinaryOctet = "";
				for (int j = 0; j < 8; j++) {
					if (maskRef == 0) {
						subClassBinaryOctet += 0;
					} else {
						subClassBinaryOctet += 1;
						maskRef--;
					}
				}
				splitBinaryMask[i] = subClassBinaryOctet;
			} else { // Caso Octeto vazio
				splitBinaryMask[i] = "00000000";
			}
		}
		return splitBinaryMask;
	}

	/*
	 * 
	 * SUB-REDES (Aqui sao metodos que foram criados para atender a demanda extra do
	 * professor de exibir mais informac�es em caso de Sub-Redes com mascara CIDR
	 * acima de 24, se um dia eu decidir revisitar esse programa vou reajustar para
	 * funcionar com as Sub-redes abaixo de CIDR 24)
	 * 
	 */

	public int getQuantSubRede() {
		return (256 / getTamanhoDeRede());
	}

	public int[] getOctetosDeRede() {
		int[] octetosDeRede = new int[getQuantSubRede()];

		for (int i = 0; i < octetosDeRede.length; i++) {
			octetosDeRede[i] = getTamanhoDeRede() * i;
		}

		return octetosDeRede;
	}

	public int[] getOctetosDeBroadcast() {
		int[] octetosDeBroadcast = new int[getQuantSubRede()];

		for (int i = 0; i < octetosDeBroadcast.length; i++) {
			octetosDeBroadcast[i] = (i * getTamanhoDeRede()) + (getTamanhoDeRede() - 1);
		}

		return octetosDeBroadcast;
	}

	public int[] getRangeStarts() {
		rangeStarts = new int[getQuantSubRede()];

		for (int i = 0; i < rangeStarts.length; i++) {
			rangeStarts[i] = (i * getTamanhoDeRede()) + 1;
		}

		return rangeStarts;
	}

	public int[] getRangeEnds() {
		int tamanhoDeRede = getTamanhoDeRede();
		rangeEnds = new int[getQuantSubRede()];

		for (int i = 0; i < rangeEnds.length; i++) {
			rangeEnds[i] = (i * tamanhoDeRede) + (tamanhoDeRede - 2);
			if (rangeEnds[i] < rangeStarts[i]) {
				rangeEnds[i] = rangeStarts[i];
			}
		}

		return rangeEnds;
	}

	// EXTRA: Detalhes de Sub Rede em Rede
	/* 
	 * TODO: Tem bastante redundancia aqui que poderia receber uma reordenada para deixar o codigo mais limpo:
	 * 		hostStart[n] sempre vai ser netIp[n] +1, 
	 * 		hostEnd[n] sempr vai ser broadCastIp[n]-1.
	 * 
	 * 		Todos preenchem os outros octetos de acordo com a macara. Poderia ser feita uma funcao que lida com
	 * 		a parte mascarada e essas outras funcoes alteram somente o ultimo octeto
	 * 
	 * 		Por enquanto vou deixar a redundancia porque ja atende os requisitos do projeto
	 *  
	 */

	public String getNetIp() {
		String[] netIp = ipSplit;
		
		for (int i = mask/8; !(i == 4) ; i++) { //For loop para zerar todos os octetos nao-mascarados
			netIp[i] = "0";
		}

		return netIp[0] + "." + netIp[1] + "." + netIp[2] + "." + netIp[3];
	}

	public String getHostStart() {
		String[] hostStart = ipSplit;
		
		for (int i = mask/8; !(i == 4) ; i++) { //For loop para zerar todos os octetos não mascarados
			hostStart[i] = "0";
		}

		hostStart[3] = "1"; // O primeiro ip valido vai ter sempre 1 no ultimo octeto

		return hostStart[0] + "." + hostStart[1] + "." + hostStart[2] + "." + hostStart[3];
	}

	public String getHostEnd() {
		String[] hostEnd = ipSplit;
		
		for (int i = mask/8; !(i == 4) ; i++) { //For loop para zerar todos os octetos não mascarados
			hostEnd[i] = "255";
		}
		
		hostEnd[3] = "254"; // O ultimo ip valido vai ter sempre 254 no ultimo octeto

		return hostEnd[0] + "." + hostEnd[1] + "." + hostEnd[2] + "." + hostEnd[3];

	}
	
	public String getBroadcastIp() {
		String[] broadcastIp = ipSplit;
		
		for (int i = mask/8; !(i == 4) ; i++) { //For loop para zerar todos os octetos nao-mascarados
			broadcastIp[i] = "255";
		}
		
		return broadcastIp[0] + "." + broadcastIp[1] + "." + broadcastIp[2] + "." + broadcastIp[3];
	}

}
