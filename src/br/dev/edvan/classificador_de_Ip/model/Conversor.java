package br.dev.edvan.classificador_de_Ip.model;

public class Conversor{

    public static char classificarRede(int firstOctet) {
        char ipClass = 'z';
        if (firstOctet <= 127) {
            ipClass = 'A';
        } else if (firstOctet <= 191) {
            ipClass = 'B';
        } else if (firstOctet <= 223) {
            ipClass = 'C';
        }
        else {
            System.out.println("Alguma coisa deu errado");
        }
        return ipClass;


    }

    public static String convertMaskDecimal(int mask) {
        String maskDecimal = "";
        if (mask % 8 == 0) { //Em caso de sem sub-redes
            for (int i = 1; i <= 4; i++) { //s�o 4 octetos, por isso 4 loops
                if (mask >= 8) {
                    maskDecimal += "255.";
                    mask -= 8;
                } else {
                    maskDecimal += "0.";
                }
            }
        } else {	//Em caso de com sub-redes
        	for (int i = 1; i<= 4; i++) {
        		if (mask == 0) {
        			maskDecimal += "0.";
        		}
        		else if (mask > 0 && mask< 8) {
        			double disponiveisDecimal = Math.pow(2.0, (8-mask));
        			double subMaskDecimal = 256-disponiveisDecimal;
        			maskDecimal += Integer.toString((int)(subMaskDecimal))+ ".";
        			mask = 0;
        		}
        		else if (mask >= 8) {
        			maskDecimal += "255.";
        			mask -=8;
        		}
        		
        	}
        }
        maskDecimal = maskDecimal.substring(0, maskDecimal.length() - 1);
        return maskDecimal;
    }

    public static String convertMaskBinary(int mask) {
        String maskBinary = "";

        if (mask % 8 == 0) {
            for (int i = 1; i <= 4; i++) { //
                if (mask >= 8) {
                    maskBinary += "11111111 ";
                    mask -= 8;

                } else {
                    maskBinary += "00000000 ";
                }
            } 
        }
        else {
        	int octetoCounter = 0;
        	for (int i = 0; i < 32; i++) {
        		
        		if (mask>0) {
        			maskBinary += "1";
        			mask-=1;
        			octetoCounter++;
        			if (octetoCounter == 8) {
        				maskBinary += " ";
        				octetoCounter = 0;
        			}
        		}
        		else {
        			maskBinary += "0";
        			octetoCounter++;
        			if (octetoCounter == 8) {
        				maskBinary += " ";
        				octetoCounter = 0;
        			}
        		}
        	}
        }
        
        maskBinary = maskBinary.substring(0, maskBinary.length() - 1);
        return maskBinary;
    }

    public static int calculateAvaliableIps(int mask){    	
    	double avaliableIps = Math.pow(2.0, (32-mask));
        return ((int)avaliableIps-2);
    }
}

