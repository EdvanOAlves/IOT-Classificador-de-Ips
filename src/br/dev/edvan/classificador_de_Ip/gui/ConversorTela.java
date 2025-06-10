package br.dev.edvan.classificador_de_Ip.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import br.dev.edvan.classificador_de_Ip.model.FichaDeRede;
import br.dev.edvan.classificador_de_Ip.model.Rede;
import br.dev.edvan.classificador_de_Ip.utils.RedeChecker;

public class ConversorTela {
	//
	// DECLARANDO OS ELEMENTOS
	//

	// Comunicacao com o usuario e Input
	private JLabel labelIp;
	private JLabel labelError;
	private JTextField textIp;

	// Botao de Start
	private JButton buttonClassificar;

	// Exibicao e navegacao dos resultados
	private JList listClassificacao;
	private JList listDetails;
	private JScrollPane scrollClassificacao;
	private JScrollPane scrollDadosSubRede;
	private String tituloDaTela;

	//
	// METODO DE INICIALIZAÇÃO
	//

	public void criarTela(String tituloDaTela) {
		// recebebendo os atributos
		this.tituloDaTela = tituloDaTela;

		// Instanciando e configurando o JFrame
		JFrame tela = new JFrame();
		tela.setLayout(null);
		tela.setTitle(this.tituloDaTela); // titulo da janela
		tela.setSize(450, 700); // tamanho da janela
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setResizable(false);

		// Inserindo valores nos text labels (Para pedir o input e retornar os erros)
		labelIp = new JLabel();
		labelIp.setText("Insira Ip:");
		labelIp.setBounds(20, 20, 150, 30);

		labelError = new JLabel();
		labelError.setBounds(20, 120, 385, 30);
		labelError.setForeground(Color.red);
		

		// Text field para input
		textIp = new JTextField();
		textIp.setBounds(20, 50, 200, 30);

		// Botão
		buttonClassificar = new JButton("Classificar");
		buttonClassificar.setBounds(20, 85, 100, 30);

		// Obtendo referencia do Container, o painel de conteudo da janela
		Container container = tela.getContentPane();

		// JList e ScrollPane que vai receber o output para as informacoes basicas
		listClassificacao = new JList();
		scrollClassificacao = new JScrollPane(listClassificacao);
		scrollClassificacao.setBounds(20, 150, 395, 115);

		// JList e ScrollPane Para dados de sub-Redes acima de 24 CIDR

		listDetails = new JList();
		scrollDadosSubRede = new JScrollPane(listDetails);
		scrollDadosSubRede.setBounds(20, 280, 395, 350);

		// Adicionando elementos na janela
		container.add(labelIp);

		container.add(textIp);

		container.add(labelError);

		container.add(buttonClassificar);

		container.add(scrollClassificacao);

		container.add(scrollDadosSubRede);

		// tornando a tela visivel
		tela.setVisible(true);

		// Adicionando ouvintes de acao ao botao
		buttonClassificar.addActionListener(new ActionListener() { // Funcionamento do botao Calcular

			@Override
			public void actionPerformed(ActionEvent e) {
				runMethods();
				textIp.requestFocus();
			}

		});
	}

	private void runMethods() {
		// Passando os valores do campo e criando o objeto
		String ipCidr = textIp.getText();

		ArrayList<String> inputErrorMessages = RedeChecker.checkInput(ipCidr);
		if (inputErrorMessages.get(0).equals("none")) {
			FichaDeRede ficha = new FichaDeRede(ipCidr);
			displayResults(ficha);
		} else {
			labelError.setText("Input invalido, erros detectados:");
			displayErrorMessage(inputErrorMessages);
		}

	}

	private void displayResults(FichaDeRede ficha) {
		if (ficha.getErrorMessages().get(0).equals("none")) { // Verificacao, se nao houve nenhum erro
			listClassificacao.setForeground(Color.black);
			listClassificacao.setListData(ficha.getProfileRede());
			listDetails.setListData(ficha.getDetailsRede());
			labelError.setText("");
		} else {
			labelError.setText("Rede impossivel, erros detectados:");
			displayErrorMessage(ficha.getErrorMessages());
		}

	}

	private void displayErrorMessage(ArrayList<String> inputErrorMessage) {
		String[] errorMessage = new String[inputErrorMessage.size()];
		for (int i = 0; i< errorMessage.length ; i++) {
			errorMessage[i] = inputErrorMessage.get(i);
		}
		listDetails.setListData(new String[1]);
		listClassificacao.setForeground(Color.red);
		listClassificacao.setListData(errorMessage);

	}
}
