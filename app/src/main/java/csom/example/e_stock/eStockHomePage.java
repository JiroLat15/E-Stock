package csom.example.e_stock;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import csom.example.e_stock.Product.ProductDatabaseHelper;

public class eStockHomePage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FrameLayout dataEntryLayout;
    private FrameLayout dataViewLayout;
    private FrameLayout dataRemoveLayout;
    private FrameLayout dataUpdateLayout;

    private EditText editTextProductName;
    private EditText editTextProductCode;
    private EditText editTextProductVariation;
    private EditText editTextProductSubVariation;
    private EditText editTextProductQuantity;
    private EditText editTextProductPrice;

    private EditText etUpdName, etUpdProdCode, etUpdProdVar, etUpdProdSubVar, etUpdProdQuant, etUpdProdPrice;

    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textview6;

    private ProductDatabaseHelper databaseHelper;
    private Button btnUpdUpdate;

    private boolean isCreateButtonVisible = false;
    private boolean isViewButtonVisible = false;
    private boolean isRemoveButtonVisible = false;
    private boolean isUpdateButtonVisible = false;
    private Spinner spinnerProductName;

    private Spinner updateSpinner;

    private ScrollView scrollableView;
    private boolean isScrollViewVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estock_home_page);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // for updating the data on the database
        updateSpinner = findViewById(R.id.SPNR_UpdateProdNameAndCode);
        //TextView ET_UpdName = findViewById(R.id.ET_UpdName);              //check line 602
        //TextView ET_UpdProdCode = findViewById(R.id.ET_UpdProdCode);      //check line 603
        etUpdProdVar = findViewById(R.id.ET_UpdProdVar);
        etUpdProdSubVar = findViewById(R.id.ET_UpdProdSubVar);
        etUpdProdQuant = findViewById(R.id.ET_UpdProdQuant);
        etUpdProdPrice = findViewById(R.id.ET_UpdProdPrice);
        btnUpdUpdate = findViewById(R.id.BTN_UpdUpdate);

        scrollableView = findViewById(R.id.scrollableView);
        scrollableView.setVisibility(View.GONE);


        spinnerProductName = findViewById(R.id.SPNR_RemoveProdNameAndCode);

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageButton menuButton = findViewById(R.id.BTN_SidePanelMenu);

        ImageButton buttonCreate = findViewById(R.id.IMGBTN_Create);
        ImageButton buttonRemove = findViewById(R.id.IMGBTN_Remove);
        ImageButton buttonUpdate = findViewById(R.id.IMGBTN_Update);
        ImageButton buttonView = findViewById(R.id.IMGBTN_View);
        ImageButton buttonHelp = findViewById(R.id.IMGBTN_Help);

        dataEntryLayout = findViewById(R.id.FL_AddProduct);
        dataViewLayout = findViewById(R.id.FL_ViewProduct);
        dataRemoveLayout = findViewById(R.id.FL_RemoveProduct);
        dataUpdateLayout = findViewById(R.id.FL_UpdateProduct);


        textView3 = findViewById(R.id.TV_AddStocks);
        textView4 = findViewById(R.id.TV_ViewStocks);
        textView5 = findViewById(R.id.TV_RemStocks);
        textview6 = findViewById(R.id.TV_UpdStocks);

        dataEntryLayout.setVisibility(View.GONE);
        dataViewLayout.setVisibility(View.GONE);
        dataRemoveLayout.setVisibility(View.GONE);
        dataUpdateLayout.setVisibility(View.GONE);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        editTextProductName = findViewById(R.id.ET_ProdAddName);
        editTextProductCode = findViewById(R.id.ET_ProdAddCode);
        editTextProductVariation = findViewById(R.id.ET_ProdAddVar);
        editTextProductSubVariation = findViewById(R.id.ET_ProdSubVar);
        editTextProductQuantity = findViewById(R.id.ET_ProdAddQuant);
        editTextProductPrice = findViewById(R.id.ET_ProdAddPrice);

        Button btnSubmit = findViewById(R.id.BTN_AddSubmit);

        databaseHelper = new ProductDatabaseHelper(this);


        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCreatePopUp();
                // Hide the ScrollView
                scrollableView.setVisibility(View.GONE);
                isScrollViewVisible = false;
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the visibility of the ScrollView
                isScrollViewVisible = !isScrollViewVisible;
                if (isScrollViewVisible) {
                    scrollableView.setVisibility(View.VISIBLE);
                } else {
                    scrollableView.setVisibility(View.GONE);
                }

                toggleViewPopUp();
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRemovePopUp();
                scrollableView.setVisibility(View.GONE);
                isScrollViewVisible = false;
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleUpdatePopUp();
                scrollableView.setVisibility(View.GONE);
                isScrollViewVisible = false;
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, start the HelpActivity
                Intent intent = new Intent(eStockHomePage.this, HelpActivity1.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_downwards, R.anim.slide_out_upwards);
                finish(); //fix this bug -> do -home -help -return repeatedly, then try going back
            }
        });
        NavigationView navigationView = findViewById(R.id.Navi_NaviMenuView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    closeHomePage();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                return true;
            }
        });

        // for Create Stocks
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = editTextProductName.getText().toString().trim();
                String productCode = editTextProductCode.getText().toString().trim();
                String productVariation = editTextProductVariation.getText().toString().trim();
                String productSubVariation = editTextProductSubVariation.getText().toString().trim();
                String productQuantityStr = editTextProductQuantity.getText().toString().trim();
                String productPriceStr = editTextProductPrice.getText().toString().trim();

                if (productName.isEmpty()) {
                    showToast("Input Value for 'Product Name'");
                    return;
                }
                if (productCode.isEmpty()) {
                    showToast("Input Value for 'Product Code'");
                    return;
                }

                int productQuantity;
                double productPrice;

                if (productQuantityStr.isEmpty()) {
                    showToast("Input Value for 'Quantity'");
                    return;
                } else {
                    productQuantity = Integer.parseInt(productQuantityStr);
                }

                if (productPriceStr.isEmpty()) {
                    showToast("Input Value for 'Price'");
                    return;
                } else {
                    productPrice = Double.parseDouble(productPriceStr);
                }

                long result = databaseHelper.insertData(productName, productCode, productVariation,
                        productSubVariation, productQuantity, productPrice);

                if (result != -1) {
                    showToast("Successfully Added: " + productName);
                    // Update the spinner with the latest product names
                    populateProductNamesSpinner();

                } else {
                    showToast("Failed to add product");
                }

                clearEditTextFields();
            }
        });
        // Initialize the remove layout components
        initRemoveLayout();
    }


    // Voids

    private void initRemoveLayout() {    // Populate the spinner with product names                                         //last
        populateProductNamesSpinner();

        // Set click listener for the remove button
        Button btnRemove = findViewById(R.id.BTN_RemSubmit);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic to remove the selected product
                removeSelectedProduct();
            }
        });

        // Add the OnItemSelectedListener to the spinner
        spinnerProductName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Display the details of the selected product
                displaySelectedProductDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void displaySelectedProductDetails() {
        // Retrieve the selected product from the spinner
        String selectedProductNameWithCode = spinnerProductName.getSelectedItem().toString();

        // Extract product name and product code from the selected item
        String[] parts = selectedProductNameWithCode.split(" - ");
        String selectedProductName = parts[0];
        String selectedProductCode = parts[1];

        // Check if a product is selected
        if (selectedProductName.isEmpty() || selectedProductCode.isEmpty()) {
            showToast("Please select a product to display details");
            return;
        }

        // Retrieve the details of the selected product
        ProductDetails productDetails = getProductDetails(selectedProductName, selectedProductCode);

        // Update the TextViews with the details
        if (productDetails != null) {
            updateDetailsTextViews(productDetails);
        } else {
            showToast("Failed to retrieve details for product: " + selectedProductName);
        }
    }

    private ProductDetails getProductDetails(String productName, String productCode) {
        Cursor cursor = databaseHelper.getProductDetails(productName, productCode);

        if (cursor != null) {
            int productCodeIndex = cursor.getColumnIndex("product_code");
            int productVariationIndex = cursor.getColumnIndex("product_variation");
            int productSubVariationIndex = cursor.getColumnIndex("product_sub_variation");
            int productQuantityIndex = cursor.getColumnIndex("quantity");
            int productPriceIndex = cursor.getColumnIndex("price");

            // Check if the columns exist in the cursor
            if (productCodeIndex != -1 && productVariationIndex != -1
                    && productSubVariationIndex != -1 && productQuantityIndex != -1
                    && productPriceIndex != -1) {

                if (cursor.moveToFirst()) {
                    String fetchedProductCode = cursor.getString(productCodeIndex);
                    String productVariation = cursor.getString(productVariationIndex);
                    String productSubVariation = cursor.getString(productSubVariationIndex);
                    int productQuantity = cursor.getInt(productQuantityIndex);
                    double productPrice = cursor.getDouble(productPriceIndex);

                    cursor.close();

                    return new ProductDetails(fetchedProductCode, productVariation, productSubVariation, productQuantity, productPrice);
                }
            } else {
                Log.e("getProductDetails", "One or more columns not found in cursor");
            }

            cursor.close();
        }
        return null;
    }

    private void updateDetailsTextViews(ProductDetails productDetails) {
        TextView tvProductCodeData = findViewById(R.id.TV_RemProdCodeData);
        TextView tvProductVariationData = findViewById(R.id.TV_RemProdVarData);
        TextView tvProductSubVariationData = findViewById(R.id.TV_RemSubVarData);
        TextView tvProductQuantityData = findViewById(R.id.TV_RemProdQuantData);
        TextView tvProductPriceData = findViewById(R.id.TV_RemProdPriceData);

        tvProductCodeData.setText(productDetails.getProductCode());
        tvProductVariationData.setText(productDetails.getProductVariation());
        tvProductSubVariationData.setText(productDetails.getProductSubVariation());
        tvProductQuantityData.setText(String.valueOf(productDetails.getProductQuantity()));
        tvProductPriceData.setText(String.valueOf(productDetails.getProductPrice()));

    }

    private void populateProductNamesSpinner() {
        List<String> productNamesWithCodes = getProductsAndCodesFromDatabase();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNamesWithCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProductName.setAdapter(adapter);
    }

    private List<String> getProductsAndCodesFromDatabase() {
        List<String> productNamesWithCodes = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllData();

        if (cursor != null) {
            int productNameIndex = cursor.getColumnIndex("product_name");
            int productCodeIndex = cursor.getColumnIndex("product_code");

            if (productNameIndex != -1 && productCodeIndex != -1) {
                if (cursor.moveToFirst()) {
                    do {
                        String productName = cursor.getString(productNameIndex);
                        String productCode = cursor.getString(productCodeIndex);
                        String nameWithCode = productName + " - " + productCode;
                        productNamesWithCodes.add(nameWithCode);
                    } while (cursor.moveToNext());
                }
            } else {
                Log.e("getProductsAndCodes", "One or more columns not found in cursor");
            }

            cursor.close();
        }

        return productNamesWithCodes;
    }

    private void removeSelectedProduct() {
        try {
            String selectedItem = spinnerProductName.getSelectedItem() != null ? spinnerProductName.getSelectedItem().toString() : null;

            if (selectedItem == null) {
                showToast("Please select a product to remove");
                return;
            }

            if (isDatabaseEmpty()) {
                showToast("Select a Product to Remove.");
                return;
            }

            String[] parts = selectedItem.split(" - ");

            if (parts.length != 2) {
                showToast("Invalid selected product");
                return;
            }

            String selectedProductName = parts[0];
            String selectedProductCode = parts[1];

            if (selectedProductName.isEmpty() || selectedProductCode.isEmpty()) {
                showToast("Please select a product to remove");
                return;
            }

            long result = databaseHelper.removeProduct(selectedProductName, selectedProductCode);

            if (result != -1) {
                showToast("Product " + selectedProductName + " - " + selectedProductCode + " has been removed");

                populateProductNamesSpinner();

                clearDetailsTextViews();
            } else {
                showToast("Failed to remove product: " + selectedProductName + " - " + selectedProductCode);
            }

            spinnerProductName.setSelection(0);
        } catch (Exception e) {
            showToast("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearDetailsTextViews() {
        TextView tvProductCodeData = findViewById(R.id.TV_RemProdCodeData);
        TextView tvProductVariationData = findViewById(R.id.TV_RemProdVarData);
        TextView tvProductSubVariationData = findViewById(R.id.TV_RemSubVarData);
        TextView tvProductQuantityData = findViewById(R.id.TV_RemProdQuantData);
        TextView tvProductPriceData = findViewById(R.id.TV_RemProdPriceData);

        // Clear the TextViews
        tvProductCodeData.setText("");
        tvProductVariationData.setText("");
        tvProductSubVariationData.setText("");
        tvProductQuantityData.setText("");
        tvProductPriceData.setText("");
    }

    private boolean isDatabaseEmpty() {
        Cursor cursor = databaseHelper.getAllData();
        boolean isEmpty = cursor == null || cursor.getCount() == 0;
        if (cursor != null) {
            cursor.close();
        }
        return isEmpty;
    }

    private void showToast(String message) {
        Toast.makeText(eStockHomePage.this, message, Toast.LENGTH_SHORT).show();
    }

    private void clearEditTextFields() {
        editTextProductName.getText().clear();
        editTextProductCode.getText().clear();
        editTextProductVariation.getText().clear();
        editTextProductSubVariation.getText().clear();
        editTextProductQuantity.getText().clear();
        editTextProductPrice.getText().clear();
    }

    private void toggleCreatePopUp() {
        isCreateButtonVisible = !isCreateButtonVisible;
        // Close any open layout before opening a new one
        textView3.setVisibility(View.GONE); // Hide "Add Stocks" TextView
        textView4.setVisibility(View.GONE); // Hide "View Stocks" TextView
        textView5.setVisibility(View.GONE); // Hide "Remove Stocks" TextView
        textview6.setVisibility(View.GONE);// Hide "Update Stocks" TextView
        dataEntryLayout.setVisibility(View.GONE);
        dataViewLayout.setVisibility(View.GONE);
        dataRemoveLayout.setVisibility(View.GONE); // Hide the remove layout
        dataUpdateLayout.setVisibility(View.GONE);
        dataViewLayout.removeAllViews();

        isViewButtonVisible = false;
        isRemoveButtonVisible = false;
        isUpdateButtonVisible = false;


        if (isCreateButtonVisible) {
            dataEntryLayout.setVisibility(View.VISIBLE);
            textView3.setVisibility(View.VISIBLE);
        }
    }

    private void toggleViewPopUp() {
        isViewButtonVisible = !isViewButtonVisible;
        // Close any open layout before opening a new one
        textView3.setVisibility(View.GONE); // Hide "Add Stocks" TextView
        textView4.setVisibility(View.GONE); // Hide "View Stocks" TextView
        textView5.setVisibility(View.GONE); // Hide "Remove Stocks" TextView
        textview6.setVisibility(View.GONE);// Hide "Update Stocks" TextView
        dataEntryLayout.setVisibility(View.GONE);
        dataViewLayout.setVisibility(View.GONE);
        dataRemoveLayout.setVisibility(View.GONE); // Hide the remove layout
        dataUpdateLayout.setVisibility(View.GONE);
        dataViewLayout.removeAllViews();

        isCreateButtonVisible = false;
        isRemoveButtonVisible = false;
        isUpdateButtonVisible = false;

        if (isViewButtonVisible) {
            textView4.setVisibility(View.VISIBLE);
            dataViewLayout.setVisibility(View.VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(eStockHomePage.this);
            View tableLayout = inflater.inflate(R.layout.table_design_layout, dataViewLayout, false);
            dataViewLayout.addView(tableLayout);
            showDatabaseData();
        }
    }

    private void toggleRemovePopUp() {
        isRemoveButtonVisible = !isRemoveButtonVisible;
        // Close any open layout before opening a new one
        textView3.setVisibility(View.GONE); // Hide "Add Stocks" TextView
        textView4.setVisibility(View.GONE); // Hide "View Stocks" TextView
        textView5.setVisibility(View.GONE); // Hide "Remove Stocks" TextView
        textview6.setVisibility(View.GONE);// Hide "Update Stocks" TextView
        dataEntryLayout.setVisibility(View.GONE);
        dataViewLayout.setVisibility(View.GONE);
        dataRemoveLayout.setVisibility(View.GONE); // Hide the remove layout
        dataUpdateLayout.setVisibility(View.GONE);
        dataViewLayout.removeAllViews();

        isCreateButtonVisible = false;
        isViewButtonVisible = false;
        isUpdateButtonVisible = false;

        if (isRemoveButtonVisible) {
            dataRemoveLayout.setVisibility(View.VISIBLE); // Show the remove layout
            textView5.setVisibility(View.VISIBLE);
        }
    }


    private void toggleUpdatePopUp() {
        isUpdateButtonVisible = !isUpdateButtonVisible;

        // Close the update layout if it is open, otherwise, open it
        // Close any other open layouts
        textView3.setVisibility(View.GONE); // Hide "Add Stocks" TextView
        textView4.setVisibility(View.GONE); // Hide "View Stocks" TextView
        textView5.setVisibility(View.GONE); // Hide "Remove Stocks" TextView
        textview6.setVisibility(View.GONE); // Hide "Update Stocks" TextView
        dataEntryLayout.setVisibility(View.GONE);
        dataViewLayout.setVisibility(View.GONE);
        dataRemoveLayout.setVisibility(View.GONE); // Hide the remove layout
        dataUpdateLayout.setVisibility(View.GONE);
        dataViewLayout.removeAllViews();

        // Reset other visibility flags
        isCreateButtonVisible = false;
        isViewButtonVisible = false;
        isRemoveButtonVisible = false;

        if (isUpdateButtonVisible) {
            dataUpdateLayout.setVisibility(View.VISIBLE);
            textview6.setVisibility(View.VISIBLE);

            // Call the method to set up the update layout components
            setupUpdateLayout();
        }
    }


    private void setupUpdateLayout() {
        // Populate the spinner with product names for updating
        List<String> productNamesWithCodes = getProductsAndCodesFromDatabase();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNamesWithCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateSpinner.setAdapter(adapter);

        // Set click listener for the update button
        btnUpdUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method to handle the update logic
                handleUpdate();
            }
        });

        // You may want to call the method to set up the spinner (if needed)
        setupUpdateSpinner();
    }


    private void setupUpdateSpinner() {
        // Assuming you have already initialized your spinner
        updateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Retrieve the selected product from the spinner
                String selectedProductNameWithCode = updateSpinner.getSelectedItem().toString();

                // Extract product name and product code from the selected item
                String[] parts = selectedProductNameWithCode.split(" - ");
                String selectedProductName = parts[0];
                String selectedProductCode = parts[1];

                // Check if a product is selected
                if (selectedProductName.isEmpty() || selectedProductCode.isEmpty()) {
                    showToast("Please select a product to display details");
                    return;
                }
                TextView ET_UpdName = findViewById(R.id.ET_UpdName);
                TextView ET_UpdProdCode = findViewById(R.id.ET_UpdProdCode);

                // Update the TextViews with the product name and product code
                ET_UpdName.setText(selectedProductName);
                ET_UpdProdCode.setText(selectedProductCode);

                // Retrieve the details of the selected product
                ProductDetails productDetails = getProductDetails(selectedProductName, selectedProductCode);

                // Update the EditText fields with the details
                if (productDetails != null) {
                    etUpdProdVar.setText(productDetails.getProductVariation());
                    etUpdProdSubVar.setText(productDetails.getProductSubVariation());
                    etUpdProdQuant.setText(String.valueOf(productDetails.getProductQuantity()));
                    etUpdProdPrice.setText(String.valueOf(productDetails.getProductPrice()));
                } else {
                    showToast("Failed to retrieve details for product: " + selectedProductName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });
    }


    private void handleUpdate() {
        // Retrieve data from user input
        String updatedProductNameWithCode = updateSpinner.getSelectedItem().toString();
        String[] parts = updatedProductNameWithCode.split(" - ");
        String updatedProductName = parts[0];
        String updatedProductCode = parts[1];
        String updatedProductVariation = etUpdProdVar.getText().toString().trim();
        String updatedProductSubVariation = etUpdProdSubVar.getText().toString().trim();
        String updatedProductQuantityStr = etUpdProdQuant.getText().toString().trim();
        String updatedProductPriceStr = etUpdProdPrice.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(updatedProductQuantityStr) && TextUtils.isEmpty(updatedProductPriceStr)
                && TextUtils.isEmpty(updatedProductVariation) && TextUtils.isEmpty(updatedProductSubVariation)) {
            showToast("At least (1) Detail from the Product should be changed");
            return;
        }

        int updatedProductQuantity;
        double updatedProductPrice;

        if (!TextUtils.isEmpty(updatedProductQuantityStr)) {
            updatedProductQuantity = Integer.parseInt(updatedProductQuantityStr);
        } else {
            updatedProductQuantity = -1; // Use a sentinel value or handle this case accordingly
        }

        if (!TextUtils.isEmpty(updatedProductPriceStr)) {
            updatedProductPrice = Double.parseDouble(updatedProductPriceStr);
        } else {
            updatedProductPrice = -1.0; // Use a sentinel value or handle this case accordingly
        }

        // Call the method to update the product in the database
        boolean updateResult = updateProductInDatabase(updatedProductName, updatedProductCode,
                updatedProductVariation, updatedProductSubVariation, updatedProductQuantity, updatedProductPrice);

        if (updateResult) {
            showToast("Product updated successfully");
            // You might want to update the spinner or refresh the data view after an update
        } else {
            showToast("Failed to update product");
        }
    }



    private boolean updateProductInDatabase(String updatedProductName, String updatedProductCode,
                                            String updatedProductVariation, String updatedProductSubVariation,
                                            int updatedProductQuantity, double updatedProductPrice) {
        long result = databaseHelper.updateProduct(updatedProductName, updatedProductCode,
                updatedProductVariation, updatedProductSubVariation, updatedProductQuantity, updatedProductPrice);

        return result != -1;
    }
    private void showDatabaseData() {
        Cursor cursor = databaseHelper.getAllData();
        LinearLayout tableLayout = dataViewLayout.findViewById(R.id.table_layout);

        View header = LayoutInflater.from(this).inflate(R.layout.table_row_layout, tableLayout, false);
        setRowData(header, "Product Name", "Product Code", "Product Variation", "Product Sub-Var", "Quantity", "Price");
        tableLayout.addView(header);

        if (cursor != null) {
            int productNameIndex = cursor.getColumnIndex("product_name");
            int productCodeIndex = cursor.getColumnIndex("product_code");
            int productVariationIndex = cursor.getColumnIndex("product_variation");
            int productSubVariationIndex = cursor.getColumnIndex("product_sub_variation");
            int productQuantityIndex = cursor.getColumnIndex("quantity");
            int productPriceIndex = cursor.getColumnIndex("price");

            if (productNameIndex != -1 && productCodeIndex != -1 && productVariationIndex != -1
                    && productSubVariationIndex != -1 && productQuantityIndex != -1 && productPriceIndex != -1) {

                if (cursor.moveToFirst()) {
                    do {
                        String productName = cursor.getString(productNameIndex);
                        String productCode = cursor.getString(productCodeIndex);
                        String productVariation = cursor.getString(productVariationIndex);
                        String productSubVariation = cursor.getString(productSubVariationIndex);
                        int productQuantity = cursor.getInt(productQuantityIndex);
                        double productPrice = cursor.getDouble(productPriceIndex);

                        View row = LayoutInflater.from(this).inflate(R.layout.table_row_layout, tableLayout, false);
                        setRowData(row, productName, productCode, productVariation, productSubVariation, String.valueOf(productQuantity), String.valueOf(productPrice));
                        tableLayout.addView(row);
                    } while (cursor.moveToNext());
                }
            } else {
                Log.e("showDatabaseData", "One or more columns not found in cursor");
            }

            cursor.close();
        } else {
            showToast("Failed to retrieve data from the database");
        }
    }

    private void setRowData(View row, String col1, String col2, String col3, String col4, String col5, String col6) {
        TextView tvCol1 = row.findViewById(R.id.tvProductName);
        TextView tvCol2 = row.findViewById(R.id.tvProductCode);
        TextView tvCol3 = row.findViewById(R.id.tvProductVariation);
        TextView tvCol4 = row.findViewById(R.id.tvProductSubVariation);
        TextView tvCol5 = row.findViewById(R.id.tvProductQuantity);
        TextView tvCol6 = row.findViewById(R.id.tvProductPrice);

        tvCol1.setTextAppearance(this, R.style.BoldTextAppearance);
        tvCol2.setTextAppearance(this, R.style.BoldTextAppearance);
        tvCol3.setTextAppearance(this, R.style.BoldTextAppearance);
        tvCol4.setTextAppearance(this, R.style.BoldTextAppearance);
        tvCol5.setTextAppearance(this, R.style.BoldTextAppearance);
        tvCol6.setTextAppearance(this, R.style.BoldTextAppearance);

        tvCol1.setText(col1);
        tvCol2.setText(col2);
        tvCol3.setText(col3);
        tvCol4.setText(col4);
        tvCol5.setText(col5);
        tvCol6.setText(col6);
    }

    private static class ProductDetails {
        private final String productCode;
        private final String productVariation;
        private final String productSubVariation;
        private final int productQuantity;
        private final double productPrice;

        public ProductDetails(String productCode, String productVariation, String productSubVariation, int productQuantity, double productPrice) {
            this.productCode = productCode;
            this.productVariation = productVariation;
            this.productSubVariation = productSubVariation;
            this.productQuantity = productQuantity;
            this.productPrice = productPrice;
        }

        public String getProductCode() {
            return productCode;
        }

        public String getProductVariation() {
            return productVariation;
        }

        public String getProductSubVariation() {
            return productSubVariation;
        }

        public int getProductQuantity() {
            return productQuantity;
        }

        public double getProductPrice() {
            return productPrice;
        }
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeHomePage() {
        Intent intent = new Intent(eStockHomePage.this, eStockFrontPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}