# 🚀 Simplified Tweeter

Uma aplicação minimalista inspirada no Twitter, desenvolvida para praticar conceitos de **Java** e **Spring Boot**, além de explorar o uso de **Docker** na criação de ambientes isolados.

Este projeto é ideal para quem quer entender como construir uma **API RESTful** do zero, com boas práticas e organização de código.

---

## 🛠️ Tecnologias e Ferramentas

- **Java** — Linguagem principal.
- **Spring Boot** — Framework para simplificar o desenvolvimento backend.
- **Maven** — Gerenciador de dependências e build.
- **Docker** — Para empacotar e rodar a aplicação facilmente.

---

## 🗂️ Estrutura do Projeto

- `src/` → Código-fonte da aplicação.
- `docker/` → Arquivos de configuração para containerização.
- `sample.http` → Arquivo com exemplos de requisições para testar os endpoints.

---

## ▶️ Como Rodar o Projeto

### ✅ Localmente (sem Docker)

1. Clone o repositório:

   ```bash
   git clone https://github.com/Emanuel-rawa/Simplified-tweeter.git
   cd Simplified-tweeter
   ```

2. Compile o projeto:

   ```bash
   ./mvnw clean install
   ```

3. Execute a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Acesse em:

   ```
   http://localhost:8080
   ```

---

### 🐳 Rodando com Docker (o jeito mais fácil)

1. Construa a imagem:

   ```bash
   docker build -t simplified-tweeter .
   ```

2. Execute o container:

   ```bash
   docker run -p 8080:8080 simplified-tweeter
   ```

Pronto! Agora é só interagir com a API via Postman, Insomnia ou diretamente pelo terminal usando `curl`.

---

## 💡 Funcionalidades

- Criar novos "tweets".
- Listar todos os "tweets".
- Deletar ou atualizar "tweets".

(Simples, direto ao ponto… mas com potencial para evoluir!)

---

## 📄 Licença

Distribuído sob a licença [MIT](LICENSE).  
Sinta-se livre para usar, modificar e compartilhar!

---

## 👤 Autor

**Emanuel Rawa**  
[Simplified Tweeter](https://github.com/Emanuel-rawa/Simplified-tweeter)
