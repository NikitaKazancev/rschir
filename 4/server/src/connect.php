<?php

$connection = new mysqli('db', 'user', 'password', 'db');

if (!$connection) {
	die(mysqli_error($connection));
}