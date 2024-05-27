package nguyendinhhieu_63134032.mathwhiz.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.GameHistory;
import nguyendinhhieu_63134032.mathwhiz.model.Quiz;

public class QuizActivity extends AppCompatActivity {
    // Khai báo các biến
    // Đối tượng Quiz lưu trữ giá trị của câu hỏi hiện tại, biến này luôn được tạo mới sau một câu hỏi
    private Quiz quiz;
    private ArrayList<GameHistory> listHistory = new ArrayList<>();
    private int countAns = 0; // Đếm số câu trả lời
    private int score = 0; // Đếm số câu trả lời đúng
    private int seconds = 0; // Đếm số giây
    private int level = 0; // Hộ khó của câu hỏi (số lượng số trong câu hỏi)
    private int time = 0; // Thời gian chơi (hết thời gian sẽ game over)
    private String operator = "+-*/"; // Phép toán trong câu hỏi
    // Các nút chứa câu trả lời
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private Button btnD;
    // Nút tạm dừng
    private ImageButton btnPause;
    // Các TextView chứa câu hỏi, điểm và thời gian
    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvTime;
    private View activityView;
    private final Handler handler = new Handler();
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        level = intent.getIntExtra("level",3);
        time = intent.getIntExtra("time", 60);
        operator = intent.getStringExtra("operation");
        currentUser = intent.getStringExtra("currentUser");
        initViews();
        newGame();
        btnA.setOnClickListener(v -> chooseAns(btnA));
        btnB.setOnClickListener(v -> chooseAns(btnB));
        btnC.setOnClickListener(v -> chooseAns(btnC));
        btnD.setOnClickListener(v -> chooseAns(btnD));
        btnPause.setOnClickListener(v -> openPauseDialog());
    }
    // Hàm để chọn câu trả lời
    public void chooseAns(Button btn){
        // Đảm bảo rằng quiz không null và có phương thức getKetQua trả về một chuỗi
        if (quiz != null && btn != null) {
            // Kiểm tra câu trả lời của người dùng
            if (btn.getText().toString().equals(String.valueOf(roundToTwoDecimalPlaces(quiz.getAnswer(), 2)))) {
                score++;
                countAns++;
                Log.e("dung", score + " " + countAns);
            }
            else {
                countAns++;
                Log.e("sai", score + " " + countAns);
            }
            newQues();
        } else {
            Log.e("chonDapAn", "Quiz or Button is null");
        }
    }
    // Hàm để tạo trò chơi mới
    public void newGame(){
        // Tạo một câu hỏi mới
        quiz = new Quiz(level, operator);
        // Đặt số câu trả lời đúng và tổng số câu trả lời về 0
        countAns = 0;
        score = 0;
        seconds = 0;
        tvScore.setText("Score " + score);
        // Hiển thị câu hỏi
        tvQuestion.setText(quiz.getChuoiPhepToan());
        // Hiển thị câu trả lời
        setAns(quiz);
    }

    public void newQues(){
        quiz = new Quiz(level, operator);
        tvScore.setText("Score " + score);
        setAns(quiz);
    }
    public void setAns(Quiz quiz){
        // Tạo một mảng chứa các lựa chọn
        Random random = new Random();
        ArrayList<Double> options = new ArrayList<>();
        Set<Double> uniqueOptions = new HashSet<>();

        // Thêm kết quả chính xác trước
        double correctAnswer = roundToTwoDecimalPlaces(quiz.getAnswer(), 2);
        uniqueOptions.add(correctAnswer);
        options.add(correctAnswer);

        // Thêm các tùy chọn ngẫu nhiên khác nhau
        while (uniqueOptions.size() < 4) {
            double randomOption = roundToTwoDecimalPlaces(random.nextInt(21) - 10 + quiz.getAnswer(), 2);
            if (!uniqueOptions.contains(randomOption)) {
                uniqueOptions.add(randomOption);
                options.add(randomOption);
            }
        }

        Collections.shuffle(options); // Trộn thứ tự các lựa chọn
        // Đặt các lựa chọn vào các nút
        btnA.setText(String.valueOf(options.get(0)));
        btnB.setText(String.valueOf(options.get(1)));
        btnC.setText(String.valueOf(options.get(2)));
        btnD.setText(String.valueOf(options.get(3)));
        tvQuestion.setText(quiz.getChuoiPhepToan());
    }
    // Hàm để làm tròn số với 2 chữ số thập phân
    public static double roundToTwoDecimalPlaces(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("Số chữ số sau dấu phẩy phải là số không âm");

        // Tạo chuỗi định dạng dựa trên số lượng chữ số thập phân
        StringBuilder formatString = new StringBuilder("#.");
        for (int i = 0; i < places; i++) {
            formatString.append('#');
        }

        DecimalFormat df = new DecimalFormat(formatString.toString());
        return Double.parseDouble(df.format(value));
    }

    // Hàm để hiển thị hộp thoại
