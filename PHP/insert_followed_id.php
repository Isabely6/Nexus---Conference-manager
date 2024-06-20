<?php
include 'db_connect.php'; 

// Get parameters from GET request
$idArticle = $_GET['idArticle'];
$idUser = $_GET['idUser'];
$change = $_GET['change'];

if ($change == "true") {
    // Query to fetch program ID associated with the article ID
    $sqlProgramId = "SELECT p.id FROM program p WHERE p.idArticle = '$idArticle'";
    $resultProgramId = mysqli_query($conn, $sqlProgramId);

    if (mysqli_num_rows($resultProgramId) > 0) {
        // Fetch program ID
        $row = mysqli_fetch_assoc($resultProgramId);
        $programId = $row['id'];

        // Check if the user is already following the article
        $sqlCheck = "SELECT * FROM following f WHERE f.user_id = '$idUser' AND f.program_id = '$programId'";
        $resultCheck = mysqli_query($conn, $sqlCheck);

        if (mysqli_num_rows($resultCheck) > 0) {
            // User is already following the article, so unfollow (delete record)
            $sqlDelete = "DELETE FROM following WHERE user_id = '$idUser' AND program_id = '$programId'";
            if (mysqli_query($conn, $sqlDelete)) {
                echo json_encode(array("message" => "Unfollowed"));
            } else {
                echo json_encode(array("message" => "Error: " . mysqli_error($conn)));
            }
        } else {
            // User is not following the article, so follow (insert record)
            $sqlInsert = "INSERT INTO following (user_id, program_id) VALUES ('$idUser', '$programId')";
            if (mysqli_query($conn, $sqlInsert)) {
                echo json_encode(array("message" => "Followed"));
            } else {
                echo json_encode(array("message" => "Error: " . mysqli_error($conn)));
            }
        }
    } else {
        echo json_encode(array("message" => "Error: Program ID not found for article ID $idArticle"));
    }
} else {
    // Query to fetch program ID associated with the article ID
    $sqlProgramId = "SELECT p.id FROM program p WHERE p.idArticle = '$idArticle'";
    $resultProgramId = mysqli_query($conn, $sqlProgramId);

    if (mysqli_num_rows($resultProgramId) > 0) {
        // Fetch program ID
        $row = mysqli_fetch_assoc($resultProgramId);
        $programId = $row['id'];

        // Check if the user is already following the article
        $sqlCheck = "SELECT * FROM following f WHERE f.user_id = '$idUser' AND f.program_id = '$programId'";
        $resultCheck = mysqli_query($conn, $sqlCheck);

        if (mysqli_num_rows($resultCheck) > 0) {
            echo json_encode(array("message" => "Followed"));
        } else {
            echo json_encode(array("message" => "Unfollowed"));
        }
    } else {
        echo json_encode(array("message" => "Error: Program ID not found for article ID $idArticle"));
    }
}

mysqli_close($conn);
?>
