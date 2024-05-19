package nguyendinhhieu_63134032.mathwhiz.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import nguyendinhhieu_63134032.mathwhiz.R;
import nguyendinhhieu_63134032.mathwhiz.model.GameHistory;

public class GameHistoryAdapter extends RecyclerView.Adapter{
    ArrayList<GameHistory> dataSource;

    public GameHistoryAdapter(ArrayList<GameHistory> dataSource) {
        this.dataSource = dataSource;
    }
    public class GameHistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvLevel;
        TextView tvScore;
        TextView tvPlayTime;
        TextView tvTimeStamp;
        TextView tvOperator;
        TextView tvAccuracy;
        public int position;
        public GameHistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvLevel = itemView.findViewById(R.id.tv_item_level);
            tvScore = itemView.findViewById(R.id.tv_item_score);
            tvPlayTime = itemView.findViewById(R.id.tv_item_play_time);
            tvTimeStamp = itemView.findViewById(R.id.tv_item_timestamp);
            tvOperator = itemView.findViewById(R.id.tv_item_operations);
            tvAccuracy = itemView.findViewById(R.id.tv_item_accuracy);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "hehehe", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new GameHistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameHistoryItemViewHolder viewHolder = (GameHistoryItemViewHolder) holder;
        viewHolder.position = holder.getAdapterPosition();
        GameHistory gameHistory = dataSource.get(viewHolder.position);

        switch (gameHistory.getDifficulty()) {
            case "2":
                viewHolder.tvLevel.setText("Easy");
                viewHolder.tvLevel.setTextColor(Color.GREEN); // Green for Easy
                viewHolder.tvOperator.setTextColor(Color.GREEN); // Green for Easy
                break;
            case "3":
                viewHolder.tvLevel.setText("Medium");
                viewHolder.tvLevel.setTextColor(Color.YELLOW); // Green for Easy
                viewHolder.tvOperator.setTextColor(Color.YELLOW); // Green for Easy

                break;
            case "4":
                viewHolder.tvLevel.setText("Hard");
                viewHolder.tvLevel.setTextColor(Color.parseColor("#D80808")); // Green for Easy
                viewHolder.tvOperator.setTextColor(Color.parseColor("#D80808")); // Green for Easy
                break;
        }
        viewHolder.tvScore.setText("Score: " + gameHistory.getScore());
        viewHolder.tvPlayTime.setText("Time: "+ gameHistory.getPlayTime() +" min");
        viewHolder.tvTimeStamp.setText(gameHistory.getTimestamp());
        viewHolder.tvOperator.setText(gameHistory.getOperator());
        viewHolder.tvAccuracy.setText("Accuracy: " + gameHistory.getAccuracy() + "%");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }
}
