package br.dev.edvan.classificador_de_Ip.gui;


import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import br.dev.edvan.classificador_de_Ip.model.Rede;

public class ConversorTela {
	//
	// ATRIBUTOS
	//

	// Comunica��o com o us�rio e Input
	private JLabel labelIp;
	private JTextField textIpCidr;

	// Bot�es para executar os m�todos
	private JButton buttonClassificar;

	// Exibi��o e navega��o dos resultados
	private JList listClassificacao;
	private JScrollPane scrollClassificacao;
	private String tituloDaTela;

	//
	// M�TODOS
	//

	public void criarTela(String tituloDaTela) {
		// recebebendo os atributos
		this.tituloDaTela = tituloDaTela;

		// Instanciando e configurando o JFrame
		JFrame tela = new JFrame();
		tela.setTitle(this.tituloDaTela); // titulo da janela
		tela.setSize(405, 420); // tamanho da janela
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setResizable(false);

		// Criando referencias para os Bounds

		// Criando text labels para a janela
		tela.setLayout(null);
		labelIp = new JLabel();
		labelIp.setText("Insira Ip:");
		labelIp.setBounds(20, 20, 150, 30);

		// Criando text fields para a janela
		textIpCidr = new JTextField();
		textIpCidr.setBounds(20, 50, 200, 30);

		// Criando bot�es para a janela
		buttonClassificar = new JButton("Classificar");
		buttonClassificar.setBounds(20, 90, 100, 30);

		// Obtendo refer�ncia do Container, o painel de conteudo da janela
		Container container = tela.getContentPane();

		// Criar o JList que vai receber a tabuada
		listClassificacao = new JList();

		// Criar o Scrollpane que vai receber o JList
		scrollClassificacao = new JScrollPane(listClassificacao);
		scrollClassificacao.setBounds(20, 150, 350, 150);

		// Adicionando elementos na janela
		container.add(labelIp);

		container.add(textIpCidr);

		container.add(buttonClassificar);
		
		container.add(scrollClassificacao);

		// Adicionando escutantes de a��o aos bot�es
		buttonClassificar.addActionListener(new ActionListener() { // Funcionamento do bot�o Calcular

			@Override
			public void actionPerformed(ActionEvent e) {
				// Coletando os valores nos campos
				String ipCidr = textIpCidr.getText();

				// Fornecendo os valores para a tabuada
				Rede rede = new Rede(ipCidr);

				// Trazer a tabuada e colocar na tela
				String[] classificacaoVisual = rede.mostrarPerfil();
				listClassificacao.setListData(classificacaoVisual);

			}
		});

		// tornando a tela vis�vel
		tela.setVisible(true);
	}

	private void exibirTabuada() {

	}

	private void limparTabuada() {

	}

}
