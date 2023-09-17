<?php

class Database {
    public function __construct(
        private readonly string $host,
        private readonly string $databaseName,
        private readonly string $user,
        private readonly string $password
    ) {}

    public function get_connection(): false|mysqli {
        return mysqli_connect($this->host, $this->user, $this->password, $this->databaseName);
    }
}