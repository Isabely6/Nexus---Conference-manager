<?php

include 'db_connect.php'; // Database connection

if (isset($_POST["title"]) && isset($_POST["track"]) && isset($_POST["author"])) {

    $title = $_POST['title'];
    $track = $_POST['track'];
    $author = $_POST['author'];
    $trackdesc = $_POST['trackdesc'];
    $date = $_POST['date'];
    $time = $_POST['time'];
    $room = $_POST['room'];
    $description = $_POST['description'];

    $dateFormat = 'F d';
    $dateObj = DateTime::createFromFormat($dateFormat, $date);
    $formattedDate = $dateObj->format('Y-m-d');

    // Article SQL query
    $sql1 = "INSERT INTO article (title, authors, description) VALUES ('$title', '$author', '$description')";
    $result = $conn->query($sql1);

    // Execute Article SQL query
    if ($result) {
        $article_id = $conn->insert_id;
        echo json_encode(array("success" => true, "message" => "New article record created successfully"));
    } else {
        echo json_encode(array("success" => false, "message" => "Error: ". $sql1. "<br>". $conn->error));
    }
}else{
    echo json_encode(array("success" => false, "message" => "Invalid request"));
}

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

// Check if track already exists
$stmt_check = $conn->prepare("SELECT id FROM track WHERE name =?");
$stmt_check->bind_param("s", $track);
$stmt_check->execute();
$stmt_check->bind_result($track_id);
$stmt_check->fetch();
$stmt_check->close();

if ($track_id == 0) {
    $assignedColor = '';

    while (true) {
        $randomIndex = rand(0, count($colors) - 1);
        $newColor = $colors[$randomIndex];

        // Check if the color already exists in the database
        $stmt_color_check = $conn->prepare("SELECT COUNT(*) as count FROM track WHERE color =?");
        $stmt_color_check->bind_param("s", $newColor);
        $stmt_color_check->execute();
        $stmt_color_check->bind_result($colorExists);
        $stmt_color_check->fetch();
        $stmt_color_check->close();

        if (!$colorExists) {
            $assignedColor = $newColor;
            break; // Exit the loop if the color doesn't exist
        }
    }

    // If track name does not exist and color is new, insert new track
    $stmt1 = $conn->prepare("INSERT INTO track (name, description, color) VALUES (?,?,?)");
    $stmt1->bind_param("sss", $track, $trackdesc, $assignedColor);

    if ($stmt1->execute()) {
        $idoftrack = $conn->insert_id;
        echo "New track record created successfully with ID: $idoftrack";
    } else {
        echo "Error: ". $stmt1->error;
    }

    $stmt1->close();
} else {
    // If track name exists, fetch the existing track ID
    $stmt2 = $conn->prepare("SELECT id FROM track WHERE name =?");
    $stmt2->bind_param("s", $track);
    $stmt2->execute();
    $stmt2->bind_result($track_id);
    $stmt2->fetch();
    $stmt2->close();
    $idoftrack = $track_id;

    echo "Track name already exists with ID: $idoftrack. Skipping insertion.";
}


//Program SQL Query
$stmt3 = $conn->prepare("INSERT INTO program (idArticle, idTrack, room, date, hour) VALUES (?, ?, ?, ?, ?)");
$stmt3->bind_param("issss", $article_id, $idoftrack, $room, $formattedDate, $time);

if ($stmt3->execute()) {
    echo "New program record created successfully";
    $stmt3->close();
} else {
    echo "Error: " . $stmt3->error;
    $stmt3->close();
}

$conn->close();
?>

