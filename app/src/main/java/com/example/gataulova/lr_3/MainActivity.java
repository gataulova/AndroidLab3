package com.example.gataulova.lr_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Database db;
    SQLiteDatabase dbInstance;
    Button button;
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // подключаемся к БД и получаем записываемый инстанс
        db = new Database(this);
        dbInstance = db.getWritableDatabase();

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        dbInstance.delete("example", null, null);

        // получаем курсор таблицы
        Cursor cursor = dbInstance.query("example", null, null, null, null, null, null);

        // проходимся по строкам и добавляем в интерфейс существующие записи
        if (cursor.moveToFirst()) {
            int textIndex = cursor.getColumnIndex("text");

            do {
                String text = cursor.getString(textIndex);

                textView.append("\n" + text);
            } while (cursor.moveToNext());
        }

        // закрываем курсор
        cursor.close();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // берем текст из поля
                String text = editText.getText().toString();

                // создаем объект для заполнения строки в БД
                ContentValues values = new ContentValues();

                values.put("text", text);

                // записываем строку в таблицу example
                dbInstance.insert("example", null, values);

                // добавляем строку в список (в интерфейсе)
                textView.append("\n" + text);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // отключаемся от БД
        db.close();
    }
}
