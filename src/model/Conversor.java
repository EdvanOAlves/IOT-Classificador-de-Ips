package model;

public class Conversor {
    public static char classificarRede(int firstOctet){
        return 0;

    }

    public static String convertMaskDecimal(int mask){
        String maskDecimal = "";
        if (mask % 8 == 0) { //Verificando se não tem sub-redes
            for (int i = 1 ; i <=4; i++){
                if (mask >= 8){
                    maskDecimal +="255.";
                    mask -=8;
                }
                else{
                    maskDecimal += "0.";
                }
            }
            maskDecimal = maskDecimal.substring(0, maskDecimal.length()-1);


        }
        else{
            System.out.println("Ainda não foi implementada função de sub-redes");
        }
        return maskDecimal;
    }

    public static String convertMaskBinary(int mask){

        return "a";

    }


}
