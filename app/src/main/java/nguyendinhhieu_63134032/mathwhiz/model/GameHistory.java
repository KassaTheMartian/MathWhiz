package nguyendinhhieu_63134032.mathwhiz.model;

public class GameHistory {
    private long score;
    private long questionsAnswered;
    private long playTime;
    private double accuracy;
    private String timestamp;
    private String difficulty;
    private String operator;

    public GameHistory() {
        // Default constructor required for calls to DataSnapshot.getValue(GameHistory.class)
    }
    public GameHistory(long score, long questionsAnswered, long playTime, double accuracy, String timestamp, String difficulty, String operator) {
        this.score = score;
        this.questionsAnswered = questionsAnswered;
        this.playTime = playTime;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.operator = operator;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(long questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(long accuracy) {
        this.accuracy = accuracy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
