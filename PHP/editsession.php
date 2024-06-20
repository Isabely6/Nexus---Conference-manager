<?php

include 'db_connect.php'; // Database connection

// Query to retrieve data
$sql = "SELECT p.date, p.hour, p.room, a.title, t.name as trackName, t.description as trackDesc, t.color, p.idArticle, a.authors, a.description as articleDesc, a.id as idArticle
FROM program p
JOIN article a ON p.idArticle = a.id
join track t on t.id = p.idTrack
";
$result = mysqli_query($conn, $sql);

// Create an array to store the data
$list = array();

while ($row = mysqli_fetch_assoc($result)) {
    $date = $row["date"];
    $formatted_date = date('F j', strtotime($date));
    $list[] = array(
        "idArticle" => $row["idArticle"],
        "author" => $row["authors"],
        "trackName" => $row["trackName"],
        "trackDesc" => $row["trackDesc"],
        "articleName" => $row["title"],
        "date" => $formatted_date,
        "hour" => $row["hour"],
        "room" => $row["room"],
        "color" => $row["color"],
        "abstract" => $row["articleDesc"]
    );
}

// Close the connection
mysqli_close($conn);

// Output the data in JSON format
echo json_encode($list);
?>