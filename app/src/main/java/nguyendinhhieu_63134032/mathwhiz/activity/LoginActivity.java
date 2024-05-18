package nguyendinhhieu_63134032.mathwhiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
    private void initViews() {
        edtUsername = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
    }
    public void login() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users").child(username);
        Log.w("LoginActivity", "Username: " + username + ", Password: " + password);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Xác thực mật khẩu
                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            // Đăng nhập thành công
                            Log.d("LoginActivity", "Đăng nhập thành công bằng username.");
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                            // Chuyển sang activity tiếp theo
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish(); // Kết thúc activity hiện tại
                        } else {
                            // Mật khẩu không trùng khớp
                            Log.w("LoginActivity", "Mật khẩu không chính xác.");
                            Toast.makeText(LoginActivity.this, "Mật khẩu không chính xác. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Username không tồn tại
                    Log.w("LoginActivity", "Username không tồn tại.");
                    Toast.makeText(LoginActivity.this, "Username không tồn tại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("LoginActivity", "Lỗi khi truy vấn dữ liệu từ Firebase: " + error.getMessage());
                Toast.makeText(LoginActivity.this, "Lỗi khi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}