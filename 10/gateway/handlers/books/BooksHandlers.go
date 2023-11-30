package handlersBooks

import (
	pb "10/gen/proto"
	"context"
	"encoding/json"
	"github.com/gorilla/mux"
	"google.golang.org/protobuf/types/known/emptypb"
	"log"
	"net/http"
)

type Book struct {
	ID     string `json:"id" bson:"_id,omitempty"`
	Title  string `json:"title" bson:"title"`
	Author string `json:"author" bson:"author"`
}

func pbBookFromDbBook(dbBook Book) *pb.Book {
	return &pb.Book{
		Id:     dbBook.ID,
		Title:  dbBook.Title,
		Author: dbBook.Author,
	}
}

func FindAll(ctx context.Context, api pb.BooksAPIClient, w http.ResponseWriter) {

	w.Header().Set("Content-Type", "application/json")

	data, err := api.FindAll(ctx, &emptypb.Empty{})
	if err != nil {
		return
	}

	err = json.NewEncoder(w).Encode(data)
	if err != nil {
		log.Fatal(err)
	}

}

func FindById(ctx context.Context, api pb.BooksAPIClient, w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Content-Type", "application/json")

	params := mux.Vars(r)
	id := params["id"]

	data, err := api.FindById(ctx, &pb.ByIdRequest{Id: id})
	if err != nil {
		log.Println(err)
	}

	err = json.NewEncoder(w).Encode(data)
	if err != nil {
		log.Println(err)
	}

}

func Save(ctx context.Context, api pb.BooksAPIClient, w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Content-Type", "application/json")
	var book Book
	_ = json.NewDecoder(r.Body).Decode(&book)

	data, err := api.Save(ctx, pbBookFromDbBook(book))
	if err != nil {
		log.Fatal(err)
	}

	err = json.NewEncoder(w).Encode(data)
	if err != nil {
		log.Fatal(err)
	}

}

func Change(ctx context.Context, api pb.BooksAPIClient, w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Content-Type", "application/json")

	var book Book
	err := json.NewDecoder(r.Body).Decode(&book)
	if err != nil {
		log.Fatal(err)
	}

	log.Println(book)

	data, err := api.Change(ctx, pbBookFromDbBook(book))
	if err != nil {
		log.Fatal(err)
	}

	err = json.NewEncoder(w).Encode(data)
	if err != nil {
		log.Fatal(err)
	}

}

func Delete(ctx context.Context, api pb.BooksAPIClient, w http.ResponseWriter, r *http.Request) {

	w.Header().Set("Content-Type", "application/json")

	params := mux.Vars(r)
	id := params["id"]

	_, err := api.Delete(ctx, &pb.ByIdRequest{Id: id})
	if err != nil {
		log.Println(err)
	}

	err = json.NewEncoder(w).Encode("Book deleted successfully")
	if err != nil {
		log.Println(err)
	}

}
