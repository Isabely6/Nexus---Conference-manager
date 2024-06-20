<?php
include 'db_connect.php'; // Database connection

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    // Handle GET request: fetch comments
    $sql = "SELECT * FROM question where active = 0";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $questions = array();
        while ($row = $result->fetch_assoc()) {
            $questions[] = $row;
        }
        echo json_encode($questions);
    } else {
        echo json_encode(array());
    }
} elseif ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Handle POST request: update comment status
    if (isset($_POST['id']) && isset($_POST['active'])) {
        $id = $_POST['id'];
        $active = $_POST['active'];

        $sql = "UPDATE question SET active = ? WHERE id = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bind_param("ii", $active, $id);

        if ($stmt->execute()) {
            echo json_encode(array("success" => true, "message" => "Question status updated successfully"));
        } else {
            echo json_encode(array("success" => false, "message" => "Error: " . $stmt->error));
        }

        $stmt->close();
    } else {
        echo json_encode(array("success" => false, "message" => "Invalid request"));
    }
}

$conn->close();
?>
