<?php

class DataWithStatus {
    public function __construct(public $data, public int $status) {}
    public static function create($data, int $status): DataWithStatus {
        return new DataWithStatus($data, $status);
    }
    public static function ok($data): DataWithStatus {
        return new DataWithStatus($data, 200);
    }
    public static function not_found_is_empty($data): DataWithStatus {
        $status = 200;
        if (!$data) {
            $status = 404;
        }

        return new DataWithStatus($data, $status);
    }
    public static function empty(): DataWithStatus {
        return new DataWithStatus(null, 404);
    }
}