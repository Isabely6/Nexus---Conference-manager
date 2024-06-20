<?php

include 'db_connect.php';

if (isset($_POST["id"])) {
    $id = $_POST["id"];

    $sql = "DELETE from question where id = $id";
    $result = $conn->query($sql);

    if ($result) {
        echo json_encode(array("success" => true, "message" => "Comment Deleted"));
    } else {
        echo json_encode(array("success" => false, "message" => "Comment Not Deleted"));
    }
} else {
    echo json_encode(array("success" => false, "message" => "Invalid request"));
}

$conn->close();
?>
