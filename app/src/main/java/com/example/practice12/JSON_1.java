package com.example.practice12;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSON_1 extends AppCompatActivity
{

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editMiddleName;
    private EditText editGroup;
    private TextView textJsonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json1);

        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editMiddleName = findViewById(R.id.edit_middle_name);
        editGroup = findViewById(R.id.edit_group);
        textJsonResult = findViewById(R.id.text_json_result);
        Button btnConvertToJson = findViewById(R.id.btn_convert_to_json);

        btnConvertToJson.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                convertToJson(); // Вызов метода для преобразования данных в JSON
            }
        });
    }

    private void convertToJson()
    {
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String middleName = editMiddleName.getText().toString();
        String group = editGroup.getText().toString();

        // Создание объекта Person с данными пользователя
        Person person = new Person(firstName, lastName, middleName, group);

        // Преобразование объекта Person в JSON строку с помощью Gson
        Gson gson = new Gson();
        String json = gson.toJson(person);

        textJsonResult.setText(json); // Отображение JSON строки в TextView

        try
        {
            String fileName = "person_data.json"; // Имя файла для сохранения
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName); // Создание объекта файла
            FileWriter writer = new FileWriter(file); // Создание объекта для записи в файл
            writer.write(json); // Запись JSON строки в файл
            writer.flush(); // Очистка буфера записи
            writer.close(); // Закрытие файла
            Toast.makeText(this, "JSON сохранен в файл: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения JSON", Toast.LENGTH_SHORT).show();
        }
    }
}
