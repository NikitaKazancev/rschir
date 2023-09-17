<?php

class PostRepo {
    public function __construct(private readonly mysqli $connection) {}
    public function find_all(): array {
        $sql = "SELECT * FROM posts";
        $result = mysqli_query($this->connection, $sql);

        $list = [];
        while ($item = mysqli_fetch_assoc($result)) {
            $list[] = $item;
        }

        return $list;
    }
    public function find_by_id($id): array|false|null {
        $id = mysqli_real_escape_string($this->connection, $id);

        $sql = "SELECT posts.id, posts.title, posts.body,
                        users.id as user_id, users.name, users.email
                FROM posts
                LEFT JOIN users ON posts.user_id = users.id
                where posts.id = '$id'";
        $result = mysqli_query($this->connection, $sql);

        return mysqli_fetch_assoc($result);
    }
    public function find_by_user_id($user_id): array {
        $user_id = mysqli_real_escape_string($this->connection, $user_id);

        $sql = "SELECT * FROM posts where user_id = '$user_id'";
        $result = mysqli_query($this->connection, $sql);

        $list = [];
        while ($item = mysqli_fetch_assoc($result)) {
            $list[] = $item;
        }

        return $list;
    }
    public function save($title, $body, $user_id): void {
        $title = mysqli_real_escape_string($this->connection, $title);
        $body = mysqli_real_escape_string($this->connection, $body);
        $user_id = mysqli_real_escape_string($this->connection, $user_id);

        $sql = "INSERT INTO posts (title, body, user_id) VALUES ('$title', '$body', '$user_id')";
        mysqli_query($this->connection, $sql);
    }
    public function update_by_user_id_and_title($user_id, $title, $body): void {
        $user_id = mysqli_real_escape_string($this->connection, $user_id);
        $title = mysqli_real_escape_string($this->connection, $title);
        $body = mysqli_real_escape_string($this->connection, $body);

        $sql = "UPDATE posts SET body='$body' WHERE user_id = '$user_id' and title = '$title'";
        mysqli_query($this->connection, $sql);
    }
    public function delete_by_id($id): void {
        $id = mysqli_real_escape_string($this->connection, $id);

        $sql = "DELETE FROM posts WHERE id = '$id'";
        mysqli_query($this->connection, $sql);
    }
}