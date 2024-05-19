package nguyendinhhieu_63134032.mathwhiz.model;

import java.util.HashMap;

public class GameHistory {
    private int score;
    private int questionsAnswered;
    private int playTime;
    private double accuracy;
    private String timestamp;
    private String difficulty;
    private String operator;

    public GameHistory() {
        // Default constructor required for calls to DataSnapshot.getValue(GameHistory.class)
    }
    public GameHistory(int score, int questionsAnswered, int playTime, double accuracy, String timestamp, String difficulty, String operator) {
        this.score = score;
        this.questionsAnswered = questionsAnswered;
        this.playTime = playTime;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
        this.operator = operator;
    }
    public HashMap<String, Object> toFirebaseObject(){
        HashMap<String, Object> gameHistoryObject = new HashMap<>();
        gameHistoryObject.put("score", score);
        gameHistoryObject.put("questionsAnswered", questionsAnswered);
        gameHistoryObject.put("playTime", playTime);
        gameHistoryObject.put("accuracy", accuracy);
        gameHistoryObject.put("timestamp", timestamp);
        gameHistoryObject.put("difficulty", difficulty);
        gameHistoryObject.put("operator", operator);
        return gameHistoryObject;
    }

    public long getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getQuestionsAnswered() {
        return questionsAnswered;
    }

    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
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
