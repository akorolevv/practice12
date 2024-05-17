package com.example.practice12;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSON_2 extends AppCompatActivity
{
    private TextView textJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json2);

        textJsonData = findViewById(R.id.text_json_data);

        try
        {
            String fileName = "person_data.json"; // Имя файла для чтения
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName); // Открытие файла

            // Проверка существования файла
            if (!file.exists())
            {
                Toast.makeText(this, "JSON файл не найден", Toast.LENGTH_SHORT).show();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file)); // Создание объекта для чтения из файла
            StringBuilder jsonBuilder = new StringBuilder(); // Создание объекта для построения строки из JSON
            String line;
            while ((line = reader.readLine()) != null)
            { // Чтение файла построчно
                jsonBuilder.append(line); // Добавление строки в StringBuilder
            }
            reader.close(); // Закрытие файла

            String json = jsonBuilder.toString(); // Получение JSON строки из StringBuilder

            // Преобразование JSON строки в объект Person
            Gson gson = new Gson();
            Person person = gson.fromJson(json, Person.class);

            // Формирование строки с данными пользователя
            String data = "Имя: " + person.firstName + "\n" +
                    "Фамилия: " + person.lastName + "\n" +
                    "Отчество: " + person.middleName + "\n" +
                    "Группа: " + person.group;

            textJsonData.setText(data); // Отображение данных в TextView
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка чтения JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
