package model;

public class Rede {
    private String ip;
    private int octetLenght;
    private int firstOctet;
    private int maskIndex;
    private int mask;

    public Rede(String ip){
        this.ip = ip;
        extractMask();
        extractFirstOctet();
        showNet();

    }

    private void extractMaskIndex(){
        maskIndex = (ip.indexOf("/")+1);
    }

    private void extractMask(){
        extractMaskIndex();
        mask = Integer.parseInt(ip.substring(maskIndex));
    }

    private void extractFirstOctet(){
        octetLenght = (ip.indexOf("."));
        firstOctet = Integer.parseInt(ip.substring(0,octetLenght));

    }

    private void showNet(){
        System.out.println("Aqui as informa��es da sua rede: ");
        System.out.println("ip: "+ ip);
        System.out.println("Classe: " + Conversor.classificarRede(firstOctet));
        System.out.println("M�scara (decimal): "+ Conversor.convertMaskDecimal(mask));
        System.out.println("M�scara (bin�rio): "+ Conversor.convertMaskBinary(mask));
        System.out.println("Quantidade de ips dispon�veis: "+  Conversor.calculateAvaliableIps(mask));

    }



}
