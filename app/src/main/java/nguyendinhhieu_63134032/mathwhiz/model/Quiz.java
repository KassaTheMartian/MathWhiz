package nguyendinhhieu_63134032.mathwhiz.model;

import java.util.Random;
import java.util.Stack;

public class Quiz {
    String chuoiPhepToan;
    double answer;
    public Quiz(String phepToan) {
        this.chuoiPhepToan = phepToan;
    }

    public Quiz(int n, String phepToan){
        this.chuoiPhepToan = taoChuoiPhepToan(n, phepToan);
        this.answer = tinhChuoiPhepToan();
    }

    public static String taoChuoiPhepToan(int n, String phepToan) {
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();

        // Thêm số đầu tiên vào chuỗi
        builder.append(rand.nextInt(10)); // Tạo số ngẫu nhiên từ 0 đến 9

        // Thêm n-1 phép toán và số kèm theo
        for (int i = 0; i < n - 1; i++) {
            // Chọn ngẫu nhiên một toán tử từ +, -, *
            char operator = phepToan.charAt(rand.nextInt(phepToan.length()));
            // Thêm toán tử vào chuỗi
            builder.append(" ").append(operator).append(" ");
            // Thêm số tiếp theo vào chuỗi
            int nextNumber = rand.nextInt(10);
            if (operator == '/' && nextNumber == 0) {
                // Nếu là phép chia và số kế tiếp là 0, thì chuyển thành phép cộng
                builder.append(1); // Thêm số 1 thay vì số 0
            } else {
                builder.append(nextNumber);
            }
        }

        return builder.toString();
    }
    public double tinhChuoiPhepToan() {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> toanTu = new Stack<>();

        for (int i = 0; i < chuoiPhepToan.length(); i++) {
            char c = chuoiPhepToan.charAt(i);
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append(c);
                while (i + 1 < chuoiPhepToan.length() && Character.isDigit(chuoiPhepToan.charAt(i + 1))) {
                    sb.append(chuoiPhepToan.charAt(++i));
                }
                numbers.push(Double.parseDouble(sb.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!toanTu.empty() && uuTien(toanTu.peek()) >= uuTien(c)) {
                    apDungToanTu(numbers, toanTu);
                }
                toanTu.push(c);
            }
        }
        while (!toanTu.empty()) {
            apDungToanTu(numbers, toanTu);
        }
        return numbers.pop();
    }

    public int uuTien(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return -1;
    }

    public void apDungToanTu(Stack<Double> numbers, Stack<Character> operators) {
        double b = numbers.pop();
        double a = numbers.pop();
        char operator = operators.pop();
        double result = 0.0;
        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                result = a / b;
                break;
        }
        numbers.push(result);
    }

    public String getChuoiPhepToan() {
        return chuoiPhepToan;
    }

    public double getAnswer() {
        return answer;
    }
}
