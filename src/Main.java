import model.Menu;
import model.Rede;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	Menu menu = new Menu();
    	
    	menu.mostrarMenu();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello world!");
        System.out.println("Olá calculadora humana!");
        System.out.println("Esse é o classificador de ips, basta inserir um ip com máscara em formato CIDR");
        System.out.println("Ex: 192.168.0.0/24");

        String ip = scanner.nextLine();

        Rede rede = new Rede(ip);





    }

}
