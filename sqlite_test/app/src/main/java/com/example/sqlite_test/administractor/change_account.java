package com.example.sqlite_test.administractor;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;

/**
 * 管理员用于修改学生登录账户信息
 */
public class change_account extends AppCompatActivity {
    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account);

        final EditText edit_account = findViewById(R.id.edit_account);
        final EditText edit_password = findViewById(R.id.edit_account_password);
        Button button_account_change = findViewById(R.id.button_account_change);
        Button button_account_back = findViewById(R.id.button_account_back);
        //获取进来这个活动的intent
        final Intent intent = getIntent();

        edit_account.setText(intent.getStringExtra("account"));
        edit_password.setText(intent.getStringExtra("password"));

        //初始化数据库对象
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(change_account.this,"test_db");

        View.OnClickListener listener = new View.OnClickListener(

        ){
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.button_account_change:
                        ContentValues values = new ContentValues();
                        values.put("account",edit_account.getText().toString());
                        values.put("password",edit_password.getText().toString());

                        db.update("load_account", values, "account = ? ", new String[]{intent.getStringExtra("account")});
                        break;
                    case R.id.button_account_back:
                        finish();
                        break;
                }
            }
        };

        button_account_back.setOnClickListener(listener);
        button_account_change.setOnClickListener(listener);


    }
}
