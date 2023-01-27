package com.example.printerapp.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.printerapp.constants.DbValues;
import com.example.printerapp.entities.Order;
import com.example.printerapp.entities.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbManager extends SQLiteOpenHelper {
    private ArrayList<Order> orders;
    private ArrayList<Resource> resources;
    private static DbManager dbManager;

    private DbManager(Context context) {
        super(context, DbValues.DB_NAME.getStringValue(), null,
                Integer.parseInt(DbValues.DB_SCHEMA.getStringValue()));

        orders = new ArrayList<>();
        resources = new ArrayList<>();
    }

    public static synchronized DbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DbManager(context);
        }
        dbManager.fillRepositories();

        return dbManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<DbValues> requests = Arrays.asList(
                DbValues.CREATE_RESOURCE_TYPES_TABLE,
                DbValues.CREATE_ORDERS_TABLE,
                DbValues.CREATE_WASTES_TABLE,
                DbValues.INSERT_RESOURCE_TYPES
        );
        for (DbValues request : requests) {
            sqLiteDatabase.execSQL(request.getStringValue());
        }
    }

    private void fillRepositories() {
        orders.clear();
        resources.clear();

        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();

            Cursor cursor = sqLiteDatabase
                    .rawQuery(DbValues.GET_ORDERS.getStringValue(), null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int orderIdColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_ID.getStringValue());
                    int orderNameColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_NAME.getStringValue());
                    int orderAmountColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_AMOUNT.getStringValue());
                    int orderStartDateColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_START_DATE.getStringValue());
                    int orderEndDateColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_END_DATE.getStringValue());
                    int orderCustomerNameColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_CUSTOMER_NAME.getStringValue());
                    int orderCustomerPhoneColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_CUSTOMER_PHONE.getStringValue());
                    int orderSizeColumnIndex = cursor.getColumnIndex(DbValues
                            .ORDER_SIZE.getStringValue());
                    int orderResourceIdColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_ID.getStringValue());
                    int orderResourceNameColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_NAME.getStringValue());
                    int orderResourcePriceColumnIndex = cursor.getColumnIndex(DbValues
                            .RESOURCE_PRICE.getStringValue());

                    do {
                       orders.add(new Order(
                               cursor.getInt(orderIdColumnIndex),
                               cursor.getString(orderNameColumnIndex),
                               cursor.getInt(orderAmountColumnIndex),
                               cursor.getLong(orderStartDateColumnIndex),
                               cursor.getLong(orderEndDateColumnIndex),
                               cursor.getString(orderCustomerNameColumnIndex),
                               cursor.getString(orderCustomerPhoneColumnIndex),
                               cursor.getDouble(orderSizeColumnIndex),
                               new Resource(
                                       cursor.getInt(orderResourceIdColumnIndex),
                                       cursor.getString(orderResourceNameColumnIndex),
                                       cursor.getDouble(orderResourcePriceColumnIndex)
                               )
                       ));
                    } while (cursor.moveToNext());
                }
            }

            cursor = sqLiteDatabase.rawQuery(DbValues
                    .GET_RESOURCES.getStringValue(), null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int resourceIdColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_ID.getStringValue());
                        int resourceNameColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_NAME.getStringValue());
                        int resourcePriceColumnIndex = cursor.getColumnIndex(DbValues
                                .RESOURCE_PRICE.getStringValue());

                        resources.add(new Resource(
                                cursor.getInt(resourceIdColumnIndex),
                                cursor.getString(resourceNameColumnIndex),
                                cursor.getDouble(resourcePriceColumnIndex)
                        ));

                    } while (cursor.moveToNext());
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        }
        catch (SQLException ignored) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion == oldVersion + 1) {
            oldVersion = newVersion;
            List<DbValues> deleteRequests = Arrays.asList(
                    DbValues.DROP_ORDERS_TABLE,
                    DbValues.DROP_RESOURCE_TYPES_TABLE,
                    DbValues.DROP_WASTES_TABLE
            );
            for (DbValues deleteRequest : deleteRequests) {
                sqLiteDatabase.execSQL(deleteRequest.getStringValue());
            }

            onCreate(sqLiteDatabase);
        }
    }

    public String[] getCustomers() {
        return orders.stream()
                .map(Order::getCustomerName)
                .toArray(String[]::new);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void addOrder(Order order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(DbValues.INSERT_ORDER.getStringValue(), new Object[]{
                order.getName(),
                order.getAmount(),
                order.getStartDate(),
                order.getEndDate(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getSize(),
                order.getResource().getKey()
        });
    }
}
