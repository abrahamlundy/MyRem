package a1407229.lundy.com.myrem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DbSensor {

    //Kasus ini ada class di dalam class init untuk get-set
    public static class DataSensor{
        public String nama;
        public String telepon;
    }

    //Private private
    private SQLiteDatabase db;
    private  final OpenHelper dbHelper;

    //public custom variabel bernama DbMahasiswa dengan mengklon Context utk dbHelper
    //Ini sebagai Constructor
    public DbSensor(Context c){
        //dengan ini maka database akan dikelola
        dbHelper = new OpenHelper(c);
    }

    //method open
    public void open(){
        //database db adalah database yang diolah oleh dbHelper saat ini
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    // method custom variabel
    public long insertData (String nama, String noTelp){
        //Instansiasi content value
        ContentValues newValues = new ContentValues();

        newValues.put("NAMA", nama);
        newValues.put("NAMA", noTelp);
        return  db.insert("DATA", null, newValues);
    }

    //variabel methd dengan mengisi nilai sendiri
    public DataSensor getDataSensor (String nama){
        Cursor cur = null;
        DataSensor M = new DataSensor();

        //Kolom yang diambil
        String[] cols = new String[]{"ID","NAMA","TELEPON"};
        String [] param = {nama};

        cur = db.query("DATA", cols,"NAMA=",param,null,null,null);

        if (cur.getCount()>0){
            cur.moveToFirst();
            M.nama = cur.getString(1);
            M.telepon = cur.getString(2);
        }

        cur.close();
        return M;
    }

    //ambil semua data mahasiswa (dibatasi 10)
//menggunakan raw query
    public ArrayList<DataSensor> getAllMahasiswa() {
        Cursor cur = null;
        ArrayList<DataSensor> out = new ArrayList<>();
        cur = db.rawQuery("SELECT nama,telepon FROM DATA Limit 10", null);
        if (cur.moveToFirst()) {
            do {
                DataSensor sens = new DataSensor();
                sens.nama = cur.getString(0);
                sens.telepon = cur.getString(1);
                out.add(sens);
            } while (cur.moveToNext());
        }
        cur.close();
        return out;
    }

}
