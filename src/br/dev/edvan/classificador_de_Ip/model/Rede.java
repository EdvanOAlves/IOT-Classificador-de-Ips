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

	public Rede(String ip) { //Método construtor, 
		setIp(ip); // Guardando o input inicial
		try {
			extractMask();
			splitIp();
			tamanhoSubRede = (int) (Math.pow(2, 8 - (mask-24)));
			//2^<bits disponíveis>
				//<bits disponíveis> = 8-<Máscara do Octeto>
				//<Máscara do Octeto> = mask-24
			
			//TODO esse tamanhoSubRede precisa ser ajustado ainda
			
		} catch (NumberFormatException exception) { //Previnindo input de letras
			errorMessage = "Formato inválido";

		} catch (StringIndexOutOfBoundsException exception) {
			errorMessage = "Formato inválido";

		} catch (NullPointerException exception) {
			errorMessage = "Formato inválido";

		} catch (Exception e) {
			// Deixei esse aqui pra previnir tudo enquanto fica em aberto para em testes achar
			// mais Exceções que façam sentido incluir no software
			errorMessage = "Formato inválido e sem preparo";
			e.printStackTrace();
		}

		// Verificações manuais

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

	// Comandos usados no construtor

	
	private void setIp(String ip) {
		this.ip = ip;
	}

	private void splitIp() {
		String ipIsolated[] = ip.split("/"); // Separando ip do CIDR
		ipSplit = (ipIsolated[0]).split("\\."); // Separando ip por octetos
		
		//Verificação de um dos bugs, ela precisa ficar dentro do trycatch para detectar o bug dos símbolos especiais
		if (ipSplit.length < 4) { // Resolvendo bug de input "10.../25"
			errorMessage = "Formato inválido, faltam valores de octetos";
		}
		
		// Verificando se tá no range de 0 e 255
		for (int i = 0 ; i < ipSplit.length ; i++) { 
			if (Integer.parseInt(ipSplit[i]) > 255 || Integer.parseInt(ipSplit[i]) < 0) {
				errorMessage = "Formato inválido, octetos devem estar entre 0 e 255";
				
			}
		}
	}
	
	public String[] getIpSplit(){
		return ipSplit;
		
	}

	public void extractMask() { // Separando máscara CIDR do ip, comparando se é um valor válido de CIDR
		int maskIndex = (ip.indexOf("/") + 1);
		mask = Integer.parseInt(ip.substring(maskIndex));
		if ((mask < 0) || (mask > 32)) {
			errorMessage = "CIDR inválido";
		}
	}
	
	public int getMask() {
		return mask;
	//TODO: Tem redundancia aqui, poderia trocar o ExtractMask pelo get, e substituir toda
	//vez que chamam essa máscara, fica aí melhoria
		
	}

	// get do ip
	public String getIp() {
		return ip;
	}

	// Gets com a lógica de conversão:

	public char getClasse() {
		char ipClasse = 'z';
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

	public String getDecimalMask() {
		String decimalMask = ""; // Iniciando a variável
		int maskRef = mask; // Fazendo uma referencia da mascara para não alterar o valor original
		for (int i = 1; i <= 4; i++) {
			if (maskRef == 0) {
				decimalMask += "0.";
			} else if (maskRef > 0 && maskRef < 8) {
				double disponiveisDecimal = Math.pow(2.0, (8 - maskRef));
				int subMaskDecimal = (int) (256 - disponiveisDecimal);
				decimalMask += (Integer.toString(subMaskDecimal)) + ".";
				maskRef = 0;
			} else if (maskRef >= 8) {
				decimalMask += "255.";
				maskRef -= 8;
			}
		}
		decimalMask = decimalMask.substring(0, decimalMask.length() - 1);
		return decimalMask;
	}

	public String getBinaryMask() { //Conversão da máscara para binário
		String binaryMask = "";
		int maskRef = mask;
		int octetoCounter = 0; // Contagem para separar em octetos
		for (int i = 0; i < 32; i++) {
			if (maskRef > 0) {
				binaryMask += "1";
				maskRef -= 1;
				octetoCounter++;
			} else {
				binaryMask += "0";
				octetoCounter++;
			}
			if (octetoCounter == 8) {
				binaryMask += " ";
				octetoCounter = 0;
			}
		}

		binaryMask = binaryMask.substring(0, binaryMask.length() - 1);
		return binaryMask;
	}

	public int getAvaliableIps() { //Calculando a quantidade de endereços disponíveis
		double avaliableIps = Math.pow(2.0, (32 - mask));
		return ((int) avaliableIps - 2);
	}
	
	/*
	*ULTIMA SPRINT: Fazer aqui as últimas funcionalidades para classificação de sub-redes acima
	*de 24, as funcionalidades:
	*
	*-Ips de rede e Broadcasts
	*-Range de ips disponíveis para host
	*
	*Brainstorm:
	*
	*Variáveis que vou precisar, exemplo CIDR 26
	*	quantSubRedes : pra saber quantos loops rodar, nesse caso, 4
	*	octetosDeRede: que vai ser o 0 e 64 //rede é sempre os redondinho
	*	octetosDeBroadcast: que vai ser o 63, 127, 191, 255 //broadcast é os redondinho -1
	*	octetosRangeStart: que vai ser o 1, 65, 129... //
	*	octetosRangeEnd: que vai ser o 62, 
	*
	*
	*Métodos
	*	getQuantSubRedes
	*	getOctetosDeRede
	*	getOctetosDeBroadcast
	*	getOctetoRangeStart
	*	getOctetoRangeEnd
	*	//Intuitivos, só vão extrair números com a máscara CIDR
	*
	*	displayIp(int octeto)	
	*	//vai receber algum dos octetos e juntar com os outros octetos, 
	*	pra poder estruturar certinho
	*
	*	
	*	
	*/
	
	public int getQuantSubRede() {
		return (256/tamanhoSubRede);
	}
	
	public int[] getOctetosDeRede() {
		int[] octetosDeRede = new int[getQuantSubRede()];
		
		for  (int i = 0; i < octetosDeRede.length ; i++) {
			octetosDeRede[i] = tamanhoSubRede*i;
		}
		
		return octetosDeRede;
	}

	public int[] getOctetosDeBroadcast() {
		int[] octetosDeBroadcast = new int[getQuantSubRede()];
		
		for (int i = 0; i < octetosDeBroadcast.length ; i++) {
			octetosDeBroadcast[i] =(i*tamanhoSubRede) + (tamanhoSubRede-1);
		}
		
		return octetosDeBroadcast;
	}

	public int[] getRangeStarts() {
		rangeStarts = new int[getQuantSubRede()];
		
		for (int i = 0; i < rangeStarts.length ; i++) {
			rangeStarts[i] = (i*tamanhoSubRede) + 1;
		}
		
		return rangeStarts;
	}

	public int[] getRangeEnds() {
		rangeEnds = new int[getQuantSubRede()];
		
		for  (int i = 0; i < rangeEnds.length ; i++) {
			rangeEnds[i] = (i*tamanhoSubRede) + (tamanhoSubRede-2);
			if (rangeEnds[i] < rangeStarts[i]) {
				rangeEnds[i] = rangeStarts[i];
			}
		}

		return rangeEnds;
	}
}

	






	
