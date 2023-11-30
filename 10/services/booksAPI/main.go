package main

import (
	pb "10/gen/proto"
	"10/services/booksAPI/handlers"
	"context"
	"fmt"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"google.golang.org/grpc"
	"net"
	"time"
)

func connectToDatabase() {

	clientOptions := options.Client().ApplyURI("mongodb://books_mongo:27017")
	client, err := mongo.NewClient(clientOptions)
	if err != nil {
		booksAPI.Logger.Fatal(err)
	}

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	err = client.Connect(ctx)
	if err != nil {
		booksAPI.Logger.Fatal(err)
	}

	fmt.Println("Connected to DB")

	booksAPI.Collection = client.Database("library").Collection("books")

}

func main() {

	connectToDatabase()

	listener, err := net.Listen("tcp", "books:8081")
	if err != nil {
		booksAPI.Logger.Fatal(err)
	}

	grpcServer := grpc.NewServer()
	pb.RegisterBooksAPIServer(grpcServer, &booksAPI.BooksAPI{})
	booksAPI.Logger.Fatal(grpcServer.Serve(listener))

}
