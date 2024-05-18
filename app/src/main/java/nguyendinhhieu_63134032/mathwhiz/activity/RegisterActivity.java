package nguyendinhhieu_63134032.mathwhiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtFullname;
    private EditText edtConfirmPassword;
    private Button btnLogin;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();
        btnRegister.setOnClickListener(v -> register());
    }

    private void initViews() {
        edtUsername = findViewById(R.id.et_username);
        edtFullname = findViewById(R.id.et_full_name);
        edtPassword = findViewById(R.id.et_password);
        edtConfirmPassword = findViewById(R.id.et_password_confirm);
        btnRegister = findViewById(R.id.btn_register);
    }

    private void register(){
        String username = edtUsername.getText().toString();
        String fullname = edtFullname.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (username.isEmpty() || fullname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }
//        // Mã hóa mật khẩu trước khi lưu trữ (sử dụng BCrypt hoặc các phương pháp mã hóa khác)
//        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
//        // Tạo người dùng mới
//        User user = new User(username, fullname, hashedPassword);
        // Create a new user
        User user = new User(username, fullname, password);
        // Save the user to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        usersRef.child(username).setValue(user);
        Toast.makeText(this, "Register successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}