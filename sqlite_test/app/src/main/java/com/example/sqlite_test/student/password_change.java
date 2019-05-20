package com.example.sqlite_test.student;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;
import com.example.sqlite_test.start_load.load;

/***
 * 用于学生/老师修改密码
 */

public class password_change extends AppCompatActivity {
    SQLiteDatabase db;
    RadioGroup load_radiogroup;
    EditText edit_account;
    EditText edit_oldpassword ;
    EditText edit_newpassword ;
    EditText edit_confirm ;
    String true_password ="";
    String state = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        Button button_change = findViewById(R.id.button_change_password_ok);

        edit_account = findViewById(R.id.et_c_account);
        edit_oldpassword = findViewById(R.id.et_c_oldpassword);
        edit_newpassword = findViewById(R.id.et_c_newpassword);
        edit_confirm = findViewById(R.id.et_c_confirm_newpassword);
        load_radiogroup= findViewById(R.id.password_radiogroup);

        db = new CommonDatabase().getSqliteObject(password_change.this,"test_db");

        load_radiogroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (group.getCheckedRadioButtonId())
                        {
                            case R.id.radiobutton_xuesheng_p:
                                state="student";


                                break;
                            case R.id.radiobutton_teacher_p:
                                state = "teacher";
                                break;
                        }


                    }
                });

        button_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state.equals("student"))
                    {
                        check("load_account");

                    }
                    else if(state .equals("teacher"))
                    {
                        check("load_teacher");
                    }
                    else
                    {
                        Toast.makeText(password_change.this,"您还没有说明身份！",Toast.LENGTH_SHORT).show();
                    }



                //如果有任何一项为空

            }


        });



    }
    public  void  check(String string)
    {
        if(edit_account.getText().toString().equals("")||edit_confirm.getText().toString().equals("")||edit_newpassword.getText().toString().equals("")||edit_oldpassword.getText().toString().equals(""))
        {
            Toast.makeText(password_change.this,"不能为空！",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //去找真正密码
            Cursor cursor =db.query(string,null,"account = ?",new String[]{edit_account.getText().toString()},null,null,null);
            //如果根本没这个账户
            if(cursor.getCount()==0)
            {
                Toast.makeText(password_change.this,"没有找到该账户",Toast.LENGTH_SHORT).show();
            }
            else
            {
                while (cursor.moveToNext())
                {
                    true_password = cursor.getString(cursor.getColumnIndex("password"));
                }
                if(!edit_oldpassword.getText().toString().equals(true_password))
                {
                    Toast.makeText(password_change.this,"原密码错误！",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //如果用户前后输入密码不同
                    if(!edit_newpassword.getText().toString().equals(edit_confirm .getText().toString()))
                    {
                        Toast.makeText(password_change.this,"前后两次输入与验证密码错误！",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ContentValues values = new ContentValues();
                        values.put("password",edit_newpassword.getText().toString());
                        //更新数据库
                        db.update("load_account", values, "account = ? ", new String[]{edit_account.getText().toString()});
                        Toast.makeText(password_change.this,"修改成功！",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(password_change.this, load.class));
                    }
                }

            }



        }
    }
}
