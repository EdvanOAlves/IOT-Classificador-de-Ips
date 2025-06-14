# IOT-Classificador-de-Ips

Atividade desafio desenvolvida nas aulas de Arquitetura de Redes em IOT do curso Desenvolvimento de Sistemas do Senai utilizando as boas práticas e programação orientada a objetos.

Enunciado:

_**Como desenvolvedor, crie uma solução em Java para que ao digitar o IP e máscara (CIDR) ex: "192.168.0.0/24" retorne:**_

- Classe do ip
- Máscara em padrão decimal
- Máscara em padrão binário
- Quantos ips estão disponíveis (para hosts)

Desafio extra: Mostrar as informações em caso de sub-redes acima de CIDR 24:

- Ips de Rede
- Primeiro Ip de host disponível
- Ultimo Ip de host disponível
- Ip de Broadcast

### Funcionamento
![](./previews/PreviewFuncionamento.png)

Dessa forma ele recebe um ip Cidr e retorna detalhes da classificação no primeiro campo e uma lista das informações sobre as sub-redes no segundo campo

![](./previews/PreviewErros.png)

O software inclui um recurso de detecção de erros que analisa o input do usuário. Caso sejam identificadas falhas que impeçam o funcionamento adequado do programa, o sistema retornará alertas de erro informando os problemas detectados.

## Tecnologias utilizadas
* Java

## Autor
[Edvan Alves](<https://br.linkedin.com/in/edvan-alves>)