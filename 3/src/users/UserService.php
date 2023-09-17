<?php

class UserService {
    public function __construct(private readonly UserRepo $repo) {}
    public function find_all(): DataWithStatus {
        $data = $this->repo->find_all();
        return DataWithStatus::ok($data);
    }
    public function find_by_id($id): DataWithStatus {
        $data = $this->repo->find_by_id($id);
        return DataWithStatus::not_found_is_empty($data);
    }
    public function find_by_id_with_join($id): DataWithStatus {
        $data = $this->repo->find_by_id_with_join($id);
        if (!$data) {
            return DataWithStatus::empty();
        }

        $result = ["id" => $data[0]["id"], "name" => $data[0]["name"], "email" => $data[0]["email"]];
        $posts = [];
        foreach ($data as $user_data) {
            $posts[] = [
                "id" => $user_data["post_id"],
                "title" => $user_data["title"],
                "body" => $user_data["body"]
            ];
        }
        $result["posts"] = $posts;

        return DataWithStatus::ok($result);
    }
    public function find_by_name($name): DataWithStatus {
        $data = $this->repo->find_by_name($name);
        return DataWithStatus::not_found_is_empty($data);
    }
    public function save($name, $email): Status {
        $db_user = $this->find_by_name($name);
        if ($db_user->status != 404) {
            return Status::create(409);
        }

        $this->repo->save($name, $email);
        return Status::create(200);
    }
    public function update_by_name($name, $email): Status {
        $db_user = $this->find_by_name($name);
        if ($db_user->status == 404) {
            return Status::create(404);
        }

        $this->repo->update_by_name($name, $email);
        return Status::create(200);
    }
    public function delete_by_id($id): Status {
        $db_user = $this->find_by_id($id);
        if ($db_user->status == 404) {
            return Status::create(404);
        }

        $this->repo->delete_by_id($id);
        return Status::create(200);
    }
}