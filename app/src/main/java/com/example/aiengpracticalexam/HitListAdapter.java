package com.example.aiengpracticalexam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aiengpracticalexam.models.Hit;

import java.util.ArrayList;

public class HitListAdapter extends RecyclerView.Adapter<HitListAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Hit> hitArrayList;
    private final RecyclerViewItemClickListener recyclerViewItemClickListener;

    HitListAdapter(Context context, ArrayList<Hit> hitArrayList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.context = context;
        this.hitArrayList = hitArrayList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_hit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(hitArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return hitArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Hit hit) {
            TextView txtTitle = itemView.findViewById(R.id.txtTitle);
            TextView txtCreated = itemView.findViewById(R.id.txtCreated);
            Switch switchSelection = itemView.findViewById(R.id.switchSelection);

            switchSelection.setOnClickListener(ViewHolder.this);

            txtTitle.setText(hit.getTitle());
            txtCreated.setText(hit.getCreatedAt());
            switchSelection.setChecked(hit.getSelected());
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.switchSelection:
                    recyclerViewItemClickListener.onItemClick(view, getAdapterPosition());
                    break;
            }
        }
    }
}
