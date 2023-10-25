package evereast.co.doctor.DrugDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void setMedicinesModel(MedicinesModel MedicinesModel);

    @Query("SELECT * FROM MedicinesModel")
    List<MedicinesModel> GetMedicines();

}
