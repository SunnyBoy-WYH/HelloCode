package com.example.sqlite_test.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.administractor.MainActivity;
import com.example.sqlite_test.administractor.change_account;
import com.example.sqlite_test.administractor.delete_change;
import com.example.sqlite_test.database.CommonDatabase;
import com.example.sqlite_test.student.activity_student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_teacher extends AppCompatActivity {
    ListView listView ;
    SQLiteDatabase db ;
    //state在这里负责标记是到查看我的课程这一步还是到下一步，也就是点击课程出来的学生，再点学生。
    String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);


        //获取数据库对象
        db = new CommonDatabase().getSqliteObject(activity_teacher.this,"test_db");

        final Intent receive_intent = getIntent();
        listView = findViewById(R.id.listview_teacher);

        Button button_look_mycourse = findViewById(R.id.button_lookmycourse);
        Button button_look_message = findViewById(R.id.teacher_look_message);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /****点击老师拥有的课程的****/
                if (state.equals("mycourse")) {
//                    //Map <String,Object> map= new HashMap<String,Object>();
                    HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);
                    //获取选择这个子项的课程名称 ，去查找这个课程下的学生
                    String course_name = map_item.get("course_name") + "";
                    Cursor cursor1 =db.rawQuery(
                            "select * from student inner join student_course " +
                                    "on student.id =student_course.student_id " +
                                    "where course_name = ?",new String[]{course_name});
                    if(cursor1.getCount()==0)
                    {
                        Toast.makeText(activity_teacher.this, "还没有任何人选这门课!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ArrayList<Map<String, String>> arrayList_student = new ArrayList<Map<String, String>>();
                        while (cursor1.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();

                            map.put("student_id", cursor1.getString(cursor1.getColumnIndex("student_id")));
                            map.put("name", cursor1.getString(cursor1.getColumnIndex("name")));
                            map.put("banji", cursor1.getString(cursor1.getColumnIndex("banji")));
                            map.put("course_name",cursor1.getString(cursor1.getColumnIndex("course_name")));
                            map.put("score", cursor1.getString(cursor1.getColumnIndex("score")));


                            arrayList_student.add(map);

                        }
                        Toast.makeText(activity_teacher.this, arrayList_student.size() + "", Toast.LENGTH_SHORT).show();
                        //设置适配器
                        SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_student, R.layout.list_item_course_information,
                                new String[]{"student_id", "name","banji","course_name","score"}, new int[]{R.id.course_student_id,
                                R.id.course_student_name,R.id.course_student_banji,R.id.course_student_coursename,R.id.course_student_score});
                        listView.setAdapter(simpleAdapter);
                       //再利用item监听器时，就是选择学生了该
                        state="mycourse_student";
                    }



                }
                else if(state.equals("mycourse_student"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent = new Intent(activity_teacher.this, change_student_score.class);
                    intent.putExtra("student_id",map_item.get("student_id")+"");
                    intent.putExtra("name",map_item.get("name")+"");
                    intent.putExtra("banji",map_item.get("banji")+"");
                    intent.putExtra("course_name",map_item.get("course_name")+"");
                    intent.putExtra("score",map_item.get("score")+"");

                    startActivity(intent);
                }
            }
        });






        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacher_name = "";
                switch (v.getId()) {

                    case R.id.button_lookmycourse:
                        state = "mycourse";

                        //去老师信息表中根据老师的id查找它的姓名
                        Cursor cursor_look_for_id = db.query("teacher", null, "teacher_id =?", new String[]{receive_intent.getStringExtra("teacher_id")}, null, null, null);
                        while (cursor_look_for_id.moveToNext()) {
                            teacher_name = cursor_look_for_id.getString(cursor_look_for_id.getColumnIndex("name"));
                        }
                        // Toast.makeText(activity_teacher.this,teacher_name,Toast.LENGTH_SHORT).show();
                        //以老师姓名为根本去查询老师旗下的课程信息


                        Cursor cursor = db.query("course", null, "teacher_name = ?", new String[]{teacher_name}, null, null, null);

                        //对游标进行遍历
                        if (cursor.getCount() == 0) {
                            Toast.makeText(activity_teacher.this, "您没有任何课", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_mycourse = new ArrayList<Map<String, String>>();
                            while (cursor.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();

                                map.put("course_name", cursor.getString(cursor.getColumnIndex("course_name")));
                                map.put("course_time", cursor.getString(cursor.getColumnIndex("course_time")));
                                map.put("course_period", cursor.getString(cursor.getColumnIndex("course_period")));


                                arrayList_mycourse.add(map);

                            }
                            Toast.makeText(activity_teacher.this, arrayList_mycourse.size() + "", Toast.LENGTH_SHORT).show();
                            //设置适配器
                            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_mycourse, R.layout.list_item_teacher_mycourse,
                                    new String[]{"course_name", "course_time", "course_period"}, new int[]{R.id.t_mycourse_name, R.id.t_mycourse_time, R.id.t_mycourse_period});
                            listView.setAdapter(simpleAdapter);


                        }
                        break;
                    case R.id.teacher_look_message:
                        Cursor cursor_look = db.query("message", null, null, null, null, null, null);

                        //对游标进行遍历
                        if (cursor_look.getCount() == 0) {
                            Toast.makeText(activity_teacher.this, "还没有任何留言", Toast.LENGTH_SHORT).show();
                        } else {
                            ArrayList<Map<String, String>> arrayList_mycourse = new ArrayList<Map<String, String>>();
                            while (cursor_look.moveToNext()) {
                                Map<String, String> map = new HashMap<String, String>();

                                map.put("message", cursor_look.getString(cursor_look.getColumnIndex("message")));


                                arrayList_mycourse.add(map);

                            }
                            //设置适配器
                            SimpleAdapter simpleAdapter = new SimpleAdapter(activity_teacher.this, arrayList_mycourse, R.layout.list_item_message_t_look,
                                    new String[]{"message"}, new int[]{R.id.text});
                            listView.setAdapter(simpleAdapter);


                            break;
                        }
                }
            }
        };
        button_look_mycourse.setOnClickListener(listener);
        button_look_message.setOnClickListener(listener);
    }
}
