<?php
// Connect to your database
$conn = mysqli_connect("localhost", "root", "", "conferencedbam");

// Check connection
if (!$conn) {
    die("Connection failed: ". mysqli_connect_error());
}

// Query to retrieve data
$sql = "SELECT p.date, p.hour, p.room, a.title, t.color, a.id
FROM program p
join track t on t.id = p.idTrack
JOIN article a ON p.idArticle = a.id
";
$result = mysqli_query($conn, $sql);

// Create an array to store the data
$list = array();

while ($row = mysqli_fetch_assoc($result)) {
    $date = $row["date"];
    $formatted_date = date('F j', strtotime($date));
    $list[] = array(
        "idArticle" =>$row["id"],
        "hour" => $row["hour"],
        "room" => $row["room"],
        "title" => $row["title"],
        "date" => $formatted_date,
        "color" => $row["color"]
    );
}

// Close the connection
mysqli_close($conn);

// Output the data in JSON format
echo json_encode($list);
?>