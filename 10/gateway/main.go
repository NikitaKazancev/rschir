package main

import (
	"10/gateway/handlers/books"
	handlersTasks "10/gateway/handlers/tasks"
	pb "10/gen/proto"
	"context"
	"flag"
	"github.com/gorilla/mux"
	"google.golang.org/grpc"
	"io"
	"log"
	"net/http"
	"os"
)

func setBooksHandlers(router *mux.Router, api pb.BooksAPIClient) {

	router.HandleFunc("/books", func(writer http.ResponseWriter, request *http.Request) {
		handlersBooks.FindAll(context.Background(), api, writer)
	}).Methods("GET")
	router.HandleFunc("/books/{id}", func(writer http.ResponseWriter, request *http.Request) {
		handlersBooks.FindById(context.Background(), api, writer, request)
	}).Methods("GET")
	router.HandleFunc("/books", func(writer http.ResponseWriter, request *http.Request) {
		handlersBooks.Save(context.Background(), api, writer, request)
	}).Methods("POST")
	router.HandleFunc("/books", func(writer http.ResponseWriter, request *http.Request) {
		handlersBooks.Change(context.Background(), api, writer, request)
	}).Methods("PUT")
	router.HandleFunc("/books/{id}", func(writer http.ResponseWriter, request *http.Request) {
		handlersBooks.Delete(context.Background(), api, writer, request)
	}).Methods("DELETE")

}

func setTasksHandlers(router *mux.Router, api pb.TasksAPIClient) {

	router.HandleFunc("/tasks", func(writer http.ResponseWriter, request *http.Request) {
		handlersTasks.FindAll(context.Background(), api, writer)
	}).Methods("GET")
	router.HandleFunc("/tasks/{id}", func(writer http.ResponseWriter, request *http.Request) {
		handlersTasks.FindById(context.Background(), api, writer, request)
	}).Methods("GET")
	router.HandleFunc("/tasks", func(writer http.ResponseWriter, request *http.Request) {
		handlersTasks.Save(context.Background(), api, writer, request)
	}).Methods("POST")
	router.HandleFunc("/tasks", func(writer http.ResponseWriter, request *http.Request) {
		handlersTasks.Change(context.Background(), api, writer, request)
	}).Methods("PUT")
	router.HandleFunc("/tasks/{id}", func(writer http.ResponseWriter, request *http.Request) {
		handlersTasks.Delete(context.Background(), api, writer, request)
	}).Methods("DELETE")

}

func APIs() (pb.BooksAPIClient, pb.TasksAPIClient) {
	conn, err := grpc.Dial("books:8081", grpc.WithInsecure())
	if err != nil {
		logger.Println(err)
	}
	booksAPI := pb.NewBooksAPIClient(conn)

	conn, err = grpc.Dial("tasks:8082", grpc.WithInsecure())
	if err != nil {
		logger.Println(err)
	}
	tasksAPI := pb.NewTasksAPIClient(conn)

	return booksAPI, tasksAPI
}

var logger *log.Logger

func main() {

	logFile, _ := os.OpenFile("./gateway/logs.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	logger = log.New(io.MultiWriter(os.Stdout, logFile), "", log.LstdFlags)

	port := flag.String("port", "8080", "port to run the server on")

	router := mux.NewRouter()

	booksAPI, tasksAPI := APIs()
	setBooksHandlers(router, booksAPI)
	setTasksHandlers(router, tasksAPI)

	logger.Println("Server is running on port " + *port)
	logger.Fatal(http.ListenAndServe(":"+*port, router))

}
