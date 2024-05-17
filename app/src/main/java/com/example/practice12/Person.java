package com.example.practice12;


public class Person
{
    protected String firstName; // Имя
    protected String lastName;  // Фамилия
    protected String middleName;// Отчество
    protected String group;     // Группа

    // Конструктор класса для создания объекта Person с заданными значениями
    public Person(String firstName, String lastName, String middleName, String group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.group = group;
    }
}
