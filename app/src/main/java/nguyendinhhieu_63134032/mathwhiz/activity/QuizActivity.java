package nguyendinhhieu_63134032.mathwhiz.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.Quiz;

public class QuizActivity extends AppCompatActivity {
    private Quiz quiz;
    private int countAns = 0;
    private int countCorrectAns = 0;
    private int seconds = 0;
    private int level = 0;
    private int time = 0;
    private String operator = "+-*/";
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private Button btnD;
    private ImageButton btnPause;
    private TextView tvDe;
    private TextView tvScore;
    private TextView tvTime;
    private View activityView;
    private Handler handler = new Handler();
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
        getControls();
        newGame();
        btnA.setOnClickListener(v -> chonDapAn(btnA));
        btnB.setOnClickListener(v -> chonDapAn(btnB));
        btnC.setOnClickListener(v -> chonDapAn(btnC));
        btnD.setOnClickListener(v -> chonDapAn(btnD));
        btnPause.setOnClickListener(v -> {
            openPauseDialog();
        });
    }

    public void chonDapAn(Button btn){
        // Đảm bảo rằng quiz không null và có phương thức getKetQua trả về một chuỗi
        if (quiz != null && btn != null) {
            // Kiểm tra câu trả lời của người dùng
            if (btn.getText().toString().equals(String.valueOf(quiz.getKetQua()))) {
                countCorrectAns++;
                countAns++;
                Log.e("dung", countCorrectAns + " " + countAns);
            }
            else {
                countAns++;
                Log.e("sai", countCorrectAns + " " + countAns);
            }
            newQues();
        } else {
            Log.e("chonDapAn", "Quiz or Button is null");
        }
    }

    public void newGame(){
        quiz = new Quiz(level, operator);
        countAns = 0;
        countCorrectAns = 0;
        seconds = 0;
        tvScore.setText("Score " + countCorrectAns);
        tvDe.setText(quiz.getChuoiPhepToan());
        setAns(quiz);
    }

    public void newQues(){
        quiz = new Quiz(level, operator);
        tvScore.setText("Score " + countCorrectAns);
        setAns(quiz);
    }
    public void setAns(Quiz quiz){
        // Tạo một mảng chứa các lựa chọn
        Random random = new Random();
        ArrayList<Double> options = new ArrayList<>();
        Set<Double> uniqueOptions = new HashSet<>();

        // Thêm kết quả chính xác trước
        double correctAnswer = roundToTwoDecimalPlaces(quiz.getKetQua());
        uniqueOptions.add(correctAnswer);
        options.add(correctAnswer);

        // Thêm các tùy chọn ngẫu nhiên khác nhau
        while (uniqueOptions.size() < 4) {
            double randomOption = roundToTwoDecimalPlaces(random.nextInt(21) - 10 + quiz.getKetQua());
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
        tvDe.setText(quiz.getChuoiPhepToan());
    }
    // Hàm để làm tròn số với 2 chữ số thập phân
    public static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Double.parseDouble(df.format(value));
    }

    // Hàm để hiển thị hộp thoại
    private void showMessageBox(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chúc mừng"); // Tiêu đề của hộp thoại
        builder.setMessage("Bạn trả lời đúng " + countCorrectAns + "\\" + countAns +" câu hỏi" +"\ntrong thời gian " + tvTime.getText().toString()); // Nội dung của hộp thoại

        // Thiết lập nút "OK"
        builder.setPositiveButton("Game mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGame();
                handler.post(updateTimeRunnable);
                dialog.dismiss();
            }
        });

        // Tạo và hiển thị hộp thoại
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getControls(){
        btnA = (Button) findViewById(R.id.bnt_a);
        btnB = (Button) findViewById(R.id.bnt_b);
        btnC = (Button) findViewById(R.id.bnt_c);
        btnD = (Button) findViewById(R.id.bnt_d);
        btnPause = (ImageButton) findViewById(R.id.btn_pause);

        tvDe = (TextView) findViewById(R.id.tv_de);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            if (seconds > time) {
                handler.removeCallbacks(updateTimeRunnable);
                showMessageBox(QuizActivity.this);
            }
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            String timeString = String.format("%02d:%02d", minutes, secs);
            tvTime.setText(timeString);
            handler.postDelayed(this, 1000); // Gửi tin nhắn sau mỗi giây
        }
    };
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
        handler.removeCallbacks(updateTimeRunnable);

        Button btnResume = dialog.findViewById(R.id.btn_resume);
        Button btnExit = dialog.findViewById(R.id.btn_exit);

        btnResume.setOnClickListener(v -> {
            dialog.dismiss();
            handler.post(updateTimeRunnable);
        });

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.setCancelable(true);
        dialog.show();
        activityView.setAlpha(0.5f);
        dialog.setOnDismissListener(dialog1 -> activityView.setAlpha(1f));
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable); // Bắt đầu gửi tin nhắn để cập nhật thời gian
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable); // Dừng gửi tin nhắn khi activity không còn được hiển thị
    }

}