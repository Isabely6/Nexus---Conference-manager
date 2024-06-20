<?php

include 'db_connect.php';

// Get the idArticle from the POST request
$idArticle = $_POST["idArticle"];

// Get the program id
$sql = "SELECT id FROM program WHERE idArticle = '$idArticle'";
$result = mysqli_query($conn, $sql);
$program_id = mysqli_fetch_assoc($result)['id'];

// Delete related records from following table
$sql = "DELETE FROM following WHERE program_id = '$program_id'";
mysqli_query($conn, $sql);

// Delete the article from the database
$sql = "DELETE FROM article WHERE id = '$idArticle'";

if (mysqli_query($conn, $sql)) {
    echo "Article deleted successfully";

    // Delete related records from program table
    $sql = "DELETE FROM program WHERE idArticle = '$idArticle'";
    mysqli_query($conn, $sql);

    // Delete related records from question table
    $sql = "DELETE FROM question WHERE idArticle = '$idArticle'";
    mysqli_query($conn, $sql);

} else {
    echo "Error: ". mysqli_error($conn);
}

// Close the database connection
mysqli_close($conn);
?>