package com.sultonbek1547.registration.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.model.User

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION),
    MyDbInterface {
    companion object {
        const val DB_NAME = "db_name"
        const val DB_VERSION = 1
        const val ID = "id"
        const val USER_TABLE = "user_table"
        const val USER_FULL_NAME = "user_name"
        const val USER_NUMBER = "user_number"
        const val USER_ADDRESS = "user_address"
        const val USER_WHICH_COUNTRY = "user_country"
        const val USER_PASSWORD = "user_password"
        const val USER_IMAGE = "user_image"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val contactQuery =
            "create table $USER_TABLE(ID integer not null primary key autoincrement unique, $USER_FULL_NAME text not null, $USER_NUMBER text not null, $USER_WHICH_COUNTRY text not null, $USER_ADDRESS text not null, $USER_PASSWORD text not null, $USER_IMAGE text not null)"
        p0?.execSQL(contactQuery)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
    override fun addUser(user: User) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_FULL_NAME, user.fullName)
        contentValues.put(USER_NUMBER, user.number)
        contentValues.put(USER_ADDRESS, user.address)
        contentValues.put(USER_WHICH_COUNTRY, user.whichCountry)
        contentValues.put(USER_PASSWORD, user.password)
        contentValues.put(USER_IMAGE, user.image)
        database.insert(USER_TABLE, null, contentValues)
        database.close()

    }

    override fun getAllUser(): ArrayList<User> {
        val database = this.writableDatabase
        val cursor = database.rawQuery("select * from $USER_TABLE", null)
        val list = ArrayList<User>()
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    User(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    override fun deleteUser(user: User) {
        val database = this.writableDatabase
        database.delete(USER_TABLE, "id=?", arrayOf(user.id.toString()))
        database.close()
        database.close()
    }

    override fun editUser(user: User): Int {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_FULL_NAME, user.fullName)
        contentValues.put(USER_NUMBER, user.number)
        contentValues.put(USER_ADDRESS, user.address)
        contentValues.put(USER_WHICH_COUNTRY, user.whichCountry)
        contentValues.put(USER_PASSWORD, user.password)
        contentValues.put(USER_IMAGE, user.image)

        return database.update(
            USER_TABLE,
            contentValues,
            "$ID=?",
            arrayOf(user.id.toString())
        )
    }
}


interface MyDbInterface {
    fun addUser(user: User)
    fun getAllUser(): ArrayList<User>
    fun deleteUser(user: User)
    fun editUser(user: User): Int

}