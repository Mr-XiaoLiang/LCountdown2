package com.lollipop.countdown.calculator.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.lollipop.countdown.calculator.Formula

class FormulaDatabase(
    private val context: Context
) : DatabaseHelper(
    context, DB_NAME, DB_VERSION
) {

    companion object {
        const val DB_NAME = "formula.db"
        const val DB_VERSION = 1
    }

    override fun getTableList(): List<Table<*>> {
        return listOf(History)
    }

    object History : Table<Formula> {

        const val COLUMN_ID = "id"
        const val COLUMN_FORMULA = "formula"
        const val COLUMN_RESULT = "result"

        val ALL_COLUMN by lazy {
            arrayOf(
                COLUMN_ID,
                COLUMN_FORMULA,
                COLUMN_RESULT
            ).joinToString(", ")
        }

        override val name: String = "history"
        override val createSql: String = """
            CREATE TABLE IF NOT EXISTS $name (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FORMULA TEXT NOT NULL,
                $COLUMN_RESULT TEXT NOT NULL
            )
        """.trimIndent()

        override fun updateSql(
            oldVersion: Int,
            newVersion: Int
        ): List<String> {
            return emptyList()
        }

        override fun createReader(db: SQLiteDatabase): Reader<Formula> {
            return HistoryReader(db)
        }

        override fun createWriter(db: SQLiteDatabase): Writer<Formula> {
            return HistoryWriter(db)
        }
    }

    private class HistoryReader(
        private val db: SQLiteDatabase
    ) : Reader<Formula>() {

        override fun read(
            pageIndex: Int,
            pageSize: Int,
            callback: (List<Formula>) -> Unit
        ) {
            doAsync(error = { onUI { callback(emptyList()) } }) {
                query(limit = "$pageSize OFFSET ${pageSize * pageIndex}").readFormulaList(callback)
            }
        }

        override fun readAll(callback: (List<Formula>) -> Unit) {
            doAsync(error = { onUI { callback(emptyList()) } }) {
                query().readFormulaList(callback)
            }
        }

        private fun Cursor.readFormulaList(callback: (List<Formula>) -> Unit) {
            autoClose { cursor ->
                val result = cursor.getCursorData { c ->
                    Formula.parse(
                        formatOption = c.optString(History.COLUMN_FORMULA),
                        formatResult = c.optString(History.COLUMN_RESULT)
                    ).apply {
                        dbId = c.optLong(History.COLUMN_ID)
                    }
                }
                onUI {
                    callback(result)
                }
            }
        }

        private fun query(
            isDesc: Boolean = true,
            limit: String = ""
        ): Cursor {
            val orderBy = if (isDesc) {
                "${History.COLUMN_ID} DESC"
            } else {
                "${History.COLUMN_ID} ASC"
            }
            return db.query(
                History.name,
                arrayOf(
                    History.COLUMN_ID,
                    History.COLUMN_FORMULA,
                    History.COLUMN_RESULT
                ),
                null,
                null,
                null,
                null,
                orderBy,
                limit
            )
        }

    }

    private class HistoryWriter(
        private val db: SQLiteDatabase
    ) : Writer<Formula>() {
        override fun insert(data: Formula) {
            doAsync {
                db.insert(
                    History.name,
                    null,
                    values(
                        History.COLUMN_FORMULA to data.formatOption(),
                        History.COLUMN_RESULT to data.formatResult()
                    )
                )
            }
        }

        override fun delete(data: Formula) {
            doAsync {
                db.delete(
                    History.name,
                    "${History.COLUMN_ID}=?",
                    arrayOf(data.dbId.toString())
                )
            }
        }

        override fun update(data: Formula) {
            if (data.dbId < 0) {
                insert(data)
                return
            }
            doAsync {
                db.update(
                    History.name,
                    values(
                        History.COLUMN_FORMULA to data.formatOption(),
                        History.COLUMN_RESULT to data.formatResult()
                    ),
                    "${History.COLUMN_ID}=?",
                    arrayOf(data.dbId.toString())
                )
            }
        }
    }

}