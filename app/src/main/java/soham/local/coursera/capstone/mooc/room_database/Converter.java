package soham.local.coursera.capstone.mooc.room_database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class Converter {

    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,output);
        return output.toByteArray();
    }

    @TypeConverter
    public Bitmap toBitmap(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

}
