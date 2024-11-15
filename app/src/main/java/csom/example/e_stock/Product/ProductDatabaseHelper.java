package csom.example.e_stock.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

public class ProductDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE products (" +
                    "product_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "product_name TEXT, " +
                    "product_code TEXT NOT NULL, " +
                    "product_variation TEXT NOT NULL, " +
                    "product_sub_variation TEXT, " +
                    "quantity INTEGER NOT NULL, " +
                    "price REAL, " +
                    "UNIQUE(product_code, product_variation) ON CONFLICT REPLACE);";

    private static final String TABLE_NAME = "products";

    public ProductDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }




    // Method to insert data into the database asynchronously
    public void insertDataAsync(String productName, String productCode, String productVariation,
                                String productSubVariation, int quantity, double price, InsertDataCallback callback) {
        new InsertDataTask(callback, this).execute(productName, productCode, productVariation,
                productSubVariation, String.valueOf(quantity), String.valueOf(price));
    }

    // Method to remove a product from the database asynchronously
    public void removeProductAsync(String productName, RemoveProductCallback callback) {
        new RemoveProductTask(callback, this).execute(productName);
    }

    // Callback interface for insert data
    public interface InsertDataCallback {
        void onDataInserted(long result);
    }

    // Callback interface for remove product
    public interface RemoveProductCallback {
        void onProductRemoved(long result);
    }


    // AsyncTask for inserting data
    private static class InsertDataTask extends AsyncTask<String, Void, Long> {
        private final WeakReference<ProductDatabaseHelper> dbHelperRef;
        private final InsertDataCallback callback;

        InsertDataTask(InsertDataCallback callback, ProductDatabaseHelper dbHelper) {
            dbHelperRef = new WeakReference<>(dbHelper);
            this.callback = callback;
        }

        @Override
        protected Long doInBackground(String... params) {
            ProductDatabaseHelper dbHelper = dbHelperRef.get();
            if (dbHelper != null) {
                return dbHelper.insertData(params[0], params[1], params[2], params[3],
                        Integer.parseInt(params[4]), Double.parseDouble(params[5]));
            }
            return -1L;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (callback != null) {
                callback.onDataInserted(result);
            }
        }
    }

    // AsyncTask for removing a product
    private static class RemoveProductTask extends AsyncTask<String, Void, Long> {
        private final WeakReference<ProductDatabaseHelper> dbHelperRef;
        private final RemoveProductCallback callback;

        RemoveProductTask(RemoveProductCallback callback, ProductDatabaseHelper dbHelper) {
            dbHelperRef = new WeakReference<>(dbHelper);
            this.callback = callback;
        }

        @Override
        protected Long doInBackground(String... params) {
            ProductDatabaseHelper dbHelper = dbHelperRef.get();
            if (dbHelper != null) {
                // Assuming params[0] is product name and params[1] is product code
                return dbHelper.removeProduct(params[0], params[1]);
            }
            return -1L;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (callback != null) {
                callback.onProductRemoved(result);
            }
        }
    }

    // Method to insert data into the database
    public long insertData(String productName, String productCode, String productVariation,
                           String productSubVariation, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("product_name", productName);
        values.put("product_code", productCode);
        values.put("product_variation", productVariation);
        values.put("product_sub_variation", productSubVariation);
        values.put("quantity", quantity);
        values.put("price", price);

        // Inserting data into the table
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public long removeProduct(String productName, String productCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "product_name=? AND product_code=?";
        String[] whereArgs = {productName, productCode};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    // Modify getProductDetails to take both product name and product code as parameters
    public Cursor getProductDetails(String productName, String productCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE product_name = ? AND product_code = ?";
        return db.rawQuery(query, new String[]{productName, productCode});
    }



    // Method to update product data in the database asynchronously
    public void updateProductAsync(String productName, String productCode, String newProductVariation,
                                   String newProductSubVariation, int newQuantity, double newPrice,
                                   UpdateProductCallback callback) {
        new UpdateProductTask(callback, this).execute(productName, productCode, newProductVariation,
                newProductSubVariation, String.valueOf(newQuantity), String.valueOf(newPrice));
    }

    // Callback interface for update product
    public interface UpdateProductCallback {
        void onProductUpdated(long result);
    }

    // AsyncTask for updating product data
    // AsyncTask for updating product data
    private static class UpdateProductTask extends AsyncTask<String, Void, Long> {
        private final WeakReference<ProductDatabaseHelper> dbHelperRef;
        private final UpdateProductCallback callback;

        UpdateProductTask(UpdateProductCallback callback, ProductDatabaseHelper dbHelper) {
            dbHelperRef = new WeakReference<>(dbHelper);
            this.callback = callback;
        }


        @Override
        protected Long doInBackground(String... params) {
            Log.d("UpdateProductTask", "Updating product in background...");

            ProductDatabaseHelper dbHelper = dbHelperRef.get();
            if (dbHelper != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put("product_variation", params[2]);
                values.put("product_sub_variation", params[3]);
                values.put("quantity", Integer.parseInt(params[4]));
                values.put("price", Double.parseDouble(params[5]));

                String whereClause = "product_name=? AND product_code=?";
                String[] whereArgs = {params[0], params[1]};

                // Updating data in the table
                return (long) db.update(TABLE_NAME, values, whereClause, whereArgs);
            }
            return -1L;
        }

        @Override
        protected void onPostExecute(Long result) {
            if (callback != null) {
                callback.onProductUpdated(result);
            }
        }
    }

    // Method to update product data in the database
    public long updateProduct(String productName, String productCode, String newProductVariation,
                              String newProductSubVariation, int newQuantity, double newPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("product_variation", newProductVariation);
        values.put("product_sub_variation", newProductSubVariation);
        values.put("quantity", newQuantity);
        values.put("price", newPrice);

        String whereClause = "product_name=? AND product_code=?";
        String[] whereArgs = {productName, productCode};

        // Updating data in the table
        long result = db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        return result;
    }

}