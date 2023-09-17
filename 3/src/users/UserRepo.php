<?php

class UserRepo {
    public function __construct(private readonly false|mysqli $connection) {}
    public function find_all(): array {
        $sql = "SELECT * FROM users";
        $result = mysqli_query($this->connection, $sql);

        $list = [];
        while($item = mysqli_fetch_assoc($result)) {
            $list[] = $item;
        }

        return $list;
    }
    public function find_by_id($id): array|false|null {
        $id = mysqli_real_escape_string($this->connection, $id);

        $sql = "SELECT * FROM users where id = '$id'";
        $result = mysqli_query($this->connection, $sql);

        return mysqli_fetch_assoc($result);
    }
    public function find_by_id_with_join($id): array|false|null {
        $id = mysqli_real_escape_string($this->connection, $id);

        $sql = "SELECT users.id, users.name, users.email,
                        posts.id as post_id, posts.title, posts.body
                FROM users
                LEFT JOIN posts ON users.id = posts.user_id
                where users.id = '$id'";
        $result = mysqli_query($this->connection, $sql);

        if (mysqli_num_rows($result) == 0) {
            return null;
        }

        $list = [];
        while($item = mysqli_fetch_assoc($result)) {
            $list[] = $item;
        }

        return $list;
    }
    public function find_by_name($name): array|false|null {
        $name = mysqli_real_escape_string($this->connection, $name);

        $sql = "SELECT * FROM users where name = '$name'";
        $result = mysqli_query($this->connection, $sql);

        return mysqli_fetch_assoc($result);
    }
    public function save($name, $email): void {
        $name = mysqli_real_escape_string($this->connection, $name);
        $email = mysqli_real_escape_string($this->connection, $email);

        $sql = "INSERT INTO users (name, email) VALUES ('$name', '$email')";
        mysqli_query($this->connection, $sql);
    }
    public function update_by_name($name, $email): void {
        $name = mysqli_real_escape_string($this->connection, $name);
        $email = mysqli_real_escape_string($this->connection, $email);

        $sql = "UPDATE users SET email='$email' WHERE name = '$name'";
        mysqli_query($this->connection, $sql);
    }
    public function delete_by_id($id): void {
        $id = mysqli_real_escape_string($this->connection, $id);

        $sql = "DELETE FROM users WHERE id = '$id'";
        mysqli_query($this->connection, $sql);
    }
}