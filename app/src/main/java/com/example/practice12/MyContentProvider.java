package com.example.practice12;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider
{

    // URI authority и базовый URI для Content Provider
    public static final String AUTHORITY = "com.example.mycontentproviderapp.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/contacts");

    // Коды для UriMatcher (для разных операций)
    private static final int CONTACTS = 1;  // Код для всех контактов
    private static final int CONTACT_ID = 2; // Код для конкретного контакта по ID

    // UriMatcher для сопоставления URI с кодами операций
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "contacts", CONTACTS);       // Сопоставление URI для всех контактов
        sUriMatcher.addURI(AUTHORITY, "contacts/#", CONTACT_ID);   // Сопоставление URI для конкретного контакта
    }

    private DBHelper dbHelper; // Объект для работы с базой данных

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext()); // Создание объекта DBHelper
        return true;
    }


    // Метод для извлечения данных из базы данных
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Получение базы данных для чтения
        Cursor cursor;                                     // Курсор для хранения результата запроса

        int match = sUriMatcher.match(uri); // Сопоставление URI с кодом операции
        switch (match) {
            case CONTACTS:
                // Запрос всех контактов
                cursor = db.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACT_ID:
                // Запрос конкретного контакта по ID
                selection = DBHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(DBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri); // Неизвестный URI
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri); // Установка уведомления об изменениях
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получение базы данных для записи
        long id = db.insert(DBHelper.TABLE_NAME, null, values); // Вставка новой записи
        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id); // Создание URI для новой записи
            getContext().getContentResolver().notifyChange(newUri, null); // Уведомление об изменении
            return newUri;
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri); // Ошибка вставки
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем базу данных для записи
        int match = sUriMatcher.match(uri);                 // Определяем тип операции по URI
        int rowsUpdated;                                    // Переменная для хранения количества обновленных строк

        switch (match)
        {
            case CONTACTS:
                // Обновление нескольких контактов (всех или по условию)
                rowsUpdated = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CONTACT_ID:
                // Обновление конкретного контакта по ID
                selection = DBHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri); // Неизвестный URI
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null); // Уведомляем об изменении данных
        }
        return rowsUpdated; // Возвращаем количество обновленных строк
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Получаем базу данных для записи
        int match = sUriMatcher.match(uri);                 // Определяем тип операции по URI
        int rowsDeleted;                                    // Переменная для хранения количества удаленных строк

        switch (match) {
            case CONTACTS:
                // Удаление нескольких контактов (всех или по условию)
                rowsDeleted = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACT_ID:
                // Удаление конкретного контакта по ID
                selection = DBHelper.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri); // Неизвестный URI
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null); // Уведомляем об изменении данных
        }
        return rowsDeleted; // Возвращаем количество удаленных строк
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }
}
