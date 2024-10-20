<?php
date_default_timezone_set("Asia/Manila");
session_start();

// Database credentials
define('DB_SERVER', 'sql108.infinityfree.com');
define('DB_USERNAME', 'if0_37361527');
define('DB_PASSWORD', 'pulsoadmin');
define('DB_DATABASE', 'if0_37361527_pulso');

// Set header for JSON response
header('Content-Type: application/json');

// Create connection
$conn = new mysqli(DB_SERVER, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

// Check connection
if ($conn->connect_error) {
    http_response_code(500); // Internal Server Error
    echo json_encode(array("status" => "error", "message" => "Connection failed: " . $conn->connect_error));
    exit();
}

// SQL query to fetch all user data from tbl_users
$sql = "SELECT 
            user_id, 
            user_lastname, 
            user_firstname, 
            user_email, 
            user_password, 
            user_date_added, 
            user_time_added, 
            user_date_updated, 
            user_time_updated, 
            user_status, 
            user_token, 
            user_access 
        FROM tbl_users";

// Execute the query and store the result
$result = $conn->query($sql);
if ($result === false) {
    http_response_code(500); // Internal Server Error
    echo json_encode(array("status" => "error", "message" => "Query failed: " . $conn->error));
    exit();
}

// Check for results
if ($result->num_rows > 0) {
    $users = array();
    while ($row = $result->fetch_assoc()) {
        $users[] = $row; // Add each row to the users array
    }
    // Return success response with users
    http_response_code(200); // OK
    echo json_encode(array("status" => "success", "data" => $users));
} else {
    // No users found response
    http_response_code(404); // Not Found
    echo json_encode(array("status" => "error", "message" => "No users found."));
}

// Close connection
$conn->close();
?>
