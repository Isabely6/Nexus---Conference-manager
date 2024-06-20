<?php
include 'db_connect.php'; // Database connection

$email = $_POST['email'];
$password = $_POST['password'];

$sql = "SELECT * FROM user WHERE email='$email'";
$result = mysqli_query($conn, $sql);

if(mysqli_num_rows($result) > 0){

    while($row = $result->fetch_assoc()) {
        $hashed_password = $row["pass"];
        if (password_verify($password, $hashed_password)){
            $idUser = $row["id"];
            $admin = $row["admin"];
            echo json_encode(["success" => true, "message" => "Login successful", "idUser" => $idUser, "admin" => $admin]);
        }
    }
} else {
    echo json_encode(["success" => false, "message" => "Invalid credentials"]);
}
?>