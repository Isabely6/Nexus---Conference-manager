<?php
include 'db_connect.php'; // Database connection

$idArticle = $_GET['idArticle'];

$sql = "SELECT a.title, a.authors, a.description, p.room, p.date, p.hour, t.color, t.name
        FROM article a 
        JOIN program p ON a.id = p.idArticle 
        join track t on p.idTrack = t.id
        WHERE a.id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $idArticle);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $response = $result->fetch_assoc();
    echo json_encode($response);
} else {
    echo json_encode([]);
}

$conn->close();
?>
