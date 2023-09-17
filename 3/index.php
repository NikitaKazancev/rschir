<?php
declare(strict_types=1);
header("Content-Type: application/json");
spl_autoload_register(function ($class) {
    if (str_starts_with($class, "User")) {
        require __DIR__ . "/src/users/$class.php";
    } elseif (str_starts_with($class, "Post")) {
        require __DIR__ . "/src/posts/$class.php";
    } else {
        require __DIR__ . "/src/$class.php";
    }
});

$method = $_SERVER["REQUEST_METHOD"];

$parsed_url = parse_url($_SERVER['REQUEST_URI']);
$query_params = [];
if (isset($parsed_url['query'])) {
    parse_str($parsed_url['query'], $query_params);
}

$path_parts = explode("/", $parsed_url["path"]);
$entity = $path_parts[1];
$id = $path_parts[2] ?? null;

$input_data = json_decode(file_get_contents("php://input"), true);

if ($entity != "users" & $entity != "posts") {
    http_response_code(404);
    exit;
}

$db = new Database('db', 'rschir_3', 'user', 'password');
$connection = $db->get_connection();

if ($entity == "users") {
    $user_controller = new UserController(new UserService(new UserRepo($connection)));

    switch ($method) {
        case 'GET':
            if ($id) {
                $user_controller->find_by_id_with_join($id);
            } elseif (!empty($query_params)) {
                $user_controller->find_by_params($query_params);
            } else {
                $user_controller->find_all();
            }
            break;

        case 'POST':
            $user_controller->save($input_data);
            break;

        case 'PUT':
            $user_controller->update_by_name($input_data);
            break;

        case 'DELETE':
            $user_controller->delete_by_id($id);
            break;
    }
} elseif ($entity == "posts") {
    $post_controller = new PostController(new PostService(new PostRepo($connection)));

    switch ($method) {
        case 'GET':
            if ($id) {
                $post_controller->find_by_id($id);
            } elseif (!empty($query_params)) {
                $post_controller->find_by_params($query_params);
            } else {
                $post_controller->find_all();
            }
            break;

        case 'POST':
            $post_controller->save($input_data);
            break;

        case 'PUT':
            $post_controller->update_by_user_id_and_title($input_data);
            break;

        case 'DELETE':
            $post_controller->delete_by_id($id);
            break;
    }
}