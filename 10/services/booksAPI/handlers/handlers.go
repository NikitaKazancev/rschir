package booksAPI

import (
	pb "10/gen/proto"
	"context"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"google.golang.org/protobuf/types/known/emptypb"
)

type BooksAPI struct {
	pb.UnimplementedBooksAPIServer
}

type Book struct {
	ID     string `json:"id" bson:"_id,omitempty"`
	Title  string `json:"title" bson:"title"`
	Author string `json:"author" bson:"author"`
}

var Collection *mongo.Collection

func bookId(id string) primitive.ObjectID {
	objID, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		Logger.Printf("Invalid book ID format: %v", err)
		return primitive.ObjectID{}
	}

	return objID
}

func pbBookFromDbBook(dbBook Book) *pb.Book {
	return &pb.Book{
		Id:     dbBook.ID,
		Title:  dbBook.Title,
		Author: dbBook.Author,
	}
}

func dbBookFromBook(pbBook *pb.Book) Book {
	return Book{
		ID:     pbBook.Id,
		Title:  pbBook.Title,
		Author: pbBook.Author,
	}
}

func (s *BooksAPI) FindAll(ctx context.Context, req *emptypb.Empty) (*pb.Books, error) {

	cur, err := Collection.Find(context.Background(), bson.D{})
	if err != nil {
		Logger.Fatal(err)
	}

	var books pb.Books
	defer cur.Close(ctx)
	for cur.Next(ctx) {
		var book Book
		err := cur.Decode(&book)
		if err != nil {
			Logger.Fatal(err)
		}

		books.Books = append(books.Books, pbBookFromDbBook(book))
	}

	return &books, nil

}

func (s *BooksAPI) FindById(ctx context.Context, req *pb.ByIdRequest) (*pb.Book, error) {

	var book Book
	err := Collection.FindOne(ctx, bson.M{"_id": bookId(req.Id)}).Decode(&book)
	if err != nil {
		Logger.Println(err)
	}

	return pbBookFromDbBook(book), err

}

func (s *BooksAPI) Save(ctx context.Context, req *pb.Book) (*pb.Book, error) {

	book := dbBookFromBook(req)

	result, err := Collection.InsertOne(ctx, book)
	if err != nil {
		Logger.Fatal(err)
	}

	insertedID, ok := result.InsertedID.(primitive.ObjectID)
	if !ok {
		Logger.Fatal("Failed to convert InsertedID to ObjectID")
	}

	book.ID = insertedID.Hex()

	return pbBookFromDbBook(book), err

}

func (s *BooksAPI) Change(ctx context.Context, req *pb.Book) (*pb.Book, error) {

	book := dbBookFromBook(req)
	book.ID = ""

	_, err := Collection.ReplaceOne(ctx, bson.M{"_id": bookId(req.Id)}, book)
	if err != nil {
		Logger.Fatal(err)
	}

	return pbBookFromDbBook(book), err

}

func (s *BooksAPI) Delete(ctx context.Context, req *pb.ByIdRequest) (*emptypb.Empty, error) {

	_, err := Collection.DeleteOne(context.Background(), bson.M{"_id": bookId(req.Id)})
	if err != nil {
		Logger.Fatal(err)
	}

	return nil, err

}
