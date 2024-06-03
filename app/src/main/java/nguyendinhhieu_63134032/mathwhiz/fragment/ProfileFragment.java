package nguyendinhhieu_63134032.mathwhiz.fragment;

import static nguyendinhhieu_63134032.mathwhiz.activity.QuizActivity.roundToTwoDecimalPlaces;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Objects;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.activity.LoginActivity;
import nguyendinhhieu_63134032.mathwhiz.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private User currentUser;
    private TextView tvUsername;
    private TextView tvFullname;
    private TextView tvTotalScore;
    private TextView tvTotalTime;
    private TextView tvAverageAccuracy;
    private TextView tvTotalGame;
    private TextView tvTotalAnswer;

    private ImageButton btnLogout;
    private ImageView imgAvatar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("currentUser");
        // Tiến hành khởi tạo các view
        initViews(view);
        btnLogout.setOnClickListener(v -> logout());
        // Lấy thông tin user từ database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Lấy thông tin user từ database
        database.getReference().child("users").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUser = dataSnapshot.getValue(User.class);
                    if (currentUser != null) {
                        tvFullname.setText(currentUser.getFullname());
                        tvUsername.setText("@" + currentUser.getUsername());
                    }
                } else {
                    // Username không tồn tại
                    Toast.makeText(getContext(), "Lỗi không tìm ra DataSnapshot", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
        // Lấy thông tin thống kê lịch sử chơi game
        database.getReference().child("users")
                .child(username)
                .child("history")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Tính toán thông tin thống kê
                int totalScore = 0;
                int totalTime = 0;
                int totalGame = 0;
                int totalAnswer = 0;
                double averageAccuracy = 0;
                // Lấy thông tin từ database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    totalScore += Integer.parseInt(Objects.requireNonNull(snapshot.child("score").getValue()).toString());
                    totalTime += Integer.parseInt(Objects.requireNonNull(snapshot.child("playTime").getValue()).toString());
                    totalAnswer += Integer.parseInt(Objects.requireNonNull(snapshot.child("questionsAnswered").getValue()).toString());
                    totalGame++;
                }
                averageAccuracy = roundToTwoDecimalPlaces((double) totalScore / totalAnswer * 100, 2);

                // Hiển thị thông tin thống kê
                tvTotalScore.setText(String.valueOf(totalScore));
                tvTotalGame.setText(String.valueOf(totalGame));
                tvAverageAccuracy.setText(averageAccuracy + "%");
                tvTotalTime.setText((roundToTwoDecimalPlaces((double) totalTime / 60, 2)) + " min");
                tvTotalAnswer.setText(String.valueOf(totalAnswer));
                Log.e("totalScore", String.valueOf(totalScore));
                Log.e("totalAnswer", String.valueOf(totalAnswer));
                Log.e("averageAccuracy", String.valueOf(averageAccuracy));
                Log.e("totalGame", String.valueOf(totalGame));
                Log.e("totalTime", String.valueOf(totalTime));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(this)
                .load(R.drawable.avt)
                .transform(new CircleCrop())
                .into(imgAvatar);

        return view;
    }
    // Khởi tạo các view
    private void initViews(View v) {
        tvUsername = v.findViewById(R.id.tv_username_profile);
        tvFullname = v.findViewById(R.id.tv_fullname_profile);
        tvAverageAccuracy = v.findViewById(R.id.tv_avg_accuracy);
        tvTotalScore = v.findViewById(R.id.tv_total_score);
        tvTotalTime = v.findViewById(R.id.tv_total_time);
        tvTotalGame = v.findViewById(R.id.tv_total_game);
        tvTotalAnswer = v.findViewById(R.id.tv_total_answer);

        btnLogout = v.findViewById(R.id.btn_logout);
        imgAvatar = v.findViewById(R.id.iv_avatar);
    }

    private void logout() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}