package com.app.arcadeaproject.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "arcadea.db"
        private const val DATABASE_VERSION = 2 // Incremented version to trigger onUpgrade

        // Tables
        const val TABLE_WISHLIST = "wishlist"
        const val TABLE_CART = "cart"

        // Columns
        const val COLUMN_ID = "id" // Game ID
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_PRICE = "price"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createWishlistTable = ("CREATE TABLE " + TABLE_WISHLIST + "("
                + COLUMN_ID + " INTEGER,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_USER_ID + ")" + ")")

        val createCartTable = ("CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_PRICE + " TEXT,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + "PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_USER_ID + ")" + ")")

        db.execSQL(createWishlistTable)
        db.execSQL(createCartTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WISHLIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    fun addToWishlist(userId: Int, id: Int, title: String, price: String, image: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_USER_ID, userId)
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_PRICE, price)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_DESCRIPTION, description)
        val result = db.insertWithOnConflict(TABLE_WISHLIST, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return result
    }

    fun addToCart(userId: Int, id: Int, title: String, price: String, image: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_USER_ID, userId)
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_PRICE, price)
        values.put(COLUMN_IMAGE, image)
        values.put(COLUMN_DESCRIPTION, description)
        val result = db.insertWithOnConflict(TABLE_CART, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
        return result
    }

    fun getAllWishlist(userId: Int): List<LocalGame> {
        val list = mutableListOf<LocalGame>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_WISHLIST WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(LocalGame(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun getAllCart(userId: Int): List<LocalGame> {
        val list = mutableListOf<LocalGame>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CART WHERE $COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(LocalGame(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun removeFromWishlist(userId: Int, id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_WISHLIST, "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?", arrayOf(id.toString(), userId.toString()))
        db.close()
        return result
    }

    fun removeFromCart(userId: Int, id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_ID = ? AND $COLUMN_USER_ID = ?", arrayOf(id.toString(), userId.toString()))
        db.close()
        return result
    }

    fun clearCart(userId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CART, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result
    }
}
