<?php

class PostController {
    public function __construct(private readonly PostService $service) {}
    private function handle_request(DataWithStatus|Status $result): void {
        http_response_code($result->status);
        echo json_encode($result, JSON_NUMERIC_CHECK);
    }
    public function find_all(): void {
        $this->handle_request($this->service->find_all());
    }
    public function find_by_id($id): void {
        $this->handle_request($this->service->find_by_id($id));
    }
    public function find_by_params($query_params): void {
        if (isset($query_params["userId"])) {
            $this->find_by_user_id($query_params["userId"]);
            return;
        }

        http_response_code(400);
    }
    private function find_by_user_id($user_id): void {
        $this->handle_request($this->service->find_by_user_id($user_id));
    }
    public function save($input_data): void {
        if (!isset($input_data["title"]) | !isset($input_data["body"]) | !isset($input_data["userId"])) {
            http_response_code(400);
            return;
        }
        $title = $input_data["title"];
        $body = $input_data["body"];
        $user_id = $input_data["userId"];

        $this->handle_request($this->service->save($title, $body, $user_id));
    }
    public function update_by_user_id_and_title($input_data): void {
        if (!isset($input_data["userId"]) | !isset($input_data["title"]) | !isset($input_data["body"])) {
            http_response_code(400);
            return;
        }
        $user_id = $input_data["userId"];
        $title = $input_data["title"];
        $body = $input_data["body"];

        $this->handle_request($this->service->update_by_user_id_and_title($user_id, $title, $body));
    }
    public function delete_by_id($id): void {
        if (!$id) {
            http_response_code(400);
            return;
        }

        $this->handle_request($this->service->delete_by_id($id));
    }
}