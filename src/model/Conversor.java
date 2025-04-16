package model;

public class Conversor{

    public static char classificarRede(int firstOctet) {
        char ipClass = 'z';
        if (firstOctet <= 127) {
            System.out.println(firstOctet);
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
        if (mask % 8 == 0) { //Verificando se não tem sub-redes
            for (int i = 1; i <= 4; i++) { //são 4 octetos, por isso 4 loops
                if (mask >= 8) {
                    maskDecimal += "255.";
                    mask -= 8;
                } else {
                    maskDecimal += "0.";
                }
            }
            maskDecimal = maskDecimal.substring(0, maskDecimal.length() - 1);
        } else {
            System.out.println("Ainda não foi implementada função de sub-redes");
        }
        return maskDecimal;
    }

    public static String convertMaskBinary(int mask) {
        String maskBinary = "";

        if (mask % 8 == 0) {
            for (int i = 1; i <= 4; i++) { //
                if (mask >= 8) {
                    maskBinary += "1111 ";
                    mask -= 8;

                } else {
                    maskBinary += "0000 ";
                }

            }
            maskBinary = maskBinary.substring(0, maskBinary.length() - 1);
        }
        return maskBinary;
    }

    public static int calculateAvaliableIps(int mask){
        int avaliableIps = 0;
        if (mask % 8 == 0) { //Verificando se não tem sub-redes
            for (int i = 1; i <= 4; i++) { //são 4 octetos, por isso 4 loops
                if (!(mask >= 8)) {
                    if (avaliableIps == 0) {
                        avaliableIps += 255;
                    } else {
                        avaliableIps *= 255;
                    }
                    mask -= 8;
                }
                else {
                    mask -=8;
                }
            }


        }
        return avaliableIps-2;
    }
}

