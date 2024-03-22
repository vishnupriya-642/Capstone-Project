package soham.local.coursera.capstone.mooc.history_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import soham.local.coursera.capstone.mooc.ExecutorUtils;
import soham.local.coursera.capstone.mooc.QRDownloaderService;
import soham.local.coursera.capstone.mooc.R;
import soham.local.coursera.capstone.mooc.databinding.ActivityHistoryBinding;
import soham.local.coursera.capstone.mooc.databinding.HistoryCardViewBinding;
import soham.local.coursera.capstone.mooc.room_database.MainDatabase;
import soham.local.coursera.capstone.mooc.room_database.QrHistory;

/*
This is the History activity which helps to manage all the saved qr that can be accessed by other
apps through ContentProvider - ContentResolver
 */


public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding _binding;
    private HistoryListAdapter _adapter;
    private MainDatabase _db;
    private Handler _handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(this._binding.getRoot());
        initViews();
    }

    /*
    * Called from the onCreateMethod
    * Initialize the ui - adapters, texts, gesture listener, gets Instance of the database
    */
    private void initViews(){

        this._binding.setHistoryActivity(this);


        this._adapter = new HistoryListAdapter();
        this._binding.cardListView.setAdapter(this._adapter);
        this._db = MainDatabase.getDatabase(this);
        ExecutorUtils.executor.execute(()->{
            List<QrHistory> list = this._db.getHistoryDAO().getItems();
            _handler.post(()-> {
                _adapter.submitList(list);
                _adapter.notifyDataSetChanged();
            });
        });
        ItemTouchHelper ith = new ItemTouchHelper(this.getItemTouchHelper());
        ith.attachToRecyclerView(this._binding.cardListView);
    }

    /*
        Deletes all the saved qr(s) in a separate thread
     */
    public void deleteAll(){
        ExecutorUtils.executor.execute(()->{
            this._db.getHistoryDAO().deleteAll();
            List<QrHistory> list = this._db.getHistoryDAO().getItems();
            _handler.post(()-> {
                _adapter.submitList(list);
                _adapter.notifyDataSetChanged();
            });
        });
    }

    /*
    Gesture Listener of the QR History items
     */
    private ItemTouchHelper.SimpleCallback getItemTouchHelper() {
        return new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                    viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (!(viewHolder instanceof HistoryListAdapter.HistoryCardViewHolder))
                    return;
                HistoryListAdapter.HistoryCardViewHolder vh = (HistoryListAdapter.HistoryCardViewHolder) viewHolder;
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        deleteItem(vh.item);
                        break;
                    case ItemTouchHelper.RIGHT:
                        deleteItem(vh.item);
                        break;
                    case ItemTouchHelper.DOWN:
                        download(vh.item);
                        break;
                    default:
                        return;
                }
                _adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }

            private void deleteItem(QrHistory item) {
                ExecutorUtils.executor.execute(() -> {
                    int status = _db.getHistoryDAO().deleteItem(item);
                    List<QrHistory> list = _db.getHistoryDAO().getItems();
                    _handler.post(() -> {
                        _adapter.submitList(list);
                        _adapter.notifyDataSetChanged();
                    });
                });
            }
            private void download(QrHistory item) {
                QRDownloaderService.startDownload(getApplicationContext(),item.url);
            }
        } ;
    }

}