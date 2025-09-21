# P2P Network Connection PoC

Este projeto é uma Prova de Conceito (PoC) para conexão em rede peer-to-peer (P2P) utilizando Java. O objetivo é demonstrar como múltiplos nós podem se comunicar diretamente, sem a necessidade de um servidor central, simulando uma rede distribuída.

## Estrutura do Projeto

- `src/`
  - `Main.java`: Ponto de entrada da aplicação. Inicializa os nós e gerencia a execução.
  - `Node.java`: Representa um nó da rede P2P, responsável pela comunicação com outros nós.
  - `Service.java` e `IService.java`: Definem e implementam os serviços oferecidos pelos nós na rede.

## Funcionalidades

- Inicialização de múltiplos nós em rede P2P
- Comunicação direta entre nós
- Interface de serviço para operações distribuídas

## Como executar

1. Certifique-se de ter o Java instalado (JDK 8 ou superior).
2. Compile os arquivos Java:
   ```bash
   javac src/*.java
   ```
3. Execute o programa principal:
   ```bash
   java -cp src Main
   ```

## Objetivo

Demonstrar conceitos básicos de redes P2P, como descoberta de nós, troca de mensagens e implementação de serviços distribuídos.

## Autor

- Lucas

---
