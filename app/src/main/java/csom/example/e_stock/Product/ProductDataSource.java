package csom.example.e_stock.Product;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ProductDataSource {

    private SQLiteDatabase database;
    private ProductDatabaseHelper dbHelper;

    public ProductDataSource(Context context) {
        dbHelper = new ProductDatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
