<?php

class UserController {
    public function __construct(private readonly UserService $service) {}
    private function handle_request(DataWithStatus|Status $result): void {
        http_response_code($result->status);
        echo json_encode($result, JSON_NUMERIC_CHECK);
    }
    public function find_all(): void {
        $this->handle_request($this->service->find_all());
    }
    public function find_by_id($id): void {
        if (!$id) {
            http_response_code(400);
            return;
        }

        $this->handle_request($this->service->find_by_id($id));
    }
    public function find_by_id_with_join($id): void {
        $this->handle_request($this->service->find_by_id_with_join($id));
    }
    public function find_by_params($query_params): void {
        if (isset($query_params["name"])) {
            $this->find_by_name($query_params["name"]);
            return;
        }

        http_response_code(400);
    }
    private function find_by_name($name): void {
        $this->handle_request($this->service->find_by_name($name));
    }
    public function save($input_data): void {
        if (!isset($input_data["name"]) | !isset($input_data["email"])) {
            http_response_code(400);
            return;
        }
        $name = $input_data["name"];
        $email = $input_data["email"];

        $this->handle_request($this->service->save($name, $email));
    }
    public function update_by_name($input_data): void {
        if (!isset($input_data["name"]) | !isset($input_data["email"])) {
            http_response_code(400);
            return;
        }
        $name = $input_data["name"];
        $email = $input_data["email"];

        $this->handle_request($this->service->update_by_name($name, $email));
    }
    public function delete_by_id($id): void {
        if (!$id) {
            http_response_code(400);
            return;
        }

        $this->handle_request($this->service->delete_by_id($id));
    }
}