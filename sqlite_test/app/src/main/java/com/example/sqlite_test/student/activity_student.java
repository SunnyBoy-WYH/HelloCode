package com.example.sqlite_test.student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_student extends AppCompatActivity {
    SQLiteDatabase db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        //获取从登录界面赶来的intent
         final Intent intent_1 = getIntent();
         //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_student.this,"test_db");
        //绑定组件
        final  ListView listView_mycourse = findViewById(R.id.listview_mycourse);
        Button button_choose_course = findViewById(R.id.choose_course);
        Button button_look_my_information  = findViewById(R.id.button_look);
        Button button_about_me = findViewById(R.id.button_aboutme);
        Button button_liuyan = findViewById(R.id.button_liuyan);
        //为listview设定监听器
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.choose_course:
                        //再将从登陆界面接受的学生学号传给选择课程的活动
                        Intent intent_2 = new Intent(activity_student.this,choose_course.class);
                        intent_2.putExtra("student_id",intent_1.getStringExtra("student_id"));
                        startActivity(intent_2);
                        break;
                        //查询该用户的选课结果
                    case R.id.button_look:
//                      //两表连接查询
                        Cursor cursor =db.rawQuery(
                                "select * from student_course inner join course " +
                                        "on student_course.course_name =course.course_name " +
                                        "AND student_course.teacher_name = course.teacher_name  " +
                                        "where student_id = ?",new String[]{intent_1.getStringExtra("student_id")});
                        ArrayList<Map<String,String>> arrayList_1= new ArrayList<Map<String,String>>();
                        if(cursor.getCount()==0)
                        {
                            Toast.makeText(activity_student.this,"您还没有选择任何课！",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            while(cursor.moveToNext())
                            {
                                Map <String,String> map= new HashMap<String,String>();

                                map.put("course_time",cursor.getString(cursor.getColumnIndex("course_time")));
                                map.put("course_name",cursor.getString(cursor.getColumnIndex("course_name")));
                                map.put("teacher_name",cursor.getString(cursor.getColumnIndex("teacher_name")));
                                map.put("course_period",cursor.getString(cursor.getColumnIndex("course_period")));


                                arrayList_1.add(map);

                            }
                            //设置适配器，并绑定布局文件
                            SimpleAdapter simpleAdapter=new SimpleAdapter(activity_student.this,arrayList_1,R.layout.choose_result,
                                    new String[]{"course_name","teacher_name","course_time","course_period"},new int[]{R.id.result_course_name,R.id.result_teacher_name,R.id.result_time,R.id.result_period});
                            listView_mycourse.setAdapter(simpleAdapter);
                        }
                        break;
                        //关于我的button
                    case R.id.button_aboutme:
                        Intent intent_about = new Intent(activity_student.this,about_me.class);
                        intent_about.putExtra("student_id",intent_1.getStringExtra("student_id"));
                        startActivity(intent_about);
                        break;
                     //提交留言
                    case R.id.button_liuyan:
                        Intent intent_submit = new Intent(activity_student.this,submit_message.class);
                        intent_submit.putExtra("student_id",intent_1.getStringExtra("student_id"));
                        startActivity(intent_submit);



                }
            }
        };

        button_choose_course.setOnClickListener(listener);
        button_look_my_information.setOnClickListener(listener);
        button_about_me.setOnClickListener(listener);
        button_liuyan.setOnClickListener(listener);
    }
}
