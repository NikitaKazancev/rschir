FROM golang:latest

WORKDIR /app

COPY . .

RUN go mod download

CMD go run ./gateway/main.go --port=${APP_PORT}