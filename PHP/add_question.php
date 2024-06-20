<?php
// Database connection
include 'db_connect.php';

$idArticle = $_POST['idArticle'];
$question = $_POST['question'];

// Check if POST variables are set and not empty
if (isset($_POST['idArticle']) && isset($_POST['question']) && !empty($_POST['idArticle']) && !empty($_POST['question'])) {
    $idArticle = $_POST['idArticle'];
    $question = $_POST['question'];
    $idUser = $_POST['idUser'];

    // Insert new question into the database
    $insert_sql = "INSERT INTO question (idArticle, idUser, question, active) VALUES (?, ?, ?, 0)"; // Initially setting active to 0 for admin approval
    $insert_stmt = $conn->prepare($insert_sql);
    $insert_stmt->bind_param("iss", $idArticle, $idUser, $question);

    if ($insert_stmt->execute()) {
        echo json_encode(["status" => "success", "message" => "Question added successfully, awaiting approval."]);
    } else {
        echo json_encode(["status" => "error", "message" => "Failed to add question: " . $insert_stmt->error]);
    }

    $insert_stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "Invalid input. 'idArticle' and 'question' are required."]);
}

$conn->close();
?>
