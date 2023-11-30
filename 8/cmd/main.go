package main

import (
	"8/scripts"
	"8/web"
	"flag"
	"net/http"
	"time"
)

func main() {
	port, cookieName := getCmdParams()

	setHandlers(cookieName)

	scripts.Logger.Printf("Сервер запущен на порту " + port)
	scripts.Logger.Fatal(http.ListenAndServe(":"+port, nil))
}

func setHandlers(cookieName string) {
	http.HandleFunc("/api/data/get", func(writer http.ResponseWriter, request *http.Request) {
		web.GetData(writer, request, cookieName)
	})
	http.HandleFunc("/api/data/linear-get", func(writer http.ResponseWriter, request *http.Request) {
		time.Sleep(5 * time.Second)
		web.GetData(writer, request, cookieName)
	})
	http.HandleFunc("/api/data/concurrent-get", func(writer http.ResponseWriter, request *http.Request) {
		go web.GetData(writer, request, cookieName)
	})

	http.HandleFunc("/api/data/set", func(writer http.ResponseWriter, request *http.Request) {
		web.SetData(writer, request, cookieName)
	})
}

func getCmdParams() (string, string) {
	port := flag.String("port", "8080", "port")
	cookieName := flag.String("cookie-name", "data", "")
	flag.Parse()

	return *port, *cookieName
}
