package soham.local.coursera.capstone.mooc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.mtp.MtpConstants;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import soham.local.coursera.capstone.mooc.databinding.ActivityMainBinding;
import soham.local.coursera.capstone.mooc.history_activity.HistoryActivity;
import soham.local.coursera.capstone.mooc.room_database.MainDatabase;
import soham.local.coursera.capstone.mooc.room_database.QrHistory;
import soham.local.coursera.capstone.mooc.NetworkChangeReceiver.OnNetworkChangeListener;


public class MainActivity extends AppCompatActivity implements OnNetworkChangeListener {

    private ActivityMainBinding _binding;
    private int _Size = 200;
    private String _Query = null;
    private String _Text = null;
    private Bitmap _Bitmap = null;
    private boolean _networkStatus = false;

    final private NetworkChangeReceiver _networkChangeReceiver = new NetworkChangeReceiver(this);

    private Handler _handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this._binding = ActivityMainBinding.inflate(this.getLayoutInflater());
        setContentView(this._binding.getRoot());
        initViews();
    }

    @Override
    public void onChange(boolean status){
        this._networkStatus = status;
        if(!status){
            _handler.post(()->{
               Toast.makeText(this,"Trying to connect to Internet",Toast.LENGTH_SHORT).show();
            });
        }else {
            _handler.post(()->{
                Toast.makeText(this,"Connected to Internet",Toast.LENGTH_SHORT).show();
            });
            if(this._binding.inputText.getText().toString().length() > 0 )
                generateAndShowQr();
        }
    }

    private void initViews(){
        this._binding.setMainActivity(this);

        this._binding.seekBar.addOnChangeListener((slider,value,fromUser)->{
            this._Size = (int)value;
        });

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(this._networkChangeReceiver, intentFilter);
    }

    public void onInputTextChange(){
        if(this._binding.inputText.getText().toString().length() < 1) {
            this._binding.generateButton.setEnabled(false);
            return;
        }
        if(!this._binding.generateButton.isEnabled())
            this._binding.generateButton.setEnabled(true);
    }

    public void generateAndShowQr(){

        if(this._Size<10) return;
        else if(this._binding.inputText.getText().toString().length() < 1){
            this._binding.inputTextLayout.setError("Cannot be Empty");
            this._Query = null;
            return;
        }

        this._Text = this._binding.inputText.getText().toString();
        this._Query = CONSTANTS.GenerateApi(this._Size,this._Text);
        this._binding.loadingPanel.setVisibility(View.VISIBLE);
        this.showQr();
    }
    
    private void showQr(){
        if(this._Query == null) return;
        Glide.with(this)
                .load(this._Query)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        _binding.loadingPanel.setVisibility(View.GONE);
                        _Bitmap = null;
                        _Text = null;
                        _Query = null;
                        if(!_networkStatus)
                            Toast.makeText(getApplicationContext(),"Not connected to Internet",Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        _binding.loadingPanel.setVisibility(View.GONE);
                        _Bitmap = ((BitmapDrawable)resource).getBitmap();
                        if(!_networkStatus)
                            Toast.makeText(getApplicationContext(),"Not connected to Internet, Showing from cache",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .into(this._binding.qrImageView);
    }

    public void downloadQR(){
        if(this._Query == null ) {
            Toast.makeText(this,"Nothing to download",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!this._networkStatus) {
            Toast.makeText(this, "Not connected to internet", Toast.LENGTH_SHORT).show();
            return;
        }
        QRDownloaderService.startDownload(this,this._Query);
        Toast.makeText(this,"Downloading ...",Toast.LENGTH_SHORT).show();
    }

    public void saveQr(){
        if(this._Bitmap == null ) {
            Toast.makeText(this,"Nothing to save",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Saving ...",Toast.LENGTH_SHORT).show();
        ExecutorUtils.executor.execute(()->{
            MainDatabase db = MainDatabase.getDatabase(this);
            long status = db.getHistoryDAO().addItem(new QrHistory(this._Query,this._Text,this._Bitmap));
            _handler.post(()->{Toast.makeText(this,
                    status < 0 ? "Failed to save." : "Saved.",
                    Toast.LENGTH_SHORT).show();});
        });
    }

    public void goToHistory(){
        Intent i = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        unregisterReceiver(this._networkChangeReceiver);
        super.onDestroy();
    }

}