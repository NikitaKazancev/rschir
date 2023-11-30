<?php

include './connect.php';

if (isset($_POST['update'])) {
	$name = $_POST['name'];
	$surname = $_POST['surname'];

	$sql = "UPDATE users set surname = '$surname' where name = '$name'";
	$result = mysqli_query($connection, $sql);

	if (!$result) {
		die(mysqli_error($connection));
	} 
}

?>

<html lang="en">
	<head>
		<title>DELETE</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.1/css/bootstrap.min.css" integrity="sha512-Z/def5z5u2aR89OuzYcxmDJ0Bnd5V1cKqBEbvLOiUNWdg9PQeXVvXLI90SE4QOHGlfLqUnDNVAYyZi8UwUTmWQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
		<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.1/js/bootstrap.min.js" integrity="sha512-fHY2UiQlipUq0dEabSM4s+phmn+bcxSYzXP4vAXItBvBHU7zAM/mkhCZjtBEIJexhOMzZbgFlPLuErlJF2b+0g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	</head>
	<body>
		<div class="container">
			<button class="btn btn-secondary my-5"><a href="./index.php" class="text-light">USERS' LIST</a></button>
			<form method="post">
				<div class="mb-3">
					<label for="form-name" class="form-label">Name</label>
					<input type="text" class="form-control" id="form-name" name="name" placeholder="Enter name">
				</div>
				<div class="mb-3">
					<label for="form-surname" class="form-label">Surname</label>
					<input type="text" class="form-control" id="form-surname" name="surname" placeholder="Enter surname">
				</div>
				<button type="submit" class="btn btn-primary" name="update">Update</button>
			</form>
		</div>
	</body>
</html>
