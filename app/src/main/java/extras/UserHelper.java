package extras;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Marcos on 02/02/2015.
 */
public class UserHelper extends SQLiteOpenHelper {
    //Declaracion del nombre de la base de datos
    public static final int DATABASE_VERSION = 1;

    //Declaracion global de la version de la base de datos
    public static final String DATABASE_NAME = "user";

    //Declaracion del nombre de la tabla
    public static final String USER_TABLE = "User";

    //sentencia global de cracion de la base de datos
    public static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE + " (username TEXT UNIQUE, " +
            "mail TEXT UNIQUE, pass TEXT, address TEXT, best INTEGER, avatar TEXT);";

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);

    }

    public void createUser(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(
                USER_TABLE,
                null,
                values);
    }

    public boolean login(String user, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"username", "pass"};
        String[] where = {user};
        Cursor mCursor = db.query(
                USER_TABLE,
                columns,
                "username=?",
                where,
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()) {
            String s = mCursor.getString(mCursor.getColumnIndex("pass"));
            if (s.equals(pass)) return true;
            else return false;
        } else {
            return false;
        }
    }

    public boolean checkMail(String mail) {
        //true: the username exist
        //false: the username doesnt exist
        SQLiteDatabase db = this.getReadableDatabase();
        String[] mails = {"mail"};
        String[] where = {mail};
        Cursor mCursor = db.query(
                USER_TABLE,
                mails,
                "mail=?",
                where,
                null,
                null,
                null
        );

        if (mCursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkUser(String user) {
        //true: the username exist
        //false: the username doesnt exist
        SQLiteDatabase db = this.getReadableDatabase();
        String[] names = {"username"};
        String[] where = {user};
        Cursor mCursor = db.query(
                USER_TABLE,
                names,
                "username=?",
                where,
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getUser(String user){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] names = {"username","mail","pass","address","best","avatar"};
        String[] where = {user};
        Cursor mCursor = db.query(
                USER_TABLE, //qué tabla?
                names,      //que cosas
                "username=?", //check
                where,      //coincida
                null,
                null,
                null
        );
        if (mCursor.moveToFirst()) {
            return mCursor;
        } else {
            return null;
        }
    }

    public int updateAddress(String user, String address){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {user};
        ContentValues c = new ContentValues();
        c.put("address", address);
        return db.update(USER_TABLE,c,"username=?",where);
    }

    public void updateRecord(String user, int intentos){
        //Código que he intentado usar para actualizar según intentos < de lo que haya
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put("best",intentos);
        String[] where = {String.valueOf(intentos),user};
        return db.update(USER_TABLE,c, "best >? AND username=?",where);
        */
        SQLiteDatabase db = this.getWritableDatabase();
        //return db.update(USER_TABLE,c, "best >? AND username=?",where);
        db.execSQL("UPDATE " + USER_TABLE + " SET best = " + intentos + " WHERE username = 'user' AND best > " + intentos + ";");
    }


    public int updateImage(String user, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] where = {user};
        ContentValues update = new ContentValues();
        update.put("avatar",path);
        return db.update(USER_TABLE,update,"username=?",where);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}

