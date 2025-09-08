package com.example.stockcontrol.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.stockcontrol.interfaces.ProductDao
import com.example.stockcontrol.model.Items
@Database(entities = [Items::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE new_products (
                        id INTEGER PRIMARY KEY NOT NULL, 
                        description TEXT NOT NULL, 
                        price REAL NOT NULL, 
                        stock INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    INSERT INTO new_products (id, description, price, stock)
                    SELECT CAST(id AS INTEGER), description, price, stock FROM products
                    """.trimIndent()
                )

                database.execSQL("DROP TABLE products")
                database.execSQL("ALTER TABLE new_products RENAME TO products")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "products_db"  // <-- aquí colocamos el nombre correcto
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
