package soham.local.coursera.capstone.mooc.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.room.Room;
import androidx.room.RoomSQLiteQuery;

import java.util.List;

import soham.local.coursera.capstone.mooc.CONSTANTS;
import soham.local.coursera.capstone.mooc.room_database.HistoryDAO;
import soham.local.coursera.capstone.mooc.room_database.MainDatabase;
import soham.local.coursera.capstone.mooc.room_database.QrHistory;

/*
    Provides read only access to other app to get all the saved qr(s).
 */

public class SavedQrProvider extends ContentProvider {

    private MainDatabase myDatabase;

    @Override
    public boolean onCreate() {
        myDatabase = MainDatabase.getDatabase(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        List<QrHistory> data = myDatabase.getHistoryDAO().getItems();
        MatrixCursor cursor = new MatrixCursor(new String[] {"text","qr"});
        for(QrHistory item : data){
            cursor.addRow(new Object[]{item.text, item.qr});
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        // Implement getType method if needed
        return null;
    }

    // Override insert, update, and delete methods to return appropriate responses
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Insert operation not supported");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Update operation not supported");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete operation not supported");
    }

}
