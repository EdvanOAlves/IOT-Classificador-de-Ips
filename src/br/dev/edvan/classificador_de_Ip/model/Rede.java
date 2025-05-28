package br.dev.edvan.classificador_de_Ip.model;

public class Rede {
	private String ip;
	private int octetLenght;
	private int firstOctet;
	private int mask;
	private String[] ipSplit;
	private int tamanhoSubRede;

	int[] rangeStarts;
	int[] rangeEnds;

	public String errorMessage = "none";

	public Rede(String ip) { // Método construtor,
		try {
			setIp(ip); // Guardando o input inicial
			extractMask();
			splitIp();
			tamanhoSubRede = (int) (Math.pow(2, 8 - (mask - 24)));
			// 2^<bits disponíveis>
			// <bits disponíveis> = 8-<Máscara do Octeto>
			// <Máscara do Octeto> = mask-24

			// TODO esse tamanhoSubRede precisa ser ajustado ainda

		} catch (NumberFormatException exception) { // Previnindo input de letras
			errorMessage = "Formato inválido";
		} catch (StringIndexOutOfBoundsException exception) {
			errorMessage = "Formato inválido";
		} catch (NullPointerException exception) {
			errorMessage = "Formato inválido";
		} catch (Exception e) {
			// Deixei esse aqui pra previnir tudo enquanto fica em aberto para em testes
			// achar
			// mais Exceções que façam sentido incluir no software
			errorMessage = "Formato inválido e sem preparo";
			e.printStackTrace();
		}

	}

	// Comandos usados no construtor
	private void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		// get do ip input inicial
		return ip;
	}

	private void splitIp() {
		String ipIsolated[] = ip.split("/"); // Separando ip do CIDR
		ipSplit = (ipIsolated[0]).split("\\."); // Separando ip por octetos
		ipVerify();
	}

	public void extractMask() { // Separando máscara CIDR do ip, comparando se é um valor válido de CIDR
		int maskIndex = (ip.indexOf("/") + 1);
		mask = Integer.parseInt(ip.substring(maskIndex));
		checkCidr();
	}

	// VERIFICAÇÕES DE ERROS MANUALMENTE
	private void ipVerify() {
		checkPeriods();
		checkOctets();
	}

	private void checkPeriods() {
		int periodCount = 0;
		for (int i = 0; i < ip.length(); i++) { // Verificação do String
			if (ip.charAt(i) == '.') { // contagem de pontos
				periodCount++;
			}
		}
		if (periodCount > 3 || periodCount < 3) { // Verificação, se a quantidade de pontos está adequadas
			errorMessage = "Insira 4 octetos devidamente separados";
		}
	}

	private void checkOctets() {
		// Verificação de um dos bugs de octetos vazios
		if (ipSplit.length < 4) { // Resolvendo bug de input "10.../25", octetos vazios basicamente
			errorMessage = "Formato inválido, faltam valores de octetos";
		}

		// Verificando se tá no range de 0 e 255
		for (int i = 0; i < ipSplit.length; i++) {
			if (Integer.parseInt(ipSplit[i]) > 255 || Integer.parseInt(ipSplit[i]) < 0) {
				errorMessage = "Formato inválido, octetos devem estar entre 0 e 255";
			}
		}
	}

	private void checkCidr() {
		if ((mask < 0) || (mask > 32)) {
			errorMessage = "CIDR inválido";
		}
	}

	// GETS E SETS

	public String[] getIpSplit() {
		return ipSplit;
	}

	public int getMask() {
		return mask;
	}

	// Gets de valores convertidos, públicos para acesso da interface gráfica

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

	public String getDecimalMask() { // método público para retornar uma String da máscara
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

	public int getAvaliableIps() { // Calculando a quantidade de endereços disponíveis
		double avaliableIps = Math.pow(2.0, (32 - mask));
		return ((int) avaliableIps - 2);
		
		//TODO ips disponíveis no caso de sub-rede 25+ lá, ele precisa desconsiderar 
		//os endereços de Rede e Broadcast imagino eu
	}

	// Funções privadas para auxiliar os gets públicos:
	private int[] extractDecimalMask() {
		int[] splitDecimalMask = new int[4]; // Iniciando array

		int maskRef = mask; // Fazendo uma referencia da mascara para não alterar o valor original
		for (int i = 1; i <= 4; i++) { //Loop que vai montar o array de octetos
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

	private String[] extractBinaryMask() { // Conversão da máscara para binário
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
	
	//SUB-REDES
	/* Então. Aqui é toda uma nova funcionalidade que não estava muito claro o que precisávamos fazer até
	 * tudo acima ter sido resolvido. Por enquanto vai ser um trecho aqui de redes dedicado a isso
	 * porque para o que foi pedido já atende. Fica o desafio caso um dia eu queira revisitar o ideal
	 * seria criar uma nova subclasse
	*/
	public int getQuantSubRede() {
		return (256 / tamanhoSubRede);
	}

	public int[] getOctetosDeRede() {
		int[] octetosDeRede = new int[getQuantSubRede()];

		for (int i = 0; i < octetosDeRede.length; i++) {
			octetosDeRede[i] = tamanhoSubRede * i;
		}

		return octetosDeRede;
	}

	public int[] getOctetosDeBroadcast() {
		int[] octetosDeBroadcast = new int[getQuantSubRede()];

		for (int i = 0; i < octetosDeBroadcast.length; i++) {
			octetosDeBroadcast[i] = (i * tamanhoSubRede) + (tamanhoSubRede - 1);
		}

		return octetosDeBroadcast;
	}

	public int[] getRangeStarts() {
		rangeStarts = new int[getQuantSubRede()];

		for (int i = 0; i < rangeStarts.length; i++) {
			rangeStarts[i] = (i * tamanhoSubRede) + 1;
		}

		return rangeStarts;
	}

	public int[] getRangeEnds() {
		rangeEnds = new int[getQuantSubRede()];

		for (int i = 0; i < rangeEnds.length; i++) {
			rangeEnds[i] = (i * tamanhoSubRede) + (tamanhoSubRede - 2);
			if (rangeEnds[i] < rangeStarts[i]) {
				rangeEnds[i] = rangeStarts[i];
			}
		}

		return rangeEnds;
	}
}
