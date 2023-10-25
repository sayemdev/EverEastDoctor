package evereast.co.doctor.PrescriptionDB;

import android.content.Context;

import androidx.room.Room;

public class AlarmDatabaseClient {

    Context context;
    private static AlarmDatabaseClient mInstance;
    AlarmDatabase alarmDatabase;

    public AlarmDatabaseClient(Context context){
        this.context=context;
        alarmDatabase = Room.databaseBuilder(context, AlarmDatabase.class,"Alarms")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized AlarmDatabaseClient getInstance(Context context){
        if (mInstance==null){
            mInstance=new AlarmDatabaseClient(context);
        }
        return mInstance;
    }

    public AlarmDatabase getCartDatabase() {
        return alarmDatabase;
    }
}
