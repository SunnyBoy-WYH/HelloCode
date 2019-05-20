package com.example.sqlite_test.student;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class choose_course extends AppCompatActivity {
    SQLiteDatabase db;
    List<item> arrayList;
    courseAdapter c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);
        /////////////////////////////
        CommonDatabase commonDatabase = new CommonDatabase();

        db =commonDatabase.getSqliteObject(choose_course.this,"test_db");
        /////////////////////////////
        final ListView listView_course = findViewById(R.id.listview_course);
        Button button_back = findViewById(R.id.back);
        Button button_choose =findViewById(R.id.choose);
        final Intent intent_3 = getIntent();
        View.OnClickListener listener_choose =new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId())
                {
                    case R.id.choose:
                        String string_chongtu = "";
                        for(int i=0;i<c.getCount();i++)
                        {
                            item it = (item)c.getItem(i);
                            if(it.ischeck==true)
                            {

                                //去表中查一下是否已经存在
                                Cursor cursor = db.query("student_course",null,"student_id =? AND course_name =? AND teacher_name = ?",
                                        new String[]{intent_3.getStringExtra("student_id"),it.getCourse_name(),it.getTeacher_name()},null,null,null);
                                if(cursor.getCount()==0)
                                {
                                    ContentValues values1 = new ContentValues();
                                    ////////////////////////////////

                                    values1.put("student_id",intent_3.getStringExtra("student_id"));
                                    values1.put("course_name",it.getCourse_name());
                                    values1.put("teacher_name",it.getTeacher_name());
                                    db.insert("student_course",null,values1);
                                }
                                else
                                {
                                    string_chongtu+=it.getCourse_name();
                                    string_chongtu+="/";
                                }

                                //Toast.makeText(choose_course.this,it.getCourse_name()+"被选中",Toast.LENGTH_SHORT).show();
                            }
                            //如果没有冲突的
                            if(string_chongtu.equals(""))
                            {
                                Toast.makeText(choose_course.this,"选课成功！",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(choose_course.this,string_chongtu+"重复选中，其他课程选课成功！",Toast.LENGTH_SHORT).show();
                            }


                        }

                        break;

                    case R.id.back:
                        finish();
                        break;
                }
            }
        };
        button_choose.setOnClickListener(listener_choose);
        button_back.setOnClickListener(listener_choose);
        listView_course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });




        Cursor cursor = db.query("course", null, null, null, null, null, null);
        arrayList= new ArrayList<item>();
        //对游标进行遍历
        while(cursor.moveToNext())
        {


            String  name = cursor.getString(cursor.getColumnIndex("course_name"));
            String  time = cursor.getString(cursor.getColumnIndex("course_time"));
            String  period = cursor.getString(cursor.getColumnIndex("course_period"));
            String  teacher = cursor.getString(cursor.getColumnIndex("teacher_name"));

            item it = new item(name,time,period,teacher);
            arrayList.add(it);



        }

         c = new courseAdapter(choose_course.this,arrayList);

        listView_course.setAdapter(c);

    }

    }





