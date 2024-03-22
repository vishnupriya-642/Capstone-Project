package soham.local.coursera.capstone.mooc.history_activity;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView.*;

import java.util.ArrayList;
import java.util.List;

import soham.local.coursera.capstone.mooc.ExecutorUtils;
import soham.local.coursera.capstone.mooc.databinding.HistoryCardViewBinding;
import soham.local.coursera.capstone.mooc.room_database.QrHistory;
/*
    List adapter class for the recycler view containing the card view of the saved qr(s)
 */
public class HistoryListAdapter extends ListAdapter<QrHistory,HistoryListAdapter.HistoryCardViewHolder> {


    public HistoryListAdapter() {
        super(new HistoryListDiffUtil());
    }

    @NonNull
    @Override
    public HistoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HistoryCardViewHolder(
                HistoryCardViewBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryCardViewHolder holder, int position) {
        Log.d("testing",this.getItem(position).toString());
        holder.onBind(this.getItem(position));
    }

    private static class HistoryListDiffUtil extends DiffUtil.ItemCallback<QrHistory>{

        @Override
        public boolean areItemsTheSame(@NonNull QrHistory oldItem, @NonNull QrHistory newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull QrHistory oldItem, @NonNull QrHistory newItem) {
            return oldItem.equals(newItem);
        }
    }

    protected class HistoryCardViewHolder extends ViewHolder{

        public HistoryCardViewBinding _binding;
        public QrHistory item;

        public HistoryCardViewHolder(@NonNull HistoryCardViewBinding binding) {
            super(binding.getRoot());
            this._binding = binding;
        }

        public void onBind(QrHistory item){
            this.item = item;
            this._binding.qrImageView.setImageBitmap(item.qr);
            this._binding.qrTextView.setText(item.text);
        }
    }

}
