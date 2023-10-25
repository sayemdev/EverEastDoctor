package evereast.co.doctor.PrescriptionDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DBDao {

    @Insert
    void AddToIXList(IxEntityModel cart);

    @Delete
    void delete(IxEntityModel ixEntityModel);

    @Query("DELETE FROM IxEntityModel")
    void DeleteAllCart();


    @Query("SELECT * FROM IxEntityModel")
    List<IxEntityModel> OfflineIXList();
/*
    @Query("SELECT * FROM IxEntityModel ORDER BY alarmDate DESC")
    List<IxEntityModel> AddressListFull();

    @Query("SELECT * FROM IxEntityModel WHERE dateTime=:dateTime ORDER BY alarmDate DESC")
    List<IxEntityModel> AlarmByDateTime(String dateTime);

    @Query("UPDATE IxEntityModel SET alarmStatus = :status WHERE dateTime = :dateTime")
    void UpdateAlarm(int status, String dateTime);

    @Query("UPDATE IxEntityModel SET alarmStatus = :status,amount=:amount,afterMeal=:meal,dateTime=:newDateTime,medicineName=:medicineName,reminderTime=:time WHERE dateTime = :dateTime")
    void UpdateWholeAlarm(String dateTime, String amount, boolean meal, String medicineName, String newDateTime,String time,int status);
*/
}
