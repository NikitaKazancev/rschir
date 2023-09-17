<?php

class PostService {
    public function __construct(private readonly PostRepo $repo) {}
    public function find_all(): DataWithStatus {
        $data = $this->repo->find_all();
        return DataWithStatus::ok($data);
    }
    public function find_by_id($id): DataWithStatus {
        $data = $this->repo->find_by_id($id);
        return DataWithStatus::not_found_is_empty($data);
    }
    public function find_by_user_id($user_id): DataWithStatus {
        $data = $this->repo->find_by_user_id($user_id);
        return DataWithStatus::ok($data);
    }
    public function save($title, $body, $user_id): Status {
        $user_posts = $this->find_by_user_id($user_id);
        foreach ($user_posts->data as $post) {
            if ($post["title"] == $title) {
                return Status::create(409);
            }
        }

        $this->repo->save($title, $body, $user_id);
        return Status::create(200);
    }
    public function update_by_user_id_and_title($user_id, $title, $body): Status {
        $user_posts = $this->find_by_user_id($user_id);
        foreach ($user_posts->data as $post) {
            if ($post["title"] == $title) {
                $this->repo->update_by_user_id_and_title($user_id, $title, $body);
                return Status::create(200);
            }
        }

        return Status::create(404);
    }
    public function delete_by_id($id): Status {
        $db_post = $this->find_by_id($id);
        if ($db_post->status == 404) {
            return Status::create(404);
        }

        $this->repo->delete_by_id($id);
        return Status::create(200);
    }
}