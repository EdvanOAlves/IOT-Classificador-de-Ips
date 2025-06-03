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
    	// Os nomes das variáveis e metodos em Rede tá uma bagunça, português, inglês, depois fazer uma operação de refactor
    	// Melhorar o funcionamento da interface gráfica, aceitar enter, retornar o foco para o campo de texto
    	// Detalhes de Sub Rede em Rede tem bastante redundância, inclui anotações explicando, só pesquisar "Detalhes de Sub Rede em Rede" na classe Rede
    }

}
