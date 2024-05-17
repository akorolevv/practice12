package com.example.practice12;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        TextView textView = findViewById(R.id.text_view);

        // URI для доступа к данным контактов в Content Provider
        Uri contentUri = Uri.parse("content://com.example.mycontentproviderapp.provider/contacts");

        // Запрос данных из Content Provider
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

        // Проверка наличия данных в курсоре
        if (cursor != null && cursor.moveToFirst())
        {
            StringBuilder data = new StringBuilder(); // Строка для хранения данных
            do {
                // Получение индексов столбцов
                int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
                int phoneIndex = cursor.getColumnIndex(DBHelper.COLUMN_PHONE);
                int emailIndex = cursor.getColumnIndex(DBHelper.COLUMN_EMAIL);

                // Извлечение данных из курсора
                int id = cursor.getInt(idIndex);
                String phone = cursor.getString(phoneIndex);
                String email = cursor.getString(emailIndex);

                // Формирование строки с данными контакта
                data.append("ID: ").append(id).append(", Phone: ").append(phone).append(", Email: ").append(email).append("\n");
            } while (cursor.moveToNext()); // Переход к следующей записи в курсоре

            textView.setText(data.toString()); // Отображение данных в TextView
        } else {
            textView.setText("Нет доступных контактов"); // Если данных нет
        }
    }
}
