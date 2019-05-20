package com.example.sqlite_test.administractor;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;
import com.example.sqlite_test.student.submit_message;

/***
 * 管理员用于增加学生信息
 */
public class add extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //实现add_activity中的按钮绑定
        Button button_add_add = findViewById(R.id.button_add_add);

        Button button_finish_add = findViewById(R.id.button_finish_add);

        //为这两个按钮实现Click监听器
        button_finish_add.setOnClickListener(this);

        button_add_add.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        CommonDatabase commonDatabase = new CommonDatabase();
        SQLiteDatabase db =commonDatabase.getSqliteObject(add.this,"test_db");

        //获取用户输入的学号
        EditText edit_xuehao = findViewById(R.id.edit_xuehao);

        String xuehao = edit_xuehao.getText().toString();
        //获取用户输入的姓名
        EditText edit_xingming = findViewById(R.id.edit_xingming);

        String xingming = edit_xingming.getText().toString();

        //获取用户输入的性别
        EditText edit_sex = findViewById(R.id.edit_xingbie);

        String sex = edit_sex.getText().toString();

        //获取用户输入的年龄
        EditText edit_age = findViewById(R.id.edit_age);

        String age = edit_age.getText().toString();
        //获取用户输入的班级
        EditText edit_banji = findViewById(R.id.edit_banji);

        String banji = edit_banji.getText().toString();

        //获取用户输入的电话
        EditText edit_phone = findViewById(R.id.edit_phone);

        String phone = edit_phone.getText().toString();

        //输入用户输入的学院
        EditText edit_college = findViewById(R.id.edit_college);

        String college = edit_college.getText().toString();
//        if(xuehao.equals(""))
//        {
//            Toast.makeText(add.this,"学号不可以为空！",Toast.LENGTH_SHORT).show();
//        }
//        else
        {

            switch (v.getId())
            {
                case R.id.button_add_add:

                    ContentValues values = new ContentValues();
                    values.put("name",xingming);
                    values.put("id",xuehao);
                    values.put("banji",banji);
                    values.put("sex",sex);
                    values.put("age",age);
                    values.put("phone",phone);
                    values.put("college",college);
                    db.insert("student", null, values);
//                ContentValues values1 = new ContentValues();
                    ////////////////////////////////
//                values1.put("account",xuehao);
//                values1.put("password",xuehao);
//                db.insert("load_account",null,values1);

                    finish();
                    break;
                case R.id.button_finish_add:
                    finish();
                    break;
                default:
                    break;
            }
        }



    }
}
