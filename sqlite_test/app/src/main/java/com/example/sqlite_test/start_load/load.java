package com.example.sqlite_test.start_load;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.administractor.Container;
import com.example.sqlite_test.administractor.MainActivity;
import com.example.sqlite_test.database.CommonDatabase;
import com.example.sqlite_test.student.activity_student;
import com.example.sqlite_test.student.password_change;
import com.example.sqlite_test.teacher.activity_teacher;

/***
 * 登陆界面的配置
 */
public class load extends AppCompatActivity {
    String account = "WYH";
    String password ="wuyuehang";
    //标记是选择了学生还是管理员
    String state = "";
    SQLiteDatabase db ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        //textview


        //账号密码编辑框
        final EditText edit_load_zhanghao = findViewById(R.id.edit_load_zhanghao);
        final EditText edit_load_password = findViewById(R.id.edit_load_password);
        CommonDatabase commonDatabase = new CommonDatabase();

        db =commonDatabase.getSqliteObject(load.this,"test_db");

        final Button button_change_password = findViewById(R.id.button_change_password);

        RadioGroup load_radiogroup = findViewById(R.id.load_radiogroup);

        final ImageButton imageButton = findViewById(R.id.imagebutton);

        //眼睛睁开闭上
        edit_load_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//初始为隐藏密文状态
        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) //按下重新设置背景图片

                {

                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.visible));
                    edit_load_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示


                }

                else if(event.getAction() == MotionEvent.ACTION_UP) //松手恢复原来图片

                {

                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.invisible));
                    edit_load_password.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏

                }

                return false;

            }


        });


        //单选按钮监听器
        load_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId())
                {
                    case R.id.radiobutton_xuesheng:

                    edit_load_zhanghao.setHint("请输入您的学号");
                        button_change_password.setVisibility(View.VISIBLE);

                        state = "student";

                        break;
                    case R.id.radiobutton_guanliyuan:

                        edit_load_zhanghao.setHint("请输入您的账号");



                        state = "guanliyuan";
                        break;
                    case R.id.radiobutton_teacher:

                        edit_load_zhanghao.setHint("请输入您的账号");


                        state = "teacher";
                        break;
                     default:
                         break;

                }
            }
        });

        //登录按钮
        Button button_load = findViewById(R.id.button_load);
        button_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取学生输入的账号密码
                String account_load = edit_load_zhanghao.getText().toString();
                String password_load = edit_load_password.getText().toString();
                if(state=="guanliyuan")
                {

                    if(edit_load_zhanghao.getText().toString().equals(account)&&edit_load_password.getText().toString().equals(password))
                    {
                        startActivity(new Intent(load.this, Container.class));
                        Toast.makeText(load.this,"登陆成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(load.this,"密码错误！",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(state.equals("student"))
                {



                    //去数据库中找账号为account的数据

                    String true_password="";

                    Cursor cursor = db.query("load_account", null, "account = ?", new String[]{account_load}, null, null, null);

                    while(cursor.moveToNext())
                    {
                         true_password =cursor.getString(cursor.getColumnIndex("password"));
                    }

                    if(password_load.equals(true_password)&&true_password.equals("")==false)
                    {
                        Intent intent = new Intent(load.this,activity_student.class);
                        intent.putExtra("student_id",account_load);
                        startActivity(intent);
                        Toast.makeText(load.this,"登陆成功！",Toast.LENGTH_SHORT).show();

                        finish();

                    }
                    else
                    {
                        Toast.makeText(load.this,"密码错误！",Toast.LENGTH_SHORT).show();
                    }



                }
                else if(state.equals("teacher"))
                {
                    //去数据库中找账号为account的数据



                    Cursor cursor = db.query("load_teacher", null, "account = ? AND password = ?", new String[]{account_load,password_load}, null, null, null);



                    if(cursor.getCount()==0)
                    {

                        Toast.makeText(load.this,"密码错误！",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Intent intent = new Intent(load.this, activity_teacher.class);
                        intent.putExtra("teacher_id",edit_load_zhanghao.getText().toString());
                        startActivity(intent);
                        Toast.makeText(load.this,"登陆成功！",Toast.LENGTH_SHORT).show();

                        finish();
                    }


                }
                else
                {
                    Toast.makeText(load.this,"您还没有选择任何登录类型！",Toast.LENGTH_SHORT).show();
                }



            }
        });
        //点击修改按钮进入更改密码的界面

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(load.this, password_change.class);
                startActivity(intent);



            }
        });


    }

}
