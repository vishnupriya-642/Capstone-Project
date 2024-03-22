package soham.local.coursera.capstone.mooc.room_database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import soham.local.coursera.capstone.mooc.CONSTANTS;

@Database(entities = {QrHistory.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class MainDatabase extends RoomDatabase {

    public abstract HistoryDAO getHistoryDAO();

    private static volatile MainDatabase INSTANCE;

    public static MainDatabase getDatabase(Context context){
        if(INSTANCE!=null)
            return INSTANCE;
        INSTANCE = Room.databaseBuilder(
                       context.getApplicationContext(),
                        MainDatabase.class,
                        CONSTANTS.DATABASE_NAME
                       ).build();
        return INSTANCE;
    }

}
