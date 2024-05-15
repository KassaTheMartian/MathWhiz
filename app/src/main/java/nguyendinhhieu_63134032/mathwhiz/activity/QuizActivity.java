package nguyendinhhieu_63134032.mathwhiz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Random;

import nguyendinhhieu_63134032.mathwhiz.model.Quiz;
import nguyendinhhieu_63134032.mathwhiz.R;

public class QuizActivity extends AppCompatActivity {
    private ArrayList<Quiz> listQuiz = new ArrayList<Quiz>();
    private int count = 0;
    private int countKq = 0;
    private int seconds = 0;
    private int size = 10;
    private int doKho = 3;
    private Button btnA;
    private Button btnB;
    private Button btnC;
    private Button btnD;
    //private Button btnNewGame;
    private TextView tvDe;
    private TextView tvScore;
    private TextView tvTime;
    //private TextView tvCount;
    //private RadioGroup rgDokho;

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

        getControls();
        newGame();
        btnA.setOnClickListener(v -> chonDapAn(btnA));
        btnB.setOnClickListener(v -> chonDapAn(btnB));
        btnC.setOnClickListener(v -> chonDapAn(btnC));
        btnD.setOnClickListener(v -> chonDapAn(btnD));
        //btnNewGame.setOnClickListener(v -> newGame());
    }

    public void chonDapAn(Button btn){
        if (btn.getText().toString().equals(String.valueOf(listQuiz.get(count).tinhChuoiPhepToan()))){
            countKq++;
            newQues();
        }else {
            newQues();
        }
    }

    public void mergeQuiz() {
        Collections.shuffle(listQuiz);
    }
    public void newGame(){

//        RadioButton selectedRb = (RadioButton) findViewById(rgDokho.getCheckedRadioButtonId());
//        String strDoKho = selectedRb.getText().toString();
//        if (strDoKho.equals("2 số")) doKho = 2;
//        if (strDoKho.equals("3 số")) doKho = 3;
//        if (strDoKho.equals("4 số")) doKho = 4;

        listQuiz = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            listQuiz.add(new Quiz(doKho));
        }

        mergeQuiz();
        count = 0;
        countKq = 0;
        seconds = 0;
        //tvCount.setText((count) + "\\" + size);
        tvScore.setText("Score " + countKq);
        tvDe.setText(listQuiz.get(count).getChuoiPhepToan());
        setAns(listQuiz.get(count));
    }

    public void newQues(){
        count++;
        if(count == size) {
            showMessageBox(this);
        }
        //tvCount.setText((count) + "\\" + size);
        tvScore.setText("Score " + countKq);

        setAns(listQuiz.get(count));
    }
    public void setAns(Quiz quiz){
        Random random = new Random();
        ArrayList<Double> options = new ArrayList<>();

        options.add(roundToTwoDecimalPlaces(quiz.tinhChuoiPhepToan()));
        options.add(roundToTwoDecimalPlaces(random.nextDouble() * 15));
        options.add(roundToTwoDecimalPlaces(random.nextDouble() * 15));
        options.add(roundToTwoDecimalPlaces(random.nextDouble() * 15));

        Collections.shuffle(options); // Trộn thứ tự các lựa chọn

        btnA.setText(String.valueOf(options.get(0)));
        btnB.setText(String.valueOf(options.get(1)));
        btnC.setText(String.valueOf(options.get(2)));
        btnD.setText(String.valueOf(options.get(3)));

        tvDe.setText(listQuiz.get(count).getChuoiPhepToan());
    }

    public static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat df = new DecimalFormat("#.#");
        return Double.parseDouble(df.format(value));
    }

    // Hàm để hiển thị hộp thoại
    private void showMessageBox(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chúc mừng"); // Tiêu đề của hộp thoại
        builder.setMessage("Bạn trả lời đúng " + countKq + "\\" + count +" câu hỏi" +"\ntrong thời gian " + tvTime.getText().toString()); // Nội dung của hộp thoại

        // Thiết lập nút "OK"
        builder.setPositiveButton("Game mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGame();
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
        //btnNewGame = (Button) findViewById(R.id.btn_new_game);
        tvDe = (TextView) findViewById(R.id.tv_de);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvTime = (TextView) findViewById(R.id.tv_time);
        //tvCount = (TextView) findViewById(R.id.tv_count);
        //rgDokho = (RadioGroup) findViewById(R.id.rg_dokho);
    }
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            int minutes = (seconds % 3600) / 60;
            int secs = seconds % 60;
            String timeString = String.format("%02d:%02d", minutes, secs);
            tvTime.setText(timeString);
            handler.postDelayed(this, 1000); // Gửi tin nhắn sau mỗi giây
        }
    };
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