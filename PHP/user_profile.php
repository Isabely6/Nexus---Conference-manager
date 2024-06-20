<?php
include 'db_connect.php'; 

$idUser = $_GET['idUser'];

// Query to retrieve data
$sql = "SELECT 
    u.user, 
    u.email,
    p.date, 
    p.hour, 
    p.room, 
    a.title,
    t.color,
    a.id
FROM 
    user u
LEFT JOIN 
    following f ON f.user_id = u.id
LEFT JOIN 
    program p ON f.program_id = p.id
LEFT JOIN 
    article a ON p.idArticle = a.id
left join
    track t on p.idTrack = t.id
WHERE 
    u.id = '$idUser'

";
$result = mysqli_query($conn, $sql);

// Create an array to store the data
$list = array();

while ($row = mysqli_fetch_assoc($result)) {
    $date = $row["date"];
    $formatted_date = date('F j', strtotime($date));
    $list[] = array(
        "idArticle" => $row["id"],
        "user" => $row["user"],
        "email" => $row["email"],
        "hour" => $row["hour"],
        "date" => $formatted_date,
        "room" => $row["room"],
        "title" => $row["title"],
        "color" => $row["color"]
    );
}

// Close the connection
mysqli_close($conn);

// Output the data in JSON format
echo json_encode($list);
?>