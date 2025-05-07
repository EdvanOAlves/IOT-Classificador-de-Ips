package br.dev.edvan.classificador_de_Ip;
import java.util.Scanner;

import br.dev.edvan.classificador_de_Ip.gui.ConversorTela;
import br.dev.edvan.classificador_de_Ip.model.Menu;
import br.dev.edvan.classificador_de_Ip.model.Rede;

public class Main {
    public static void main(String[] args) {
    	//INTERFACE EM TEXTO (Para troubleshooting)
//    	Menu menu = new Menu();
//    	menu.mostrarMenu();
//    	Scanner scanner = new Scanner(System.in);
//    	
//    	System.out.println("Hello world!");
//    	System.out.println("Olá calculadora humana!");
//    	System.out.println("Esse é o classificador de ips, basta inserir um ip com máscara em formato CIDR");
//    	System.out.println("Ex: 192.168.0.0/24");
//    	String ip = scanner.nextLine();
//    	Rede rede = new Rede(ip);
    	
    	//INTERFACE GR�FICA
    	ConversorTela tela = new ConversorTela();
    	tela.criarTela("Classificador de Ips");

//TODO:
    	//O sistema ainda precisa retornar quantas redes disponíveis naquele ip, isso vai incluir um calculo a mais em caso de sub-redes
    	//retornar ip de rede, de host, de broadcast
    	//Resolver pendencias no TODO do Trycatch




    }

}
