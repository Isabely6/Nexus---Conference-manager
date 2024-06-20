<?php
include 'db_connect.php'; // Database connection

$colors = array(
    "#D2DAFF",
    "#FFBA72",
    "#FFD2F0",
    "#B8DFB0",
    "#FF7D7D",
    "#7DFFE4",
    "#B27DFF",
    "#FF7D98",
    "#00A80C",
    "#5E7DFF",
    "#C2FF5E",
    "#FFE05E"
);

// Get and sanitize all the input variables
$idArticle = $_POST['idArticle'];
$articleName =  $_POST['articleName'];
$trackName =  $_POST['trackName'];
$trackDesc =  $_POST['trackDesc'];
$author =  $_POST['author'];
$date = $_POST['date'];
$time = $_POST['time'];
$room = $_POST['room'];
$abstract = $_POST['abstract'];

$dateFormat = 'F d';
$dateObj = DateTime::createFromFormat($dateFormat, $date);
$formattedDate = $dateObj->format('Y-m-d');

// Retrieve the programID based on idArticle
$queryProgram = "SELECT id FROM program WHERE idArticle = '$idArticle'";
$resultProgram = mysqli_query($conn, $queryProgram);

if (mysqli_num_rows($resultProgram) > 0) {
    $row = mysqli_fetch_assoc($resultProgram);
    $programID = $row['id'];

    // Check if the track exists
    $stmt_check = $conn->prepare("SELECT id FROM track WHERE name = ?");
    $stmt_check->bind_param("s", $trackName);
    $stmt_check->execute();
    $stmt_check->bind_result($trackID);
    $stmt_check->fetch();
    $stmt_check->close();

    if (!$trackID) {
        $assignedColor = '';

        while (true) {
            $randomIndex = rand(0, count($colors) - 1);
            $newColor = $colors[$randomIndex];

            // Check if the color already exists in the database
            $stmt_color_check = $conn->prepare("SELECT COUNT(*) as count FROM track WHERE color = ?");
            $stmt_color_check->bind_param("s", $newColor);
            $stmt_color_check->execute();
            $stmt_color_check->bind_result($colorExists);
            $stmt_color_check->fetch();
            $stmt_color_check->close();

            if ($colorExists == 0) {
                $assignedColor = $newColor;
                break; // Exit the loop if the color doesn't exist
            }
        }

        // If track name does not exist and color is new, insert new track
        $stmt1 = $conn->prepare("INSERT INTO track (name, description, color) VALUES (?, ?, ?)");
        $stmt1->bind_param("sss", $trackName, $trackDesc, $assignedColor);

        if ($stmt1->execute()) {
            $trackID = $conn->insert_id;
        } else {
            echo "Error: " . $stmt1->error;
            $stmt1->close();
            mysqli_close($conn);
            exit();
        }
        $stmt1->close();
    } else {
        // Track exists, update its description
        $stmt_update_track = $conn->prepare("UPDATE track SET description = ? WHERE id = ?");
        $stmt_update_track->bind_param("si", $trackDesc, $trackID);
        $stmt_update_track->execute();
        $stmt_update_track->close();
    }

    // Update the article information
    $stmt_update_article = $conn->prepare("UPDATE article SET title = ?, authors = ?, description = ? WHERE id = ?");
    $stmt_update_article->bind_param("sssi", $articleName, $author, $abstract, $idArticle);
    $articleUpdated = $stmt_update_article->execute();
    $stmt_update_article->close();

    // Update the program information
    $stmt_update_program = $conn->prepare("UPDATE program SET date = ?, hour = ?, room = ?, idTrack = ? WHERE id = ?");
    $stmt_update_program->bind_param("sssii", $formattedDate, $time, $room, $trackID, $programID);
    $programUpdated = $stmt_update_program->execute();
    $stmt_update_program->close();

    if ($articleUpdated && $programUpdated) {
        echo "Session updated successfully";
    } else {
        echo "Error updating session: " . mysqli_error($conn);
    }
} else {
    echo "Program not found";
}

// Close the connection
mysqli_close($conn);
?>