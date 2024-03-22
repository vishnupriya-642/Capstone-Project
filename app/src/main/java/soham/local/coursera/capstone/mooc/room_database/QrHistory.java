package soham.local.coursera.capstone.mooc.room_database;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import soham.local.coursera.capstone.mooc.CONSTANTS;

@Entity(tableName = CONSTANTS.TABLE_QR_HISTORY)
public class QrHistory {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "url")
    @NonNull
    public String url;


    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "qr")
    public Bitmap qr;

    public QrHistory(String url,String text,Bitmap qr){
        this.url = url;
        this.text = text;
        this.qr = qr;
    }

    public boolean equals(QrHistory obj){
        return this.url.equals(obj.url) &&
                this.text.equals(obj.text) ;
    }

    @NonNull
    @Override
    public String toString() {
        return this.url+"\t"+this.text+"\n";
    }
}
