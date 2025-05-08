package com.example.mad_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopUpHistoryAdapter extends RecyclerView.Adapter<TopUpHistoryAdapter.ViewHolder> {

    private List<String> historyList;

    public TopUpHistoryAdapter(List<String> historyList) {
        this.historyList = historyList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

    @NonNull
    @Override
    public TopUpHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUpHistoryAdapter.ViewHolder holder, int position) {
        holder.textView.setText(historyList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
