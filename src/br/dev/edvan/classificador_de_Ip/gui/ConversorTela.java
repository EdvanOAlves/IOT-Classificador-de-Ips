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
	// DECLARANDO OS ELEMENTOS
	//
	
	// Comunicacao com o usario e Input
	private JLabel labelIp;
	private JLabel labelError;
	private JTextField textIp;

	// Botao de Start
	private JButton buttonClassificar;

	// Exibicao e navegacao dos resultados
	private JList listClassificacao;
	private JScrollPane scrollClassificacao;
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
		tela.setSize(450, 420); // tamanho da janela
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setResizable(false);

		// Inserindo valores nos text labels (Para pedir o input e retornar os erros)
		labelIp = new JLabel();
		labelIp.setText("Insira Ip:");
		labelIp.setBounds(20, 20, 150, 30);
		
		labelError = new JLabel();
		labelError.setBounds(20, 115, 385, 30);
		

		// Text field para input
		textIp = new JTextField();
		textIp.setBounds(20, 50, 200, 30);

		// Botão
		buttonClassificar = new JButton("Classificar");
		buttonClassificar.setBounds(20, 85, 100, 30);

		// Obtendo referencia do Container, o painel de conteudo da janela
		Container container = tela.getContentPane();

		// JList que vai receber a tabuada
		listClassificacao = new JList();

		// Scrollpane que vai receber o JList
		scrollClassificacao = new JScrollPane(listClassificacao);
		scrollClassificacao.setBounds(20, 150, 395, 150);

		// Adicionando elementos na janela
		container.add(labelIp);

		container.add(textIp);
		
		container.add(labelError);

		container.add(buttonClassificar);
		
		container.add(scrollClassificacao);

		// Adicionando ouvintes de acao ao botao
		buttonClassificar.addActionListener(new ActionListener() { // Funcionamento do bot�o Calcular

			@Override
			public void actionPerformed(ActionEvent e) {
				// Passando os valores do campo e criando o objeto
				String ipCidr = textIp.getText();
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
				}

			}
		});

		// tornando a tela visivel
		tela.setVisible(true);
	}

}
