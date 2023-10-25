package evereast.co.doctor.PrescriptionDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class IxEntityModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int ix;
    @ColumnInfo(name = "investigation")
    public String investigation;

    public IxEntityModel() {
    }

    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }

    public String getInvestigation() {
        return investigation;
    }

    public void setInvestigation(String investigation) {
        this.investigation = investigation;
    }
}
