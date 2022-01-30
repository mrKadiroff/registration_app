package com.example.registration.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.registration.models.RegisterModel
import com.example.registration.utils.Constant

class MyDbHelper(context: Context):
    SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.DB_VERSION), DatabaseService {
    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table ${Constant.TABLE_NAME} (${Constant.ID} integer not null primary key autoincrement unique, ${Constant.IMAGE} text not null, ${Constant.NAME} text not null, ${Constant.PHONE} text not null, ${Constant.COUNTRY} text not null, ${Constant.ADRESS} text not null, ${Constant.PASSWORD} text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists ${Constant.TABLE_NAME}")
        onCreate(db)
    }

    override fun insertRegister(registerModel: RegisterModel) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.IMAGE, registerModel.rasm)
        contentValues.put(Constant.NAME, registerModel.ism)
        contentValues.put(Constant.PHONE, registerModel.telefon)
        contentValues.put(Constant.COUNTRY, registerModel.mamlakat)
        contentValues.put(Constant.ADRESS, registerModel.manzil)
        contentValues.put(Constant.PASSWORD, registerModel.parol)
        database.insert(Constant.TABLE_NAME, null, contentValues)
        database.close()
    }

    override fun getAllCamera(): ArrayList<RegisterModel> {
        val list = ArrayList<RegisterModel>()
        val query = "select * from ${Constant.TABLE_NAME}"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val rasm = cursor.getString(1)
                val ism = cursor.getString(2)
                val telefon = cursor.getString(3)
                val mamlakat = cursor.getString(4)
                val manzil = cursor.getString(5)
                val parol = cursor.getString(6)
                val camera = RegisterModel(id, rasm, ism, telefon, mamlakat, manzil, parol)
                list.add(camera)
            } while (cursor.moveToNext())
        }
        return list
    }

}