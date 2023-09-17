<?php

class Status {
    public function __construct(public int $status) {}
    public static function create(int $status) {
        return new Status($status);
    }
}