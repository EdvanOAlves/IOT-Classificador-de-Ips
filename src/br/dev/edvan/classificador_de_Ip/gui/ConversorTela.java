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

import br.dev.edvan.classificador_de_Ip.model.FichaDeRede;
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
	private JList listDadosSubRede;
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
		labelError.setBounds(20, 115, 385, 30);

		// Text field para input
		textIp = new JTextField();
		textIp.setBounds(20, 50, 200, 30);

		// Botão
		buttonClassificar = new JButton("Classificar");
		buttonClassificar.setBounds(20, 85, 100, 30);

		// Obtendo referencia do Container, o painel de conteudo da janela
		Container container = tela.getContentPane();

		// JList e ScrollPane que vai receber o output para as informações básicas
		listClassificacao = new JList();
		scrollClassificacao = new JScrollPane(listClassificacao);
		scrollClassificacao.setBounds(20, 150, 395, 115);

		// JList e ScrollPane Para dados de sub-Redes acima de 24 CIDR \o/

		listDadosSubRede = new JList();
		scrollDadosSubRede = new JScrollPane(listDadosSubRede);
		scrollDadosSubRede.setBounds(20, 280, 395, 350);

		// Adicionando elementos na janela
		container.add(labelIp);

		container.add(textIp);

		container.add(labelError);

		container.add(buttonClassificar);

		container.add(scrollClassificacao);

		container.add(scrollDadosSubRede);

		// Adicionando ouvintes de acao ao botao
		buttonClassificar.addActionListener(new ActionListener() { // Funcionamento do bot�o Calcular

			@Override
			public void actionPerformed(ActionEvent e) {
				// Passando os valores do campo e criando o objeto
				String ipCidr = textIp.getText();
				
				
				Rede rede = new Rede(ipCidr);
				FichaDeRede ficha = new FichaDeRede(rede);

				// Montagem do vetor para exibicao na tela
				String[] classificacaoVisual = ficha.getClassificacaoVisual();
				String[] listFichas = null;
				String[][] fichasSubRede = null;
				

				if (rede.getMask() > 24) { // Se for nossa premiada sub-rede lá
					fichasSubRede = new String[rede.getQuantSubRede() + 1][6];
					// Criando uma lista de todas as redes
					// o primeiro parâmetro vai ditar quantas redes, logo quantas fichas
					// o segundo é só pra formatar a ficha, 1 titulo, 3 campos de valores, uma linha
					// vazia

					for (int i = 0; i < rede.getQuantSubRede(); i++) {
						String[] ipSplit = rede.getIpSplit();
						String ipMasked = String.format("%s.%s.%s.", ipSplit[0], ipSplit[1], ipSplit[2]);

						int[] octetosDeRede = rede.getOctetosDeRede();
						int[] octetosDeBroadcast = rede.getOctetosDeBroadcast();
						int[] rangeStarts = rede.getRangeStarts();
						int[] rangeEnds = rede.getRangeEnds();
						
						classificacaoVisual[5] = "Número de Sub-Redes: " + rede.getQuantSubRede();

						fichasSubRede[i][0] = "SUB-REDE: " + (i + 1);
						fichasSubRede[i][1] = "Endereço de rede: " + ipMasked + "" + octetosDeRede[i];
						fichasSubRede[i][2] = "Primeiro ip válido: " + ipMasked + rangeStarts[i];
						fichasSubRede[i][3] = "Último ip válido: " + ipMasked + rangeEnds[i];
						fichasSubRede[i][4] = "Endereço de broadcast: " + ipMasked + octetosDeBroadcast[i];
						fichasSubRede[i][5] = " ";

					}
					// Isso daqui é uma gambiarra feia, reorganizar depois, colocar num método, enfim.
					// 
					listFichas = new String[fichasSubRede.length * fichasSubRede[0].length];
					int k = 0;
					for (int i = 0; i < fichasSubRede.length; i++) {
						for (int j = 0; j < 6; j++) {
							listFichas[k] = fichasSubRede[i][j];
							k++;

						}
					}

					if (rede.getMask() == 32) {
						listFichas = new String[6];

						listFichas[0] = "SUB-REDE";
						listFichas[1] = "Endereço de rede: " + rede.getIp();
						listFichas[2] = "Endereço de broadcast: " + rede.getIp();
						listFichas[3] = "Primeiro ip válido: N/A";
						listFichas[4] = "Último ip válido: N/A";
						listFichas[5] = " ";

					}

				}
				else { //Pra montar a nossa ficha em CIDR abaixo de 25
					int[] octetosDeRede = rede.getOctetosDeRede();
					int[] octetosDeBroadcast = rede.getOctetosDeBroadcast();
					int[] rangeStarts = rede.getRangeStarts();
					int[] rangeEnds = rede.getRangeEnds();
					
					String[] ipSplit = rede.getIpSplit();
					String ipMasked = String.format("%s.%s.%s.", ipSplit[0], ipSplit[1], ipSplit[2]);
					//TODO: Essas variáveis podem ficar fora do else, não esperava que ia precisar delas sem o CIDR 25
					
					listFichas = new String[6];
					listFichas[0] = "REDE";
					listFichas[1] = "Endereço de rede: " + ipMasked +octetosDeRede[0];
					listFichas[2] = "Endereço de broadcast: " + ipMasked+octetosDeBroadcast[0];
					listFichas[3] = "Primeiro ip válido: "+ ipMasked+octetosDeRede[0]+1;
					listFichas[4] = "Último ip válido: "+ ipMasked+(octetosDeBroadcast[0]-1);
					listFichas[5] = " ";
					
				}

				if (rede.errorMessage.equals("none")) { // Verificacao, se nao houve nenhum erro
					listClassificacao.setListData(classificacaoVisual);
					if (rede.getMask() > 24) {
						listDadosSubRede.setListData(listFichas);
					}
					else {
						listDadosSubRede.setListData(listFichas);
						
					}

					labelError.setText("");

				} else {
					labelError.setText(rede.errorMessage);
					listClassificacao.setListData(new String[1]);
					listDadosSubRede.setListData(new String[1]);
				}

			}
		});

		// tornando a tela visivel
		tela.setVisible(true);
	}

}
