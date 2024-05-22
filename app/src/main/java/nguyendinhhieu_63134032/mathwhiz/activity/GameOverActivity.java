package nguyendinhhieu_63134032.mathwhiz.activity;

import static nguyendinhhieu_63134032.mathwhiz.activity.QuizActivity.roundToTwoDecimalPlaces;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.GameHistory;

public class GameOverActivity extends AppCompatActivity {
    private Button btnRetry;
    private Button btnExit;
    private TextView tvScore;
    private TextView tvTimeGameOver;
    private TextView tvAccuracy;
    private TextView tvTotalAns;
    private TextView tvLevel;
    private TextView tvOperator;

    private String currentUser;
    private String operator;
    private int level;
    private int score;
    private int countAns;
    private int time;
    private double accuracy;
    private String stringDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        Intent intent = getIntent();

        score = intent.getIntExtra("score", 0);
        countAns = intent.getIntExtra("countAns", 0);
        time = intent.getIntExtra("time", 0);
        level = intent.getIntExtra("level", 0);
        operator = intent.getStringExtra("operator");
        currentUser = intent.getStringExtra("currentUser");
        accuracy = roundToTwoDecimalPlaces((double) score / countAns * 100,2);
        Date date = new Date();
        stringDate = new SimpleDateFormat("dd-MM-yy HH:mm a").format(date);


        tvScore.setText(String.valueOf(score));
        tvTimeGameOver.setText(time/60 + " min");
        tvAccuracy.setText(accuracy + "%");
        tvTotalAns.setText(String.valueOf(countAns));
        btnRetry.setOnClickListener(v -> {
            // Chuyển về màn hình chơi
//            Intent intent1 = new Intent(GameOverActivity.this, QuizActivity.class);
//            intent1.putExtra("level", level);
//            intent1.putExtra("time", time);
//            intent1.putExtra("operator", operator);
//            intent1.putExtra("currentUser", currentUser);
//            startActivity(intent1);
            finish();
//            // Lưu lịch sử chơi
//            GameHistory gameHistory = new GameHistory(score, countAns, time, accuracy,stringDate, String.valueOf(level), operator);
//            addGameHistory(currentUser,gameHistory);

        });
        btnExit.setOnClickListener(v -> {
            // Lưu lịch sử chơi
            GameHistory gameHistory = new GameHistory(score, countAns, time, accuracy,stringDate, String.valueOf(level), operator);
            addGameHistory(currentUser,gameHistory);
            // Chuyển về màn hình chính
            Intent intent1 = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent1);
        });
    }
    // Lưu lịch sử chơi vào Firebase
    private void addGameHistory(String username, GameHistory gameHistory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users").child(username).child("history");
        // Tạo một khóa ngẫu nhiên
        String historyId = usersRef.push().getKey();
        // Thêm lịch sử chơi vào Firebase
        if (historyId != null)
            usersRef.child(historyId).setValue(gameHistory);
    }
    // Khởi tạo view
    private void initViews(){
        btnRetry = findViewById(R.id.btn_retry_game_over);
        btnExit = findViewById(R.id.btn_exit_game_over);

        tvLevel = findViewById(R.id.tv_level_game_over);
        tvOperator = findViewById(R.id.tv_operator_game_over);
        tvScore = findViewById(R.id.tv_score_game_over);
        tvTimeGameOver = findViewById(R.id.tv_time_game_over);
        tvAccuracy = findViewById(R.id.tv_accuracy_game_over);
        tvTotalAns = findViewById(R.id.tv_total_ans_game_over);
    }
}

