package br.dev.edvan.classificador_de_Ip;
import java.util.Scanner;

import br.dev.edvan.classificador_de_Ip.gui.ConversorTela;
import br.dev.edvan.classificador_de_Ip.model.Rede;

public class Main {
    public static void main(String[] args) {    	
    	//INTERFACE GRAFICA
    	
    	ConversorTela tela = new ConversorTela();
    	tela.criarTela("Classificador de Ips");

//TODO:
    	//O sistema ainda precisa retornar quantas redes disponíveis naquele ip, isso vai incluir um calculo a mais em caso de sub-redes
    	//retornar ip de rede, de host, de broadcast
    		//Para esses dois vou usar o ipSplit, também seria bacana substituir no getClass para otimização 
    	//Os nomes das variáveis e metodos em Rede tá uma bagunça, português, inglês, depois fazer uma operação de refactor
    	//Do jeito que eu fiz para subredes em classe C ele quebrou o diagnostico de erros, consertar depois
    }

}
