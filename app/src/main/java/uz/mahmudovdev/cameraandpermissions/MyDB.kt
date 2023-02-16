package uz.mahmudovdev.cameraandpermissions

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDB(context: Context) : SQLiteOpenHelper(context, "users.db", null, 1) {
    val tableName = "userImage"
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table $tableName(id integer primary key, name text, image blob)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    fun insertData(name: String?, img: ByteArray): Long {
        val database: SQLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("name", name)
        contentValues.put("image", img)
        return database.insert(tableName, null, contentValues)
    }
}