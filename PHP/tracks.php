<?php

include 'db_connect.php'; // Database connection

// Query to retrieve tracks data
$sql = "SELECT * FROM track";
$result = mysqli_query($conn, $sql);

// Create an array to store the tracks data
$tracks = array();

while ($row = mysqli_fetch_assoc($result)) {
    $tracks[] = array(
        "id" => $row["id"],
        "name" => $row["name"],
        "description" => $row["description"],
        "color" => $row["color"]
    );
}

// Query to retrieve article-track relationships
$sql = "SELECT idTrack FROM program";
$result = mysqli_query($conn, $sql);

// Create an array to store the track IDs that are attached to articles
$attached_tracks = array();

while ($row = mysqli_fetch_assoc($result)) {
    $attached_tracks[] = $row["idTrack"];
}

// Delete tracks that are not attached to any article
foreach ($tracks as $key => $track) {
    if (!in_array($track["id"], $attached_tracks)) {
        $sql = "DELETE FROM track WHERE id = ". $track["id"];
        mysqli_query($conn, $sql);
        unset($tracks[$key]); // Remove the track from the $tracks array
    }
}

// Close the connection
mysqli_close($conn);

// Output the tracks data in JSON format
echo json_encode($tracks);

?>