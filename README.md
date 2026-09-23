# ☕ Grão & Cia

Sistema de pedidos para retirada em loja, cardápio e controle de estoque para a cafeteria fictícia **Grão & Cia**.

## 🚀 Como Executar o Ambiente Local (Infraestrutura)

### Pré-requisitos
- [Docker](https://www.docker.com/) e Docker Compose instalados.

### Passo a Passo
1. Copie o arquivo de variáveis de ambiente (se necessário):
   ```bash
   cp .env

2. Suba os containers do PostgreSQL e Redis:
   ```bash
   docker compose up -d

3. Verifique se os containers estão saudáveis:
    ```bash
    docker compose ps