package nguyendinhhieu_63134032.mathwhiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.mindrot.jbcrypt.BCrypt;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtFullname;
    private EditText edtConfirmPassword;
    private Button btnRegister;
    private TextView btnLogin;
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
        btnLogin.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void initViews() {
        edtUsername = findViewById(R.id.et_username);
        edtFullname = findViewById(R.id.et_full_name);
        edtPassword = findViewById(R.id.et_password);
        edtConfirmPassword = findViewById(R.id.et_password_confirm);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);
        SpannableString content = new SpannableString("LOGIN");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        btnLogin.setText(content);
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        // Kiểm tra xem tên người dùng đã tồn tại trong cơ sở dữ liệu chưa
        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Tên người dùng đã tồn tại
                    Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Tên người dùng chưa tồn tại, tiến hành đăng ký
                    // Mã hóa mật khẩu trước khi lưu trữ (sử dụng BCrypt)
                    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                    // Tạo người dùng mới
                    User user = new User(username, fullname, hashedPassword);
                    usersRef.child(username).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}