package com.example.printerapp.constants;

public enum DbValues implements EnumDecoder {
    DB_NAME("printerapp.db"),
    DB_SCHEMA("1"),
    ORDER_ID("order_id"),
    ORDER_NAME("order_name"),
    ORDER_AMOUNT("order_amount"),
    ORDER_START_DATE("order_start_date"),
    ORDER_END_DATE("order_end_date"),
    ORDER_CUSTOMER_NAME("order_customer_name"),
    ORDER_CUSTOMER_PHONE("order_customer_phone"),
    ORDER_SIZE("order_size"),
    RESOURCE_ID("resource_id"),
    RESOURCE_NAME("resource_name"),
    RESOURCE_PRICE("resource_price"),
    WASTE_ID("waste_id"),
    WASTE_MONTH("waste_month"),
    WASTE_YEAR("waste_year"),
    ELECTRICITY_AMOUNT("electricity_amount"),
    RESOURCE_AMOUNT("resource_amount"),
    DROP_ORDERS_TABLE("DROP TABLE IF EXISTS orders;"),
    DROP_WASTES_TABLE("DROP TABLE IF EXISTS wastes;"),
    DROP_RESOURCE_TYPES_TABLE("DROP TABLE IF EXISTS resource_types;"),
    CREATE_RESOURCE_TYPES_TABLE("CREATE TABLE IF NOT EXISTS resource_types(" +
            "resource_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "resource_name VARCHAR(30) NOT NULL," +
            "resource_price REAL NOT NULL);"),
    CREATE_WASTES_TABLE("CREATE TABLE IF NOT EXISTS wastes(" +
            "waste_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "waste_month INTEGER NOT NULL," +
            "waste_year INTEGER NOT NULL," +
            "electricity_amount REAL NOT NULL," +
            "resource_amount REAL NOT NULL);"),
    CREATE_ORDERS_TABLE("CREATE TABLE IF NOT EXISTS orders(" +
            "order_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "order_name VARCHAR(50) NOT NULL," +
            "order_amount INTEGER NOT NULL," +
            "order_start_date LONG NOT NULL," +
            "order_end_date LONG NOT NULL," +
            "order_customer_name VARCHAR(50) NOT NULL," +
            "order_customer_phone VARCHAR(20) NOT NULL," +
            "order_size REAL NOT NULL," +
            "resource_type INTEGER," +
            "FOREIGN KEY (resource_type) REFERENCES resource_types(resource_id));"),
    INSERT_ORDER("INSERT INTO orders(order_name, order_amount, order_start_date, " +
            "order_end_date, order_customer_name, order_customer_phone, order_size, " +
            "resource_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?);"),
    UPDATE_ORDER("UPDATE orders SET order_name = ?, order_amount = ?, " +
            "order_start_date = ?, order_end_date = ?, order_customer_name = ?, " +
            "order_customer_phone = ?, order_size = ?, resource_type = ? WHERE order_id = ?;"),
    INSERT_RESOURCE_TYPES("INSERT INTO resource_types(resource_name, resource_price) " +
            "VALUES ('PLA', 8.5), ('ABS', 10.0), ('HIPS', 9.5), ('PETG', 11.8), ('SBS', 15.5)," +
            " ('Фотополимер 16мкм', 65.5), ('Фотополимер 32мкм', 82.5), ('Полиамид', 34.8);"),
    GET_ORDERS("SELECT order_id, order_name, order_amount, order_start_date, " +
            "order_end_date, order_customer_name, order_customer_phone, order_size, resource_id, " +
            "resource_name, resource_price FROM orders " +
            "JOIN resource_types ON resource_id = resource_type;"),
    DELETE_ORDER_BY_ID("DELETE FROM orders WHERE order_id = ?;"),
    GET_RESOURCES("SELECT * FROM resource_types;");

    private String stringValue;

    DbValues(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
