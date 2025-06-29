package com.lollipop.countdown.calculator.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors

abstract class DatabaseHelper(
    private val context: Context,
    dbName: String,
    dbVersion: Int
) : SQLiteOpenHelper(
    context, dbName, null, dbVersion
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val tables = getTableList()
        for (table in tables) {
            db?.execSQL(table.createSql)
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        val tables = getTableList()
        for (table in tables) {
            for (sql in table.updateSql(oldVersion, newVersion)) {
                db?.execSQL(sql)
            }
        }
    }

    abstract fun getTableList(): List<Table<*>>

    interface Table<T> {

        val name: String

        val createSql: String

        fun updateSql(oldVersion: Int, newVersion: Int): List<String>

        fun createReader(db: SQLiteDatabase): Reader<T>

        fun createWriter(db: SQLiteDatabase): Writer<T>

    }

    abstract class Controller {

        protected val dbExecutor by lazy { DBExecutor() }

        protected fun doAsync(error: (Throwable) -> Unit = {}, block: () -> Unit) {
            dbExecutor.onAsync(error, block)
        }

        protected fun onUI(error: (Throwable) -> Unit = {}, block: () -> Unit) {
            dbExecutor.onMain(error, block)
        }

    }

    abstract class Reader<T> : Controller() {

        abstract fun read(pageIndex: Int, pageSize: Int, callback: (List<T>) -> Unit)

        abstract fun readAll(callback: (List<T>) -> Unit)

        fun Cursor.autoClose(block: (Cursor) -> Unit) {
            val cursor = this
            try {
                block(cursor)
            } catch (e: Throwable) {
                Log.e("Database.Reader", "Cursor auto close error", e)
            } finally {
                try {
                    if (!cursor.isClosed) {
                        cursor.close()
                    }
                } catch (ignore: Throwable) {

                }
            }
        }

        fun <T : Any> Cursor.getCursorData(map: (Cursor) -> T?): List<T> {
            val cursor = this
            val result = mutableListOf<T>()
            while (cursor.moveToNext()) {
                try {
                    map(cursor)?.let {
                        result.add(it)
                    }
                } catch (e: Throwable) {
                    Log.e("Database", "getCursorData", e)
                }
            }
            return result
        }

        fun Cursor.optString(columnName: String, def: String = ""): String {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getString(columnIndex) ?: def
        }

        fun Cursor.optLong(columnName: String, def: Long = 0L): Long {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getLong(columnIndex)
        }

        fun Cursor.optInt(columnName: String, def: Int = 0): Int {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getInt(columnIndex)
        }

        fun Cursor.optBoolean(columnName: String, def: Boolean = false): Boolean {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getInt(columnIndex) != 0
        }

        fun Cursor.optFloat(columnName: String, def: Float = 0f): Float {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getFloat(columnIndex)
        }

        fun Cursor.optDouble(columnName: String, def: Double = 0.0): Double {
            val cursor = this
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex < 0) {
                return def
            }
            return cursor.getDouble(columnIndex)
        }

    }

    abstract class Writer<T> : Controller() {

        abstract fun insert(data: T)

        abstract fun delete(data: T)

        abstract fun update(data: T)

        fun values(vararg values: Pair<String, Any?>): ContentValues {
            val contentValues = ContentValues()
            for (pair in values) {
                val any = pair.second
                when (any) {
                    is String -> contentValues.put(pair.first, any)
                    is Int -> contentValues.put(pair.first, any)
                    is Long -> contentValues.put(pair.first, any)
                    is Float -> contentValues.put(pair.first, any)
                    is Double -> contentValues.put(pair.first, any)
                    is Boolean -> contentValues.put(
                        pair.first, if (any) {
                            1
                        } else {
                            0
                        }
                    )
                }
            }
            return contentValues
        }

    }

    class DBExecutor() {

        private val asyncExecutor by lazy { Executors.newSingleThreadExecutor() }
        private val mainExecutor by lazy { Handler(Looper.getMainLooper()) }

        fun onAsync(error: (Throwable) -> Unit = {}, block: () -> Unit) {
            asyncExecutor.execute {
                try {
                    block()
                } catch (e: Exception) {
                    Log.e("DBExecutor", "DB read error", e)
                    error(e)
                }
            }
        }

        fun onMain(error: (Throwable) -> Unit = {}, block: () -> Unit) {
            mainExecutor.post {
                try {
                    block()
                } catch (e: Exception) {
                    Log.e("DBExecutor", "DB read error", e)
                    error(e)
                }
            }
        }

    }

}