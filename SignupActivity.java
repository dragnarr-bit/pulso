package com.example.pulsomobile.Signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pulsomobile.MainActivity;
import com.example.pulsomobile.R;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    EditText signupFirstName;
    EditText signupLastName;
    EditText signupEmail;
    EditText signupPassword;
    EditText confirmPassword;
    Button signupButton;
    TextView loginText;

    private static final String SIGNUP_URL = "https://pulso.site/insert_user.php";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        signupFirstName = findViewById(R.id.first_name);
        signupLastName = findViewById(R.id.last_name);
        signupEmail = findViewById(R.id.email);
        signupPassword = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.confirm_password);
        signupButton = findViewById(R.id.signupButton);
        loginText = findViewById(R.id.loginText);

        // Set click listener for the signup button
        signupButton.setOnClickListener(view -> handleSignup());

        // Set click listener for the login text
        loginText.setOnClickListener(view -> openLoginActivity());
    }

    // Method to handle signup process
    private void handleSignup() {
        String firstName = signupFirstName.getText().toString().trim();
        String lastName = signupLastName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String confirmPasswordText = confirmPassword.getText().toString().trim();

        // Validate fields
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please enter your first and last name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(SignupActivity.this, "Password must be at least 8 characters, contain an uppercase letter, a lowercase letter, a digit, and a special character", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPasswordText)) {
            Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create JSON object for the API request
            JSONObject json = new JSONObject();
            json.put("user_firstname", firstName);
            json.put("user_lastname", lastName);
            json.put("user_email", email);
            json.put("user_password", password);
            json.put("user_access", "User"); // Default user access level

            // Send API request
            sendSignupRequest(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SignupActivity.this, "Error creating signup data", Toast.LENGTH_SHORT).show();
        }
    }

    // Email validation
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation
    private boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
        return passwordPattern.matcher(password).matches();
    }

    // Method to send signup request to the server
    private void sendSignupRequest(String jsonData) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(SIGNUP_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Signup failed: Network error", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Method to open the login activity
    public void openLoginActivity() {
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
