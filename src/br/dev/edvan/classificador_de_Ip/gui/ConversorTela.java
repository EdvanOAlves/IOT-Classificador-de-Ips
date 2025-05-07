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

	// Comunicacao com o usario e Input
	private JLabel labelIp;
	private JLabel labelError;
	private JTextField textIp;

	// Botoes para executar os metodos
	private JButton buttonClassificar;

	// Exibicao e navegacao dos resultados
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
		tela.setLayout(null);
		tela.setTitle(this.tituloDaTela); // titulo da janela
		tela.setSize(410, 420); // tamanho da janela
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setResizable(false);

		// Criando text labels para a janela
		labelIp = new JLabel();
		labelIp.setText("Insira Ip:");
		labelIp.setBounds(20, 20, 150, 30);
		
		labelError = new JLabel();
		labelError.setBounds(20, 100, 385, 30);
		
		

		// Criando text fields para a janela
		textIp = new JTextField();
		textIp.setBounds(20, 50, 200, 30);

		// Criando botao para a janela
		buttonClassificar = new JButton("Classificar");
		buttonClassificar.setBounds(20, 80, 100, 30);

		// Obtendo referencia do Container, o painel de conteudo da janela
		Container container = tela.getContentPane();

		// Criar o JList que vai receber a tabuada
		listClassificacao = new JList();

		// Criar o Scrollpane que vai receber o JList
		scrollClassificacao = new JScrollPane(listClassificacao);
		scrollClassificacao.setBounds(20, 150, 350, 150);

		// Adicionando elementos na janela
		container.add(labelIp);

		container.add(textIp);
		
		container.add(labelError);

		container.add(buttonClassificar);
		
		container.add(scrollClassificacao);

		// Adicionando escutantes de acao ao botao
		buttonClassificar.addActionListener(new ActionListener() { // Funcionamento do bot�o Calcular

			@Override
			public void actionPerformed(ActionEvent e) {
				// Coletando os valores nos campos
				String ipCidr = textIp.getText();

				// Fornecendo os valores para a tabuada
				Rede rede = new Rede(ipCidr);

				// Montagem do vetor para exibicao na tela
				String[] classificacaoVisual = new String[5];
				
				classificacaoVisual[0] = "ip: " + rede.getIp();
				classificacaoVisual[1] = "Classe: " + rede.getClasse();
				classificacaoVisual[2] = "Mascara (Decimal): " + rede.getDecimalMask();
				classificacaoVisual[3] = "Mascara (Binario): " + rede.getBinaryMask();
				classificacaoVisual[4] = "Quantidade de ips disponiveis: " + rede.getAvaliableIps();

				if (rede.errorMessage.equals("none")){ //Verificacao, se nao houve nenhum erro
					listClassificacao.setListData(classificacaoVisual);
					labelError.setText("");
					
				}
				else {
					labelError.setText(rede.errorMessage);
					listClassificacao.setListData(new String[1]);
					//TODO adicionar setText no labelError
				}

			}
		});

		// tornando a tela visivel
		tela.setVisible(true);
	}

	private void exibirTabuada() {

	}

	private void limparTabuada() {

	}

}
