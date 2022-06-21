package com.example.internetapiexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentDisplayAdapter extends RecyclerView.Adapter<ContentDisplayAdapter.ViewHolder> {
    private List<String> content_strings;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView thing_item;

        public ViewHolder(View view) {
            super(view);
            thing_item = view.findViewById(R.id.a_content);
        }
    }

    public ContentDisplayAdapter(List<String> content_str) {
        this.content_strings = content_str;
    }

    @Override
    public int getItemViewType(int position) {
        // 给每个ItemView指定不同的类型，这样在RecyclerView看来，这些ItemView全是不同的，不能复用
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String a_thing = this.content_strings.get(position);
        holder.thing_item.setText(a_thing);

    }

    @Override
    public int getItemCount() {
        return this.content_strings.size();
    }
}
