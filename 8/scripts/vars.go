package scripts

import (
	"github.com/gorilla/securecookie"
	"io"
	"log"
	"os"
)

var Cookie = securecookie.New(
	securecookie.GenerateRandomKey(32),
	securecookie.GenerateRandomKey(32),
)

var logFile, _ = os.OpenFile("server.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
var Logger = log.New(io.MultiWriter(os.Stdout, logFile), "", log.LstdFlags)

type Data struct {
	Data string `json:"data"`
}
