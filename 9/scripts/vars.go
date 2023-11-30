package scripts

import (
	"go.mongodb.org/mongo-driver/bson/primitive"
	"io"
	"log"
	"os"
)

var logFile, _ = os.OpenFile("server.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
var Logger = log.New(io.MultiWriter(os.Stdout, logFile), "", log.LstdFlags)

type File struct {
	ID          primitive.ObjectID `json:"id,omitempty" bson:"_id,omitempty"`
	Name        string             `json:"name,omitempty" bson:"name,omitempty"`
	ContentType string             `json:"contentType,omitempty" bson:"contentType,omitempty"`
	Description string             `json:"description,omitempty" bson:"description,omitempty"`
	Size        int64              `json:"size,omitempty" bson:"size,omitempty"`
}
