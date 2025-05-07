package br.dev.edvan.classificador_de_Ip.model;

public class Rede {
	private String ip;
	private int octetLenght;
	private int firstOctet;
	private int mask;

	public String errorMessage = "none";

	public Rede(String ip) { // Rotina de tratamento do input, extrai as informacoes relevantes e expõe erros
		setIp(ip); // Guardando o input inicial

		// TODO: bugs para capturar:

		// Lidar com a brecha no input "10.../24",
		// provavelmente vou precisar fazer um split ou usar um regex

		int periodCount = 0;
		for (int i = 0; i < ip.length(); i++) { // Verificação do String
			if (ip.charAt(i) == '.') { // contagem de pontos
				periodCount++;
			}
		}

		if (periodCount > 3 || periodCount < 3) { // Verificação, se tem mais de um ponto
			System.out.println(periodCount);
			errorMessage = "Insira 4 octetos devidamente separados";
		}

		// Trycatchs para exceções genéricas
		try {
			extractMask();
			extractFirstOctet();
		} catch (NumberFormatException exception) {
			errorMessage = "Formato inválido";

		} catch (StringIndexOutOfBoundsException exception) {
			errorMessage = "Formato inválido";

		} catch (Exception e) {
			errorMessage = "Formato inválido e sem preparo";
			e.printStackTrace();
		}
	}

	// Comandos usados no construtor
	private void setIp(String ip) {
		this.ip = ip;
	}

	public void extractFirstOctet() {
		octetLenght = (ip.indexOf("."));
		firstOctet = Integer.parseInt(ip.substring(0, octetLenght));
	}

	public void extractMask() { // Separando máscara CIDR do ip, comparando se é um valor válido de CIDR
		int maskIndex = (ip.indexOf("/") + 1);
		mask = Integer.parseInt(ip.substring(maskIndex));
		if ((mask < 0) || (mask > 32)) {
			errorMessage = "CIDR inválido";
		}
	}

	// get do ip
	public String getIp() {
		return ip;
	}

	// Gets com a lógica de conversão:

	public char getClasse() {
		char ipClasse = 'z';
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

	public String getBinaryMask() {
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

	public int getAvaliableIps() {
		double avaliableIps = Math.pow(2.0, (32 - mask));
		return ((int) avaliableIps - 2);
	}
}
