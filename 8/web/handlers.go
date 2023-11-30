package web

import (
	"8/scripts"
	"encoding/json"
	"net/http"
)

func GetData(writer http.ResponseWriter, request *http.Request, cookieName string) {
	requestCookie, err := request.Cookie(cookieName)
	if err != nil {
		http.Error(writer, err.Error(), http.StatusBadRequest)
		return
	}

	var value scripts.Data
	err = scripts.Cookie.Decode(cookieName, requestCookie.Value, &value)
	if err != nil {
		http.Error(writer, err.Error(), http.StatusNotFound)
		return
	}

	err = json.NewEncoder(writer).Encode(scripts.Data{
		Data: value.Data,
	})
	if err != nil {
		http.Error(writer, err.Error(), http.StatusInternalServerError)
		return
	}

	scripts.Logger.Println("Retrieved data:", value)
}

func SetData(writer http.ResponseWriter, request *http.Request, cookieName string) {
	var data scripts.Data
	err := json.NewDecoder(request.Body).Decode(&data)
	if err != nil {
		http.Error(writer, err.Error(), http.StatusBadRequest)
		return
	}

	encoded, err := scripts.Cookie.Encode(cookieName, data)
	if err != nil {
		http.Error(writer, err.Error(), http.StatusInternalServerError)
		return
	}

	http.SetCookie(writer, &http.Cookie{
		Name:  cookieName,
		Value: encoded,
		Path:  "/",
	})

	scripts.Logger.Println("Handled data:", data)
}
