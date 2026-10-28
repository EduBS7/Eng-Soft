# Exemplos de uso (curl)

Fluxo completo, com a API rodando em `localhost:8080`.

## 1. Cadastrar dois jogadores (US01)

```bash
curl -X POST localhost:8080/poker/cadastro -H "Content-Type: application/json" \
  -d '{"nome":"Ana","email":"ana@email.com","senha":"senha123"}'
# 201 → {"id":1}

curl -X POST localhost:8080/poker/cadastro -H "Content-Type: application/json" \
  -d '{"nome":"Bruno","email":"bruno@email.com","senha":"senha123"}'
# 201 → {"id":2}
```

## 2. Ana cria uma mesa (endpoint de apoio)

```bash
curl -X POST localhost:8080/poker/partidas -H "Content-Type: application/json" \
  -d '{"idJogador":1,"nome":"Mesa da Ana","maxJogadores":4}'
# 201 → {"id":1}
```

## 3. Consultar mesas abertas (US09)

```bash
curl localhost:8080/poker/partidasAbertas
# 200 → [{"id":1,"nome":"Mesa da Ana","jogadoresPresentes":1,"maxJogadores":4}]
```

## 4. Bruno entra na mesa (US10)

```bash
curl -X POST "localhost:8080/poker/1/entrar?idJogador=2"
# 200 → {"entrou":true,"mensagem":"Jogador entrou na partida.","idPartida":1,"idJogador":2,"jogadoresPresentes":2,"maxJogadores":4}
```

## 5. Ana inicia a partida (US12 + US13)

```bash
curl -X POST "localhost:8080/poker/1/iniciar?idJogador=1"
# 200 → {"iniciada":true,"mensagem":"Partida iniciada. Cartas distribuídas.","idPartida":1,"rodada":1,"jogadoresNaMesa":2}
```

## 6. Ver as cartas recebidas (apoio ao US13)

```bash
curl "localhost:8080/poker/1/minhaMao?idJogador=2"
# 200 → {"idPartida":1,"rodada":1,"cartas":[{"valor":"REI","naipe":"COPAS","descricao":"K♥"},{"valor":"DOIS","naipe":"PAUS","descricao":"2♣"}]}
```

## Erros

| Situação | Resposta |
|----------|----------|
| Mesa cheia | `409` `{"status":409,"erro":"Conflict","mensagem":"A mesa está cheia."}` |
| Partida já iniciada | `409` `"A partida já foi iniciada."` |
| Iniciar sem ser o criador | `403` `"Somente o jogador que criou a mesa pode iniciar a partida."` |
| Iniciar com 1 jogador só | `409` `"São necessários pelo menos 2 jogadores para iniciar. Presentes: 1."` |
| Partida ou jogador inexistente | `404` |
| E-mail já cadastrado | `409` |
| Campo inválido no cadastro | `400` com a lista em `detalhes` |
