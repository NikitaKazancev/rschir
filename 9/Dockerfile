FROM golang:latest

WORKDIR /app

COPY . .

RUN go mod download

CMD go run ./cmd/main.go --port=${PORT} --db-name=${DATABASE_NAME}