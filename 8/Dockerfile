FROM golang

WORKDIR /app

COPY . .

RUN go mod download

CMD go run ./cmd/main.go --port=${PORT} --cookie-name=${COOKIE_NAME}