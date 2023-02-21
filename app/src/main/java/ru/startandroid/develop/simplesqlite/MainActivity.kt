package ru.startandroid.develop.simplesqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

const val LOG_TAG:String = "myLogs"

class MainActivity : AppCompatActivity(), OnClickListener {

    private var btnAdd:Button? = null
    private var btnRead:Button? = null
    private var btnClear:Button? = null

    private var etName:EditText? = null
    private var etEmail:EditText? = null

    private var dbHelper:SQLiteOpenHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById<View>(R.id.btnAdd) as Button
        btnAdd?.setOnClickListener(this)

        btnRead = findViewById<View>(R.id.btnRead) as Button
        btnRead?.setOnClickListener(this)

        btnClear = findViewById<View>(R.id.btnClear) as Button
        btnClear?.setOnClickListener(this)

        etName = findViewById<View>(R.id.etName) as EditText
        etEmail = findViewById<View>(R.id.etEmail) as EditText

        dbHelper = DBHelper(this)
    }

    override fun onClick(v: View?) {
        val cv: ContentValues = ContentValues()

        val name:String = etName!!.text.toString()
        val email:String = etEmail!!.text.toString()

        val db:SQLiteDatabase = dbHelper!!.writableDatabase

        when(v?.id) {
            R.id.btnAdd -> {
                Log.d(LOG_TAG, "---Insert in mytable: ---")

                cv.put("name", name)
                cv.put("email", email)

                val rowID = db.insert("mytable", null, cv)
                Log.d(LOG_TAG, "row inserted, ID = $rowID")
            }
            R.id.btnRead -> {
                Log.d(LOG_TAG, "---Rows in mytable: ---")

                val c = db.query("mytable", null, null, null, null, null, null)

                if (c.moveToFirst()) {
                    val idColIndex:Int = c.getColumnIndex("id")
                    val nameColIndex:Int = c.getColumnIndex("name")
                    val emailColIndex:Int = c.getColumnIndex("email")

                    do {
                        Log.d(LOG_TAG,
                        "ID = ${c.getInt(idColIndex)}" +
                                ", name = ${c.getString(nameColIndex)}" +
                                ", email = ${c.getString(emailColIndex)}")
                    } while (c.moveToNext())
                } else {
                    Log.d(LOG_TAG, "0 rows")
                    c.close()
                }
            }
            R.id.btnClear -> {
                Log.d(LOG_TAG, "---Clear mytable: ---")

                val clearCount:Int = db.delete("mytable", null, null)

                Log.d(LOG_TAG, "deleted rows count = $clearCount")
            }
        }
        dbHelper!!.close()
    }

    internal inner class DBHelper(context: Context?) :
        SQLiteOpenHelper (context, "myDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
        Log.d(LOG_TAG, "--- onCreate database ---")
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVerison: Int, newVersion: Int) {

        }
    }
}