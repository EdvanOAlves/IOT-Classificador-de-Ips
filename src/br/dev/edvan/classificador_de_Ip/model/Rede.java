package br.dev.edvan.classificador_de_Ip.model;

public class Rede {
	private String ip;
	private int octetLenght;
	private int firstOctet;
	private int mask;

	public String errorMessage = "none";

	public Rede(String ip) { // Rotina de tratamento do input, extrai as informacoes relevantes e expõe erros
		setIp(ip); // Guardando o input inicial

		try {
			extractMask();
			extractFirstOctet();
//			showNet();
		} catch (NumberFormatException exception) {
			errorMessage = "Formato inválido";

		} catch (Exception e2) {
			e2.printStackTrace();
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

	public void extractMask() {
		int maskIndex = (ip.indexOf("/") + 1);
		mask = Integer.parseInt(ip.substring(maskIndex));
		if (mask >32) {
			errorMessage = "CIDR acima de 32";
		}
	}
	//get do ip
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
        String decimalMask = "";
        if (mask % 8 == 0) { //Em caso de sem sub-redes
            for (int i = 1; i <= 4; i++) { //s�o 4 octetos, por isso 4 loops
                if (mask >= 8) {
                	decimalMask += "255.";
                    mask -= 8;
                } else {
                	decimalMask += "0.";
                }
            }
        } else {	//Em caso de com sub-redes
        	for (int i = 1; i<= 4; i++) {
        		if (mask == 0) {
        			decimalMask += "0.";
        		}
        		else if (mask > 0 && mask< 8) {
        			double disponiveisDecimal = Math.pow(2.0, (8-mask));
        			double subMaskDecimal = 256-disponiveisDecimal;
        			decimalMask += Integer.toString((int)(subMaskDecimal))+ ".";
        			mask = 0;
        		}
        		else if (mask >= 8) {
        			decimalMask += "255.";
        			mask -=8;
        		}
        		
        	}
        }
        decimalMask = decimalMask.substring(0, decimalMask.length() - 1);
        return decimalMask;
	}

	public String getBinaryMask() {
		String binaryMask = "";

		if (mask % 8 == 0) { // Caso Classfull
			for (int i = 1; i <= 4; i++) { //
				if (mask >= 8) {
					binaryMask += "11111111 ";
					mask -= 8;

				} else {
					binaryMask += "00000000 ";
				}
			}
			
		} else { // Caso subclasse
			int octetoCounter = 0;
			for (int i = 0; i < 32; i++) {

				if (mask > 0) {
					binaryMask += "1";
					mask -= 1;
					octetoCounter++;
					if (octetoCounter == 8) {
						binaryMask += " ";
						octetoCounter = 0;
					}
				} else {
					binaryMask += "0";
					octetoCounter++;
					if (octetoCounter == 8) {
						binaryMask += " ";
						octetoCounter = 0;
					}
				}
			}
		}

		binaryMask = binaryMask.substring(0, binaryMask.length() - 1);
		return binaryMask;
	}
	
	public int getAvaliableIps() {
		double avaliableIps = Math.pow(2.0, (32-mask));
        return ((int)avaliableIps-2);
	}

	
	//Métodos antigos de output

//	private void showNet() { //Metodo antigo de output, no console
//		System.out.println("Aqui as informacoes da sua rede: ");
//		System.out.println("ip: " + ip);
//		System.out.println("Classe: " + Conversor.classificarRede(firstOctet));
//		System.out.println("Mascara (decimal): " + Conversor.convertMaskDecimal(mask));
//		System.out.println("Mascara (binário): " + Conversor.convertMaskBinary(mask));
//		System.out.println("Quantidade de ips disponiveis: " + Conversor.calculateAvaliableIps(mask));
//	}


//	public String[] mostrarPerfil() { // Método antigo de output para a interface grafica
//		String[] perfil = new String[5];
//		if (errorMessage == "none") {
//			perfil[0] = "ip: " + ip; //
//			perfil[1] = "Classe: " + Conversor.classificarRede(firstOctet); //
//			perfil[2] = "Máscara (decimal): " + Conversor.convertMaskDecimal(mask);//
//			perfil[3] = "Máscara (binário): " + Conversor.convertMaskBinary(mask); //
//			perfil[4] = "Quantidade de ips disponíveis: " + Conversor.calculateAvaliableIps(mask);
//		} else {
//			perfil[0] = errorMessage;
//		}
//		return perfil;
//
//	}
}
