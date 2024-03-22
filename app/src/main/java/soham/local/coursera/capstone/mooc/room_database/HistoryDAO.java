package soham.local.coursera.capstone.mooc.room_database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import soham.local.coursera.capstone.mooc.CONSTANTS;

@Dao
public interface HistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long addItem(QrHistory item);

    @Query("select * from "+ CONSTANTS.TABLE_QR_HISTORY)
    public List<QrHistory> getItems();

    @Query("delete from "+CONSTANTS.TABLE_QR_HISTORY)
    public int deleteAll();

    @Delete
    public int deleteItem(QrHistory item);
}
