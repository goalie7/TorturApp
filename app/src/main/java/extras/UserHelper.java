package extras;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
            Log.i("LOGIN", "EXISTE EL USUARIO Y SU PASS ES : " + s + " y LA INTRODUCIDA ES :" + pass);
            if (s.equals(pass)) return true;
            else return false;
        } else {
            //Log.i("userbd", "No Existe user:  " + where[0]);
            return false;
            /* record not exist */
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
//            Log.i("userbd", "Existe mail " + where + " CURSOR: " + mCursor.getString(mCursor.getColumnIndex("username")));
//            Log.i("count", "COUNT mail: " + mCursor.getCount()+ "CONTENIDO : " + mCursor.getString(mCursor.getColumnIndex("mail")));
            return true;
            /* record exist */
        } else {
            Log.i("userbd", "No existe mail " + mail);
            return false;
            /* record not exist */
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
            /* record exist */
        } else {
//            Log.i("userbd", "No Existe user:  " + where[0]);
            return false;
            /* record not exist */
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}

