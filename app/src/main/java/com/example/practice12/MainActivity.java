package com.example.practice12;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText editTextId, editTextPhone, editTextEmail;
    private TextView textViewContacts;
    private Button buttonSave, buttonDelete, buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.edit_text_id);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextEmail = findViewById(R.id.edit_text_email);
        textViewContacts = findViewById(R.id.text_view_contacts);

        buttonSave = findViewById(R.id.button_save);
        buttonDelete = findViewById(R.id.button_delete);
        buttonUpdate = findViewById(R.id.button_update);

        buttonSave.setOnClickListener(v -> saveContact());
        buttonDelete.setOnClickListener(v -> deleteContact());
        buttonUpdate.setOnClickListener(v -> updateContact());

    }

    private void saveContact()
    {
        String id = editTextId.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, id);
        values.put(DBHelper.COLUMN_PHONE, phone);
        values.put(DBHelper.COLUMN_EMAIL, email);

        Uri newUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        if (newUri != null) {
            Toast.makeText(this, "Контакт сохранен", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteContact() {
        String id = editTextId.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Введите ID для удаления", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, id);
        int rowsDeleted = getContentResolver().delete(uri, null, null);

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Контакт удален", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Контакт не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateContact() {
        String id = editTextId.getText().toString();
        if (id.isEmpty()) {
            Toast.makeText(this, "Введите ID для обновления", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PHONE, phone);
        values.put(DBHelper.COLUMN_EMAIL, email);

        Uri uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, id);
        int rowsUpdated = getContentResolver().update(uri, values, null, null);

        if (rowsUpdated > 0)
        {
            Toast.makeText(this, "Контакт обновлен", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Контакт не найден", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        editTextId.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");
    }
}
