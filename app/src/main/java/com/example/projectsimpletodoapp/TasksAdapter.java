package com.example.projectsimpletodoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }
    public interface OnClickListener{
        void onItemClicked(int position);
    }

    List<String>tasks;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public TasksAdapter(List<String> tasks, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.tasks = tasks;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View todoView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String task = tasks.get(position);
        holder.bind(task);
    }

    //Returns the amount of tasks are in the list
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);

        }

        //Updates the view inside of the view holder with this data
        public void bind(String task) {
            textView.setText(task);
            textView.setOnClickListener(v -> {
                if(clickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

            textView.setOnLongClickListener(v -> {
                //Notify the listener which position was long pressed
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }

}
