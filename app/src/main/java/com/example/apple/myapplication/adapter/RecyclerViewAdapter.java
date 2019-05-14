package com.example.apple.myapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 测试列表
 *
 * @author bonepeople
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        TextView textView = new TextView(parent.getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        textView.setBackgroundColor(0xFF669966);
        holder = new ViewHolder_item(textView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    private class ViewHolder_item extends RecyclerView.ViewHolder {
        TextView textView_name;

        ViewHolder_item(TextView itemView) {
            super(itemView);
            textView_name = itemView;
        }
    }
}
