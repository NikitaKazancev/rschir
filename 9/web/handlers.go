package web

import (
	"9/scripts"
	"context"
	"encoding/json"
	"github.com/gorilla/mux"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/gridfs"
	"io/ioutil"
	"log"
	"net/http"
)

func FindAll(
	writer http.ResponseWriter,
	dbClient *mongo.Client,
	dbName string) {

	ctx := context.Background()
	dbCollection := dbClient.Database(dbName).Collection(dbName)
	dbCursor, err := dbCollection.Find(ctx, bson.M{})
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error getting files", http.StatusInternalServerError)
		return
	}
	defer dbCursor.Close(ctx)

	var files []scripts.File
	for dbCursor.Next(ctx) {
		var file scripts.File
		err := dbCursor.Decode(&file)
		if err != nil {
			log.Println(err)
			http.Error(writer, "Error decoding files", http.StatusInternalServerError)
			return
		}
		files = append(files, file)
	}
	err = dbCursor.Err()
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error iterating files", http.StatusInternalServerError)
		return
	}

	writer.Header().Set("Content-Type", "application/json")
	json.NewEncoder(writer).Encode(files)
	scripts.Logger.Println("Files were got")

}

func FindById(
	writer http.ResponseWriter,
	request *http.Request,
	dbClient *mongo.Client,
	gridFSBucket *gridfs.Bucket,
	dbName string) {

	vars := mux.Vars(request)
	id, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(writer, "Invalid ID", http.StatusBadRequest)
		return
	}

	ctx := context.Background()
	collection := dbClient.Database(dbName).Collection(dbName)
	filter := bson.M{"_id": id}
	var file scripts.File
	err = collection.FindOne(ctx, filter).Decode(&file)
	if err != nil {
		log.Println(err)
		http.Error(writer, "File not found", http.StatusNotFound)
		return
	}

	downloadStream, err := gridFSBucket.OpenDownloadStream(id)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error getting file", http.StatusInternalServerError)
		return
	}
	defer downloadStream.Close()

	fileBytes, err := ioutil.ReadAll(downloadStream)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error getting file", http.StatusInternalServerError)
		return
	}

	writer.Header().Set("Content-Type", file.ContentType)
	writer.Write(fileBytes)
	scripts.Logger.Println("File was read")

}

func FindInfoById(
	writer http.ResponseWriter,
	request *http.Request,
	dbClient *mongo.Client,
	dbName string) {

	vars := mux.Vars(request)
	id, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(writer, "Invalid ID", http.StatusBadRequest)
		return
	}

	ctx := context.Background()
	collection := dbClient.Database(dbName).Collection(dbName)
	filter := bson.M{"_id": id}
	var file scripts.File
	err = collection.FindOne(ctx, filter).Decode(&file)
	if err != nil {
		log.Println(err)
		http.Error(writer, "File not found", http.StatusNotFound)
		return
	}

	writer.Header().Set("Content-Type", "application/json")
	fileInfo := map[string]interface{}{
		"id":          file.ID.Hex(),
		"name":        file.Name,
		"description": file.Description,
		"size":        file.Size,
		"contentType": file.ContentType,
	}

	json.NewEncoder(writer).Encode(fileInfo)
	scripts.Logger.Println("File info was read")

}

func Save(
	writer http.ResponseWriter,
	request *http.Request,
	dbClient *mongo.Client,
	gridFSBucket *gridfs.Bucket,
	dbName string) {

	file, handler, err := request.FormFile("file")
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error reading file", http.StatusInternalServerError)
		return
	}
	defer file.Close()

	ctx := context.Background()
	collection := dbClient.Database(dbName).Collection(dbName)
	result, err := collection.InsertOne(ctx, bson.M{
		"name":        request.FormValue("name"),
		"contentType": handler.Header.Get("Content-Type"),
		"description": request.FormValue("description"),
		"size":        handler.Size,
	})
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error inserting file", http.StatusInternalServerError)
		return
	}
	id := result.InsertedID.(primitive.ObjectID)

	fileBytes, err := ioutil.ReadAll(file)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error reading file", http.StatusInternalServerError)
		return
	}

	uploadStream, err := gridFSBucket.OpenUploadStreamWithID(id, id.Hex())
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error uploading file", http.StatusInternalServerError)
		return
	}
	defer uploadStream.Close()

	_, err = uploadStream.Write(fileBytes)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error uploading file", http.StatusInternalServerError)
		return
	}

	writer.WriteHeader(http.StatusCreated)
	scripts.Logger.Println("File was created")

}

func ChangeById(
	writer http.ResponseWriter,
	request *http.Request,
	dbClient *mongo.Client,
	gridFSBucket *gridfs.Bucket,
	dbName string) {

	vars := mux.Vars(request)
	id, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(writer, "Invalid ID", http.StatusBadRequest)
		return
	}
	file, handler, err := request.FormFile("file")
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error reading file 1", http.StatusInternalServerError)
		return
	}
	defer file.Close()

	ctx := context.Background()
	collection := dbClient.Database(dbName).Collection(dbName)
	filter := bson.M{"_id": id}
	update := bson.M{
		"$set": bson.M{
			"name":        request.FormValue("name"),
			"contentType": handler.Header.Get("Content-Type"),
			"description": request.FormValue("description"),
			"size":        handler.Size,
		},
	}
	result, err := collection.UpdateOne(ctx, filter, update)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error updating file", http.StatusInternalServerError)
		return
	}
	if result.ModifiedCount == 0 {
		http.Error(writer, "File not found", http.StatusNotFound)
		return
	}

	fileBytes, err := ioutil.ReadAll(file)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error reading file 2", http.StatusInternalServerError)
		return
	}
	err = gridFSBucket.Delete(id)

	uploadStream, err := gridFSBucket.OpenUploadStreamWithID(id, id.Hex())
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error uploading file 1", http.StatusInternalServerError)
		return
	}
	defer uploadStream.Close()

	_, err = uploadStream.Write(fileBytes)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error uploading file 2", http.StatusInternalServerError)
		return
	}

	writer.WriteHeader(http.StatusNoContent)
	scripts.Logger.Println("File was updated")

}

func DeleteById(
	writer http.ResponseWriter,
	request *http.Request,
	dbClient *mongo.Client,
	gridFSBucket *gridfs.Bucket,
	dbName string) {

	vars := mux.Vars(request)
	id, err := primitive.ObjectIDFromHex(vars["id"])
	if err != nil {
		http.Error(writer, "Invalid ID", http.StatusBadRequest)
		return
	}

	ctx := context.Background()
	collection := dbClient.Database(dbName).Collection(dbName)
	filter := bson.M{"_id": id}
	result, err := collection.DeleteOne(ctx, filter)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error deleting file", http.StatusInternalServerError)
		return
	}
	if result.DeletedCount == 0 {
		http.Error(writer, "File not found", http.StatusNotFound)
		return
	}

	err = gridFSBucket.Delete(id)
	if err != nil {
		log.Println(err)
		http.Error(writer, "Error deleting file", http.StatusInternalServerError)
		return
	}

	writer.WriteHeader(http.StatusNoContent)
	scripts.Logger.Println("File was deleted")

}
