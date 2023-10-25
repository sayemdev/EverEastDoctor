package evereast.co.doctor.DrugDB;

import android.content.Context;

import androidx.room.Room;

public class CartDatabaseClient {
    private static CartDatabaseClient mInstance;
    Context context;
    CartDatabase cartDatabase;

    public CartDatabaseClient(Context context) {
        this.context =context;
        cartDatabase =(Room.databaseBuilder(context, CartDatabase.class, "CartDatabase")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build());
    }

    public static synchronized CartDatabaseClient getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new CartDatabaseClient(context);
        }
        return mInstance;
    }

    public CartDatabase getCartDatabase() {
        return cartDatabase;
    }
}
