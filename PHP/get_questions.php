<?php
// Database connection
include 'db_connect.php';

if (isset($_GET['idArticle'])) {
    $idArticle = intval($_GET['idArticle']);

    // Fetch questions for the article
    $questions_sql = "SELECT q.question, u.user FROM question q join user u on q.idUser = u.id WHERE idArticle = ? AND active = 1";
    $questions_stmt = $conn->prepare($questions_sql);
    $questions_stmt->bind_param("i", $idArticle);
    $questions_stmt->execute();
    $questions_result = $questions_stmt->get_result();

    $questions = array();
    while ($row = $questions_result->fetch_assoc()) {
        $questions[] = array(
            "idArticle" => $idArticle,
            "question" => $row["question"],
            "username" => $row["user"]
        );
    }

    echo json_encode($questions);
} else {
    echo json_encode(array("error" => "Invalid or missing idArticle parameter"));
}

$conn->close();
?>

