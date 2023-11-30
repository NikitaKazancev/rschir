<?php
include './connect.php';
?>

<!DOCTYPE html>
<html lang="en">
<head>
		<title>READ</title>
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.1/css/bootstrap.min.css" integrity="sha512-Z/def5z5u2aR89OuzYcxmDJ0Bnd5V1cKqBEbvLOiUNWdg9PQeXVvXLI90SE4QOHGlfLqUnDNVAYyZi8UwUTmWQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
		<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.1/js/bootstrap.min.js" integrity="sha512-fHY2UiQlipUq0dEabSM4s+phmn+bcxSYzXP4vAXItBvBHU7zAM/mkhCZjtBEIJexhOMzZbgFlPLuErlJF2b+0g==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	</head>
<body>
	<div class="container">
		<div class="my-5">
			<button class="btn btn-success mr-1"><a href="./create.php" class="text-light">CREATE</a></button>
			<button class="btn btn-primary mr-1"><a href="./update.php" class="text-light">UPDATE</a></button>
			<button class="btn btn-danger mr-1"><a href="./delete.php" class="text-light">DELETE</a></button>
		</div>
		<table class="table">
			<thead>
				<tr>
					<th scope="col">№</th>
					<th scope="col">Name</th>
					<th scope="col">Surname</th>
				</tr>
			</thead>
			<tbody>
				<?php
				$sql = "SELECT * from `users`";
				$res = mysqli_query($connection, $sql);
				if (!$res) {
					die(mysqli_error($connection));
				}

				while($row = mysqli_fetch_assoc($res)) {
					$id = $row['id'];
					$name = $row['name'];
					$surname = $row['surname'];
					echo '
					<tr>
						<th scope="row">'.$id.'</th>
						<td>'.$name.'</td>
						<td>'.$surname.'</td>
					</tr>';
				}
				?>
			</tbody>
		</table>
	</div>
</body>
</html>