//    private void showMessageBox(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        // Dừng bộ đếm thời gian
//        onPause();
//        // Thiết lập tiêu đề và nội dung cho hộp thoại
//        builder.setTitle("GAME OVER");
//        builder.setMessage("Bạn trả lời đúng " + countCorrectAns + "\\" + countAns +" câu hỏi" +"\ntrong thời gian " + tvTime.getText().toString());
//
//        // Thiết lập nút "OK"
//        builder.setPositiveButton("Game mới", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                newGame();
//                onResume();
//                dialog.dismiss();
//            }
//        });
//
//        // Tạo và hiển thị hộp thoại
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
    // Hàm để lấy các điều khiển từ giao diện
    public void initViews(){
        btnA = (Button) findViewById(R.id.bnt_a);
        btnB = (Button) findViewById(R.id.bnt_b);
        btnC = (Button) findViewById(R.id.bnt_c);
        btnD = (Button) findViewById(R.id.bnt_d);
        btnPause = (ImageButton) findViewById(R.id.btn_pause);

        tvQuestion = (TextView) findViewById(R.id.tv_de);
        tvScore = (TextView) findViewById(R.id.tv_item_score);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }
    // Runnable để cập nhật thời gian
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            // Tăng thời gian lên 1 giây
            seconds++;
            // Kiểm tra xem thời gian đã hết chưa
            if (seconds > time) {
                // Hiển thị hộp thoại
                openGameOverDialog();
                handler.removeCallbacks(this); // Dừng việc gửi tin nhắn tiếp theo
                onPause(); // Gọi onPause() nếu cần thiết
                return; // Kết thúc runnable
            }
            // Chuyển đổi thời gian từ giây sang phút và giây
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            // Định dạng thời gian
            String timeString = String.format("%02d:%02d", minutes, secs);
            // Cập nhật thời gian trên giao diện
            tvTime.setText(timeString);
            handler.postDelayed(this, 1000); // Gửi tin nhắn sau mỗi giây
        }
    };
    // Hàm để mở hộp thoại tạm dừng
    private void  openPauseDialog() {
        final Dialog dialog = new Dialog(QuizActivity.this);
        activityView = findViewById(R.id.main);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pause);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        // Dừng bộ đếm thời gian
        onPause();

        Button btnResume = dialog.findViewById(R.id.btn_resume);
        Button btnExit = dialog.findViewById(R.id.btn_exit_game_over);

        btnResume.setOnClickListener(v -> {
            dialog.dismiss();
            onResume();
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            addGameHistory(currentUser, listHistory);
            finish();
        });

        dialog.setCancelable(false);
        dialog.show();
        activityView.setAlpha(0.5f);
        dialog.setOnDismissListener(dialog1 -> activityView.setAlpha(1f));
    }
    //Hàm để mở hộp thoại kết thúc trò chơi
    private void openGameOverDialog(){
        final Dialog dialog = new Dialog(QuizActivity.this);
        activityView = findViewById(R.id.main);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game_over);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        Button btnNewGame = dialog.findViewById(R.id.btn_retry_game_over);
        Button btnExit = dialog.findViewById(R.id.btn_exit_game_over);

        TextView tvScore = dialog.findViewById(R.id.tv_score_game_over);
        TextView tvTimeGameOver = dialog.findViewById(R.id.tv_time_game_over);
        TextView tvAccuracy = dialog.findViewById(R.id.tv_accuracy_game_over);
        TextView tvTotalAns = dialog.findViewById(R.id.tv_total_ans_game_over);

        double accuracy = roundToTwoDecimalPlaces((double) score / countAns * 100,2);
        Date date = new Date();
        String stringDate = new SimpleDateFormat("dd-MM-yy HH:mm a").format(date);

        listHistory.add(new GameHistory(score, countAns, time, accuracy , stringDate, String.valueOf(level), operator));
        tvScore.setText(String.valueOf(score));
        tvTimeGameOver.setText(tvTime.getText().toString());
        tvAccuracy.setText(accuracy + "%");
        tvTotalAns.setText(String.valueOf(countAns));
        btnNewGame.setOnClickListener(v -> {
            // Lưu lịch sử chơi
            dialog.dismiss();
            onResume();
            newGame();
        });

        btnExit.setOnClickListener(v -> {
            // Lưu lịch sử chơi
            addGameHistory(currentUser,listHistory);
            dialog.dismiss();
            finish();
        });


        dialog.setCancelable(false);
        dialog.show();
        activityView.setAlpha(0.5f);
        dialog.setOnDismissListener(dialog1 -> activityView.setAlpha(1f));
    }
    private void openGamOverActivity(){
        Intent intent = new Intent(QuizActivity.this, GameOverActivity.class);

        intent.putExtra("level", level);
        intent.putExtra("time", time);
        intent.putExtra("score", score);
        intent.putExtra("countAns", countAns);
        intent.putExtra("time", seconds);
        intent.putExtra("level", level);
        intent.putExtra("operator", operator);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
    // Lưu lịch sử chơi vào Firebase
    private void addGameHistory(String username, ArrayList<GameHistory> listHistory) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users").child(username).child("history");

        for (GameHistory gameHistory : listHistory) {
            // Tạo một khóa ngẫu nhiên
            String historyId = usersRef.push().getKey();
            // Thêm lịch sử chơi vào Firebase
            if (historyId != null)
                usersRef.child(historyId).setValue(gameHistory);
        }
        // Tạo đối tượng HashMap để lưu trữ lịch sử chơi
//        HashMap<String, Object> item = new HashMap<>();
//        item.put(historyId, gameHistory.toFirebaseObject());
//
//        usersRef.updateChildren(item);
    }
    // Hàm để bắt đầu bộ đếm thời gian
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable); // Bắt đầu gửi tin nhắn để cập nhật thời gian
    }
    // Hàm để dừng bộ đếm thời gian
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable); // Dừng gửi tin nhắn khi activity không còn được hiển thị
    }

}