<?php

include 'db_connect.php'; // Database connection

// Query to retrieve data
$sql = "SELECT p.date, p.hour, p.room, a.title, t.name, t.color, p.idArticle, a.authors
FROM program p
JOIN article a ON p.idArticle = a.id
join track t on t.id = p.idTrack;
";
$result = mysqli_query($conn, $sql);

// Create an array to store the data
$list = array();

while ($row = mysqli_fetch_assoc($result)) {
    $date = $row["date"];
    $formatted_date = date('F j', strtotime($date));
    $list[] = array(
        "idArticle" => $row["idArticle"],
        "authors" => $row["authors"],
        "trackName" => $row["name"],
        "title" => $row["title"],
        "date" => $formatted_date,
        "hour" => $row["hour"],
        "room" => $row["room"],
        "color" => $row["color"]
    );
}

// Close the connection
mysqli_close($conn);

// Output the data in JSON format
echo json_encode($list);
?>