create:
	protoc --proto_path=proto proto/*.proto --go_out=gen/
	protoc --proto_path=proto proto/*.proto --go_grpc_out=gen/

clean:
	rm gen/proto/*.go