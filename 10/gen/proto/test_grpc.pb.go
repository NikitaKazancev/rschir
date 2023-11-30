// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v4.25.1
// source: test.proto

package proto

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
	emptypb "google.golang.org/protobuf/types/known/emptypb"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// BooksAPIClient is the client API for BooksAPI service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type BooksAPIClient interface {
	FindAll(ctx context.Context, in *emptypb.Empty, opts ...grpc.CallOption) (*Books, error)
	FindById(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*Book, error)
	Save(ctx context.Context, in *Book, opts ...grpc.CallOption) (*Book, error)
	Change(ctx context.Context, in *Book, opts ...grpc.CallOption) (*Book, error)
	Delete(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*emptypb.Empty, error)
}

type booksAPIClient struct {
	cc grpc.ClientConnInterface
}

func NewBooksAPIClient(cc grpc.ClientConnInterface) BooksAPIClient {
	return &booksAPIClient{cc}
}

func (c *booksAPIClient) FindAll(ctx context.Context, in *emptypb.Empty, opts ...grpc.CallOption) (*Books, error) {
	out := new(Books)
	err := c.cc.Invoke(ctx, "/main.BooksAPI/FindAll", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *booksAPIClient) FindById(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*Book, error) {
	out := new(Book)
	err := c.cc.Invoke(ctx, "/main.BooksAPI/FindById", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *booksAPIClient) Save(ctx context.Context, in *Book, opts ...grpc.CallOption) (*Book, error) {
	out := new(Book)
	err := c.cc.Invoke(ctx, "/main.BooksAPI/Save", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *booksAPIClient) Change(ctx context.Context, in *Book, opts ...grpc.CallOption) (*Book, error) {
	out := new(Book)
	err := c.cc.Invoke(ctx, "/main.BooksAPI/Change", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *booksAPIClient) Delete(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*emptypb.Empty, error) {
	out := new(emptypb.Empty)
	err := c.cc.Invoke(ctx, "/main.BooksAPI/Delete", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// BooksAPIServer is the server API for BooksAPI service.
// All implementations must embed UnimplementedBooksAPIServer
// for forward compatibility
type BooksAPIServer interface {
	FindAll(context.Context, *emptypb.Empty) (*Books, error)
	FindById(context.Context, *ByIdRequest) (*Book, error)
	Save(context.Context, *Book) (*Book, error)
	Change(context.Context, *Book) (*Book, error)
	Delete(context.Context, *ByIdRequest) (*emptypb.Empty, error)
	mustEmbedUnimplementedBooksAPIServer()
}

// UnimplementedBooksAPIServer must be embedded to have forward compatible implementations.
type UnimplementedBooksAPIServer struct {
}

func (UnimplementedBooksAPIServer) FindAll(context.Context, *emptypb.Empty) (*Books, error) {
	return nil, status.Errorf(codes.Unimplemented, "method FindAll not implemented")
}
func (UnimplementedBooksAPIServer) FindById(context.Context, *ByIdRequest) (*Book, error) {
	return nil, status.Errorf(codes.Unimplemented, "method FindById not implemented")
}
func (UnimplementedBooksAPIServer) Save(context.Context, *Book) (*Book, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Save not implemented")
}
func (UnimplementedBooksAPIServer) Change(context.Context, *Book) (*Book, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Change not implemented")
}
func (UnimplementedBooksAPIServer) Delete(context.Context, *ByIdRequest) (*emptypb.Empty, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Delete not implemented")
}
func (UnimplementedBooksAPIServer) mustEmbedUnimplementedBooksAPIServer() {}

// UnsafeBooksAPIServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to BooksAPIServer will
// result in compilation errors.
type UnsafeBooksAPIServer interface {
	mustEmbedUnimplementedBooksAPIServer()
}

func RegisterBooksAPIServer(s grpc.ServiceRegistrar, srv BooksAPIServer) {
	s.RegisterService(&BooksAPI_ServiceDesc, srv)
}

func _BooksAPI_FindAll_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(emptypb.Empty)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(BooksAPIServer).FindAll(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.BooksAPI/FindAll",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(BooksAPIServer).FindAll(ctx, req.(*emptypb.Empty))
	}
	return interceptor(ctx, in, info, handler)
}

func _BooksAPI_FindById_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ByIdRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(BooksAPIServer).FindById(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.BooksAPI/FindById",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(BooksAPIServer).FindById(ctx, req.(*ByIdRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _BooksAPI_Save_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Book)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(BooksAPIServer).Save(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.BooksAPI/Save",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(BooksAPIServer).Save(ctx, req.(*Book))
	}
	return interceptor(ctx, in, info, handler)
}

func _BooksAPI_Change_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Book)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(BooksAPIServer).Change(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.BooksAPI/Change",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(BooksAPIServer).Change(ctx, req.(*Book))
	}
	return interceptor(ctx, in, info, handler)
}

func _BooksAPI_Delete_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ByIdRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(BooksAPIServer).Delete(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.BooksAPI/Delete",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(BooksAPIServer).Delete(ctx, req.(*ByIdRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// BooksAPI_ServiceDesc is the grpc.ServiceDesc for BooksAPI service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var BooksAPI_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "main.BooksAPI",
	HandlerType: (*BooksAPIServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "FindAll",
			Handler:    _BooksAPI_FindAll_Handler,
		},
		{
			MethodName: "FindById",
			Handler:    _BooksAPI_FindById_Handler,
		},
		{
			MethodName: "Save",
			Handler:    _BooksAPI_Save_Handler,
		},
		{
			MethodName: "Change",
			Handler:    _BooksAPI_Change_Handler,
		},
		{
			MethodName: "Delete",
			Handler:    _BooksAPI_Delete_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "test.proto",
}

// TasksAPIClient is the client API for TasksAPI service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type TasksAPIClient interface {
	FindAll(ctx context.Context, in *emptypb.Empty, opts ...grpc.CallOption) (*Tasks, error)
	FindById(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*Task, error)
	Save(ctx context.Context, in *Task, opts ...grpc.CallOption) (*Task, error)
	Change(ctx context.Context, in *Task, opts ...grpc.CallOption) (*Task, error)
	Delete(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*emptypb.Empty, error)
}

type tasksAPIClient struct {
	cc grpc.ClientConnInterface
}

func NewTasksAPIClient(cc grpc.ClientConnInterface) TasksAPIClient {
	return &tasksAPIClient{cc}
}

func (c *tasksAPIClient) FindAll(ctx context.Context, in *emptypb.Empty, opts ...grpc.CallOption) (*Tasks, error) {
	out := new(Tasks)
	err := c.cc.Invoke(ctx, "/main.TasksAPI/FindAll", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *tasksAPIClient) FindById(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*Task, error) {
	out := new(Task)
	err := c.cc.Invoke(ctx, "/main.TasksAPI/FindById", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *tasksAPIClient) Save(ctx context.Context, in *Task, opts ...grpc.CallOption) (*Task, error) {
	out := new(Task)
	err := c.cc.Invoke(ctx, "/main.TasksAPI/Save", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *tasksAPIClient) Change(ctx context.Context, in *Task, opts ...grpc.CallOption) (*Task, error) {
	out := new(Task)
	err := c.cc.Invoke(ctx, "/main.TasksAPI/Change", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *tasksAPIClient) Delete(ctx context.Context, in *ByIdRequest, opts ...grpc.CallOption) (*emptypb.Empty, error) {
	out := new(emptypb.Empty)
	err := c.cc.Invoke(ctx, "/main.TasksAPI/Delete", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// TasksAPIServer is the server API for TasksAPI service.
// All implementations must embed UnimplementedTasksAPIServer
// for forward compatibility
type TasksAPIServer interface {
	FindAll(context.Context, *emptypb.Empty) (*Tasks, error)
	FindById(context.Context, *ByIdRequest) (*Task, error)
	Save(context.Context, *Task) (*Task, error)
	Change(context.Context, *Task) (*Task, error)
	Delete(context.Context, *ByIdRequest) (*emptypb.Empty, error)
	mustEmbedUnimplementedTasksAPIServer()
}

// UnimplementedTasksAPIServer must be embedded to have forward compatible implementations.
type UnimplementedTasksAPIServer struct {
}

func (UnimplementedTasksAPIServer) FindAll(context.Context, *emptypb.Empty) (*Tasks, error) {
	return nil, status.Errorf(codes.Unimplemented, "method FindAll not implemented")
}
func (UnimplementedTasksAPIServer) FindById(context.Context, *ByIdRequest) (*Task, error) {
	return nil, status.Errorf(codes.Unimplemented, "method FindById not implemented")
}
func (UnimplementedTasksAPIServer) Save(context.Context, *Task) (*Task, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Save not implemented")
}
func (UnimplementedTasksAPIServer) Change(context.Context, *Task) (*Task, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Change not implemented")
}
func (UnimplementedTasksAPIServer) Delete(context.Context, *ByIdRequest) (*emptypb.Empty, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Delete not implemented")
}
func (UnimplementedTasksAPIServer) mustEmbedUnimplementedTasksAPIServer() {}

// UnsafeTasksAPIServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to TasksAPIServer will
// result in compilation errors.
type UnsafeTasksAPIServer interface {
	mustEmbedUnimplementedTasksAPIServer()
}

func RegisterTasksAPIServer(s grpc.ServiceRegistrar, srv TasksAPIServer) {
	s.RegisterService(&TasksAPI_ServiceDesc, srv)
}

func _TasksAPI_FindAll_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(emptypb.Empty)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TasksAPIServer).FindAll(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.TasksAPI/FindAll",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TasksAPIServer).FindAll(ctx, req.(*emptypb.Empty))
	}
	return interceptor(ctx, in, info, handler)
}

func _TasksAPI_FindById_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ByIdRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TasksAPIServer).FindById(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.TasksAPI/FindById",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TasksAPIServer).FindById(ctx, req.(*ByIdRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _TasksAPI_Save_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Task)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TasksAPIServer).Save(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.TasksAPI/Save",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TasksAPIServer).Save(ctx, req.(*Task))
	}
	return interceptor(ctx, in, info, handler)
}

func _TasksAPI_Change_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Task)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TasksAPIServer).Change(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.TasksAPI/Change",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TasksAPIServer).Change(ctx, req.(*Task))
	}
	return interceptor(ctx, in, info, handler)
}

func _TasksAPI_Delete_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(ByIdRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(TasksAPIServer).Delete(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/main.TasksAPI/Delete",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(TasksAPIServer).Delete(ctx, req.(*ByIdRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// TasksAPI_ServiceDesc is the grpc.ServiceDesc for TasksAPI service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var TasksAPI_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "main.TasksAPI",
	HandlerType: (*TasksAPIServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "FindAll",
			Handler:    _TasksAPI_FindAll_Handler,
		},
		{
			MethodName: "FindById",
			Handler:    _TasksAPI_FindById_Handler,
		},
		{
			MethodName: "Save",
			Handler:    _TasksAPI_Save_Handler,
		},
		{
			MethodName: "Change",
			Handler:    _TasksAPI_Change_Handler,
		},
		{
			MethodName: "Delete",
			Handler:    _TasksAPI_Delete_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "test.proto",
}