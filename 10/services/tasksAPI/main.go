package main

import (
	pb "10/gen/proto"
	tasksAPI "10/services/tasksAPI/handlers"
	"context"
	"fmt"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"google.golang.org/grpc"
	"net"
	"time"
)

func connectToDatabase() {

	clientOptions := options.Client().ApplyURI("mongodb://tasks_mongo:27018")
	client, err := mongo.NewClient(clientOptions)
	if err != nil {
		tasksAPI.Logger.Fatal(err)
	}

	ctx, cancel := context.WithTimeout(context.Background(), 10*time.Second)
	defer cancel()

	err = client.Connect(ctx)
	if err != nil {
		tasksAPI.Logger.Fatal(err)
	}

	fmt.Println("Connected to DB")

	tasksAPI.Collection = client.Database("work").Collection("tasks")

}

func main() {

	connectToDatabase()

	listener, err := net.Listen("tcp", "tasks:8082")
	if err != nil {
		tasksAPI.Logger.Fatal(err)
	}

	grpcServer := grpc.NewServer()
	pb.RegisterTasksAPIServer(grpcServer, &tasksAPI.TasksAPI{})
	tasksAPI.Logger.Fatal(grpcServer.Serve(listener))

}
