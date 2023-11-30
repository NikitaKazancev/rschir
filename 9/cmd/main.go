package main

import (
	"9/scripts"
	"9/web"
	"context"
	"flag"
	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"net/http"
)

func main() {

	port, dbName := commandArgs()

	dbClient := dbClient()
	gridFSBucket := gridFSBucket(dbClient, dbName)

	router := mux.NewRouter()

	registerHandlers(router, dbClient, gridFSBucket, dbName)

	scripts.Logger.Println("Server started on port " + *port)
	scripts.Logger.Fatal(http.ListenAndServe(":"+*port, router))

}

func commandArgs() (*string, *string) {

	port := flag.String("port", "8080", "port to run the server on")
	dbName := flag.String("db-name", "db", "")
	flag.Parse()

	return port, dbName

}

func dbClient() *mongo.Client {

	ctx := context.Background()
	clientOptions := options.Client().ApplyURI("mongodb://mongo:27017")
	dbClient, err := mongo.Connect(ctx, clientOptions)
	if err != nil {
		log.Fatal(err)
	}

	return dbClient

}

func gridFSBucket(dbClient *mongo.Client, name *string) *gridfs.Bucket {

	gridFSBucket, err := gridfs.NewBucket(
		dbClient.Database(*name),
	)
	if err != nil {
		log.Fatal(err)
	}

	return gridFSBucket

}

func registerHandlers(
	router *mux.Router,
	dbClient *mongo.Client,
	gridFSBucket *gridfs.Bucket,
	dbName *string) {

	router.HandleFunc("/api/files", func(writer http.ResponseWriter, request *http.Request) {
		web.FindAll(writer, dbClient, *dbName)
	}).Methods("GET")
	router.HandleFunc("/api/files/{id}", func(writer http.ResponseWriter, request *http.Request) {
		web.FindById(writer, request, dbClient, gridFSBucket, *dbName)
	}).Methods("GET")
	router.HandleFunc("/api/files/{id}/info", func(writer http.ResponseWriter, request *http.Request) {
		web.FindInfoById(writer, request, dbClient, *dbName)
	}).Methods("GET")

	router.HandleFunc("/api/files", func(writer http.ResponseWriter, request *http.Request) {
		web.Save(writer, request, dbClient, gridFSBucket, *dbName)
	}).Methods("POST")

	router.HandleFunc("/api/files/{id}", func(writer http.ResponseWriter, request *http.Request) {
		web.ChangeById(writer, request, dbClient, gridFSBucket, *dbName)
	}).Methods("PUT")

	router.HandleFunc("/api/files/{id}", func(writer http.ResponseWriter, request *http.Request) {
		web.DeleteById(writer, request, dbClient, gridFSBucket, *dbName)
	}).Methods("DELETE")

}
