package nguyendinhhieu_63134032.mathwhiz.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.activity.QuizActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private Button btnStandard;
    private int time = 60;
    private int level = 2;
    private String operation = "+-*/";
    private int countOperation = 4;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo Button
        btnStandard = view.findViewById(R.id.btn_standard);
        btnStandard.setOnClickListener(v -> {
            openStandardDialog();

        });
        return view;
    }
    private void  openStandardDialog() {
        // Khởi tạo dialog
        final Dialog dialog = new Dialog(getContext());
        View rootView = getView();
        // Không hiển thị tiêu đề của dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Gán giao diện cho dialog
        dialog.setContentView(R.layout.dialog_ready);
        // Lấy cửa sổ của dialog
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        // Đặt kích thước của dialog
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Đặt màu nền của dialog
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Đặt vị trí của dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        // Tìm các điều khiển trong dialog
        ToggleButton tb1min = dialog.findViewById(R.id.tb_one_min);
        ToggleButton tb2min = dialog.findViewById(R.id.tb_two_min);
        ToggleButton tb3min = dialog.findViewById(R.id.tb_three_min);
        ToggleButton tbEasy = dialog.findViewById(R.id.tb_easy);
        ToggleButton tbMedium = dialog.findViewById(R.id.tb_medium);
        ToggleButton tbHard = dialog.findViewById(R.id.tb_hard);
        ToggleButton tbAdd = dialog.findViewById(R.id.tb_add);
        ToggleButton tbMinus = dialog.findViewById(R.id.tb_minus);
        ToggleButton tbMulti = dialog.findViewById(R.id.tb_multi);
        ToggleButton tbDivide = dialog.findViewById(R.id.tb_divide);
        Button btnStart = dialog.findViewById(R.id.btn_start);

        // Chọn thời gian chơi
        tb1min.setOnClickListener(v -> {
            tb1min.setChecked(true);
            tb2min.setChecked(false);
            tb3min.setChecked(false);
            time = 60;
        });
        tb2min.setOnClickListener(v -> {
            tb1min.setChecked(false);
            tb2min.setChecked(true);
            tb3min.setChecked(false);
            time = 60;
        });
        tb3min.setOnClickListener(v -> {
            tb1min.setChecked(false);
            tb2min.setChecked(false);
            tb3min.setChecked(true);
            time = 60;

        });
        // Chọn level
        tbEasy.setOnClickListener(v -> {
            tbEasy.setChecked(true);
            tbMedium.setChecked(false);
            tbHard.setChecked(false);
            level = 2;
        });
        tbMedium.setOnClickListener(v -> {
            tbEasy.setChecked(false);
            tbMedium.setChecked(true);
            tbHard.setChecked(false);
            level = 3;

        });
        tbHard.setOnClickListener(v -> {
            tbEasy.setChecked(false);
            tbMedium.setChecked(false);
            tbHard.setChecked(true);
            level = 4;

        });
        // Chọn phép toán
        tbAdd.setOnCheckedChangeListener(chooseOperationListener);
        tbMinus.setOnCheckedChangeListener(chooseOperationListener);
        tbMulti.setOnCheckedChangeListener(chooseOperationListener);
        tbDivide.setOnCheckedChangeListener(chooseOperationListener);
        // Nhấn nút bắt đầu
        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra("time", time);
            intent.putExtra("level", level);
            intent.putExtra("operation", operation);
            startActivity(intent);
            dialog.dismiss();
        });
        // Hiển thị dialog
        dialog.show();
        rootView.setAlpha(0.1f);
        dialog.setOnDismissListener(dialog1 -> rootView.setAlpha(1f));
    }
    private CompoundButton.OnCheckedChangeListener chooseOperationListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String o = buttonView.getText().toString();
            if (isChecked) {
                if (!operation.contains(o)) {
                    operation += o;
                    countOperation++;
                }
            } else {
                if (operation.contains(o)) {
                    operation = operation.replace(o, "");
                    countOperation--;
                    if (countOperation == 0) {
                        Toast.makeText(getContext(), "Chọn ít nhất 1 phép toán", Toast.LENGTH_SHORT).show();
                        buttonView.setChecked(true);
                        operation += o;
                        countOperation++;
                    }
                }
            }
        }
    };

}