package booksAPI

import (
	"io"
	"log"
	"os"
)

var logFile, _ = os.OpenFile("./services/booksAPI/logs.log", os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
var Logger = log.New(io.MultiWriter(os.Stdout, logFile), "", log.LstdFlags)
