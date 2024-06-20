<?php

include 'db_connect.php';

if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["password"])) {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];

    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    $sql = "INSERT INTO user (email, user, pass) VALUES ('$email', '$name', '$hashed_password')";
    $result = $conn->query($sql);

    if ($result) {
        echo json_encode(array("success" => true, "message" => "User Successfully Registered"));
    } else {
        echo json_encode(array("success" => false, "message" => "Failed to Register User"));
    }
} else {
    echo json_encode(array("success" => false, "message" => "Invalid request"));
}

$conn->close();
?>
