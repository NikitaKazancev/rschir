package tasksAPI

import (
	pb "10/gen/proto"
	"context"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"google.golang.org/protobuf/types/known/emptypb"
)

type TasksAPI struct {
	pb.UnimplementedTasksAPIServer
}

type Task struct {
	ID      string `json:"id" bson:"_id,omitempty"`
	Title   string `json:"title" bson:"title"`
	Content string `json:"content" bson:"content"`
}

var Collection *mongo.Collection

func taskId(id string) primitive.ObjectID {
	objID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		Logger.Printf("Invalid task ID format: %v", err)
		return primitive.ObjectID{}
	}

	return objID
}

func pbTaskFromDbTask(dbBook Task) *pb.Task {
	return &pb.Task{
		Id:      dbBook.ID,
		Title:   dbBook.Title,
		Content: dbBook.Content,
	}
}

func dbTaskFromPbTask(pbBook *pb.Task) Task {
	return Task{
		ID:      pbBook.Id,
		Title:   pbBook.Title,
		Content: pbBook.Content,
	}
}

func (s *TasksAPI) FindAll(ctx context.Context, req *emptypb.Empty) (*pb.Tasks, error) {

	cur, err := Collection.Find(context.Background(), bson.D{})
	if err != nil {
		Logger.Fatal(err)
	}

	var tasks pb.Tasks
	defer cur.Close(ctx)
	for cur.Next(ctx) {
		var task Task
		err := cur.Decode(&task)
		if err != nil {
			Logger.Fatal(err)
		}

		tasks.Tasks = append(tasks.Tasks, pbTaskFromDbTask(task))
	}

	return &tasks, nil

}

func (s *TasksAPI) FindById(ctx context.Context, req *pb.ByIdRequest) (*pb.Task, error) {

	var task Task
	err := Collection.FindOne(ctx, bson.M{"_id": taskId(req.Id)}).Decode(&task)
	if err != nil {
		Logger.Println(err)
	}

	return pbTaskFromDbTask(task), err

}

func (s *TasksAPI) Save(ctx context.Context, req *pb.Task) (*pb.Task, error) {

	task := dbTaskFromPbTask(req)

	result, err := Collection.InsertOne(ctx, task)
	if err != nil {
		Logger.Fatal(err)
	}

	insertedID, ok := result.InsertedID.(primitive.ObjectID)
	if !ok {
		Logger.Fatal("Failed to convert InsertedID to ObjectID")
	}

	task.ID = insertedID.Hex()

	return pbTaskFromDbTask(task), err

}

func (s *TasksAPI) Change(ctx context.Context, req *pb.Task) (*pb.Task, error) {

	task := dbTaskFromPbTask(req)
	task.ID = ""

	_, err := Collection.ReplaceOne(ctx, bson.M{"_id": taskId(req.Id)}, task)
	if err != nil {
		Logger.Fatal(err)
	}

	return pbTaskFromDbTask(task), err

}

func (s *TasksAPI) Delete(ctx context.Context, req *pb.ByIdRequest) (*emptypb.Empty, error) {

	_, err := Collection.DeleteOne(context.Background(), bson.M{"_id": taskId(req.Id)})
	if err != nil {
		Logger.Fatal(err)
	}

	return nil, err

}
