package com.example.sqlite_test.administractor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edit_querybyid;
    SQLiteDatabase db;

    String listview_state ="";





    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //绑定按钮
        init();




    }
    public void init()
    {
        //增加信息的按钮
        Button button_add =  findViewById(R.id.button_add);
        //查询所有的按钮
        Button button_query = findViewById(R.id.button_query);
        //清除查询结果的按钮
        Button button_clear_query = findViewById(R.id.button_clear_query);
        //查询学生登录账号密码的按钮
        Button button_query_account = findViewById(R.id.button_query_account);
        //按照学号查询的按钮
        Button button_query_byid = findViewById(R.id.button_query_byid);
        //按照学号查询的编辑框
        edit_querybyid = findViewById(R.id.edit_querybyid);
        //增加教师信息的button
        Button button_add_teacher = findViewById(R.id.button_add_teacher);
        //查询教师登陆信息的button
        Button button_look_account = findViewById(R.id.button_look_teacher);
        //查询教师信息的button
        Button button_query_teacher = findViewById(R.id.button_query_teacher);
        //查看课程信息的button
        Button button_query_course = findViewById(R.id.button_look_sumcourse);
        //查看留言的button
        Button button_look_message = findViewById(R.id.button_query_liuyan);

        //设置监听器
        button_add.setOnClickListener(this);
        button_query.setOnClickListener(this);
        button_clear_query.setOnClickListener(this);
        button_query_byid.setOnClickListener(this);
        button_query_account.setOnClickListener(this);
        button_add_teacher.setOnClickListener(this);
        button_look_account.setOnClickListener(this);
        button_query_teacher.setOnClickListener(this);
        button_query_course.setOnClickListener(this);
        button_look_message.setOnClickListener(this);





    }



    @Override

    public void onClick(View v) {
        /******列表视图设置********/
        final ListView listView = findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listview_state.equals("student"))
                {
                    //Map <String,Object> map= new HashMap<String,Object>();
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(MainActivity.this, delete_change.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("id",map_item.get("id")+"");
                    intent_delete.putExtra("name",map_item.get("name")+"");
                    intent_delete.putExtra("sex",map_item.get("sex")+"");
                    intent_delete.putExtra("age",map_item.get("age")+"");
                    intent_delete.putExtra("banji",map_item.get("banji")+"");
                    intent_delete.putExtra("phone",map_item.get("phone")+"");
                    intent_delete.putExtra("college",map_item.get("college")+"");
                    startActivity(intent_delete);
                }
                else if(listview_state.equals("account"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, change_account.class);
                    intent.putExtra("account",map_item.get("account")+"");
                    intent.putExtra("password",map_item.get("password")+"");
                    startActivity(intent);
                }
                //若是老师状态
                else if(listview_state.equals("teacher"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(MainActivity.this, delete_change_teacher.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("teacher_id",map_item.get("teacher_id")+"");
                    intent_delete.putExtra("name",map_item.get("name")+"");
                    intent_delete.putExtra("sex",map_item.get("sex")+"");
                    intent_delete.putExtra("age",map_item.get("age")+"");
                    intent_delete.putExtra("level",map_item.get("level")+"");
                    intent_delete.putExtra("phone",map_item.get("phone")+"");
                    intent_delete.putExtra("college",map_item.get("college")+"");
                    startActivity(intent_delete);

                }
                else  if(listview_state.equals("change_course_set"))
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent_delete = new Intent(MainActivity.this, change_course_set.class);
                    //获取map中的三项数据，并放入intent
                    intent_delete.putExtra("teacher_name",map_item.get("teacher_name")+"");
                    intent_delete.putExtra("course_name",map_item.get("course_name")+"");
                    intent_delete.putExtra("course_time",map_item.get("course_time")+"");
                    intent_delete.putExtra("course_period",map_item.get("course_period")+"");

                    startActivity(intent_delete);
                }
                else if (listview_state.equals("message"))
                {

                }
                else
                {
                    HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, change_account_teacher.class);
                    intent.putExtra("account",map_item.get("account")+"");
                    intent.putExtra("password",map_item.get("password")+"");
                    startActivity(intent);

                }

             //

            }
        });
        /*****建立数据库，并建表*******/
        CommonDatabase commonDatabase= new CommonDatabase();
        db=commonDatabase.getSqliteObject(MainActivity.this,"test_db");
        /*****设置按钮点击监听器******/
        switch(v.getId()){
            /****按照id查询****/
            case R.id.button_query_byid:
                String text_query_by_id = edit_querybyid.getText().toString();
                Cursor cursor_query_byid = db.query("student", null, "id = ?", new String[]{text_query_by_id}, null, null, null);
                ArrayList<Map<String,String>> arrayList_query= new ArrayList<Map<String,String>>();
                if(cursor_query_byid.getCount()==0)
                {
                    listView.setAdapter(null);
                    Toast.makeText(MainActivity.this,"没有查到任何结果",Toast.LENGTH_SHORT).show();
                }
                //对游标进行遍历
                else
                {
                    while(cursor_query_byid.moveToNext())
                    {
                        Map <String,String> map= new HashMap<String,String>();

                        map.put("name",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("name")));
                        map.put("id",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("id")));
                        map.put("sex",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("sex")));
                        map.put("age",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("age")));
                        map.put("banji",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("banji")));
                        map.put("phone",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("phone")));
                        map.put("college",cursor_query_byid.getString(cursor_query_byid.getColumnIndex("college")));
                        arrayList_query.add(map);

                    }
                    //设置适配器
                    SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList_query,R.layout.list_item,
                            new String[]{"name","id","sex","age","banji","phone","college"},new int[]{R.id.iname,R.id.iid,R.id.isex,R.id.iage,
                            R.id.ibanji,R.id.iphone,R.id.icollege});
                    listView.setAdapter(simpleAdapter);

                }
                break;


            //增加信息按钮
            case R.id.button_add:
                  Intent intent_add = new Intent(MainActivity.this, add.class);
                  startActivity(intent_add);

                  break;
           /***************************************查询所有学生信息*********************************/
            case R.id.button_query:
                //查询的是学生表,所以
                listview_state="student";
                //建立游标对象
                Cursor cursor = db.query("student", null, null, null, null, null, null);
                ArrayList<Map<String,String>> arrayList= new ArrayList<Map<String,String>>();
                //对游标进行遍历
                while(cursor.moveToNext())
                {
                    Map <String,String> map= new HashMap<String,String>();

                    map.put("name",cursor.getString(cursor.getColumnIndex("name")));
                    map.put("id",cursor.getString(cursor.getColumnIndex("id")));
                    map.put("sex",cursor.getString(cursor.getColumnIndex("sex")));
                    map.put("age",cursor.getString(cursor.getColumnIndex("age")));
                    map.put("banji",cursor.getString(cursor.getColumnIndex("banji")));
                    map.put("phone",cursor.getString(cursor.getColumnIndex("phone")));
                    map.put("college",cursor.getString(cursor.getColumnIndex("college")));

                    arrayList.add(map);

                }
                //设置适配器
                SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.list_item,
                        new String[]{"name","id","sex","age","banji","phone","college"},new int[]{R.id.iname,R.id.iid,R.id.isex,R.id.iage,
                        R.id.ibanji,R.id.iphone,R.id.icollege});
                listView.setAdapter(simpleAdapter);
                break;

            /******清除查询按钮*****/

            case R.id.button_clear_query:

                listView.setAdapter(null);

                break;
                /******查询所有学生用户账号密码*******/
            case R.id.button_query_account:
                //查询的是账户，所以设置listview为查询账户状态
                listview_state ="account";


                Cursor cursor_account = db.query("load_account", null, null, null, null, null, null);
                ArrayList<Map<String,String>> arrayList_account= new ArrayList<Map<String,String>>();
                //对游标进行遍历
               while(cursor_account.moveToNext())
                {
                    Map <String,String> map= new HashMap<String,String>();
                    map.put("account",cursor_account.getString(cursor_account.getColumnIndex("account")));
                    map.put("password",cursor_account.getString(cursor_account.getColumnIndex("password")));
                    arrayList_account.add(map);

                }

                //设置适配器
                SimpleAdapter simpleAdapter_account=new SimpleAdapter(this,arrayList_account,R.layout.list_item_account,
                        new String[]{"account","password"},new int[]{R.id.account_t,R.id.account_tv});
                listView.setAdapter(simpleAdapter_account);
                break;
                /*****增加教师信息*****/
            case R.id.button_add_teacher:
                Intent intent_add_teacher = new Intent(MainActivity.this, add_teacher.class);
                startActivity(intent_add_teacher);
                break;
              /*****查询老师信息****/
            case R.id.button_query_teacher:
                //设置listview状态为老师
                listview_state="teacher";

                Cursor cursor_teacher = db.query("teacher", null, null, null, null, null, null);
                ArrayList<Map<String,String>> arrayList_teacher= new ArrayList<Map<String,String>>();
                //对游标进行遍历
                while(cursor_teacher.moveToNext())
                {

                    Map <String,String> map= new HashMap<String,String>();

                    map.put("teacher_id",cursor_teacher.getString(cursor_teacher.getColumnIndex("teacher_id")));
                    map.put("name",cursor_teacher.getString(cursor_teacher.getColumnIndex("name")));
                    map.put("sex",cursor_teacher.getString(cursor_teacher.getColumnIndex("sex")));
                    map.put("age",cursor_teacher.getString(cursor_teacher.getColumnIndex("age")));
                    map.put("level",cursor_teacher.getString(cursor_teacher.getColumnIndex("level")));
                    map.put("phone",cursor_teacher.getString(cursor_teacher.getColumnIndex("phone")));
                    map.put("college",cursor_teacher.getString(cursor_teacher.getColumnIndex("college")));

                    arrayList_teacher.add(map);

                }
                //设置适配器
                SimpleAdapter simpleAdapter_teacher=new SimpleAdapter(this,arrayList_teacher,R.layout.list__item_teacher,
                        new String[]{"teacher_id","name","sex","age","level","phone","college"},
                        new int[]{R.id.list_t_id,R.id.list_t_name,R.id.list_t_sex,R.id.list_t_age,
                        R.id.list_t_level,R.id.list_t_phone,R.id.list_t_college});
                listView.setAdapter(simpleAdapter_teacher);
                break;
                /*****查询教师登陆信息****/
            case R.id.button_look_teacher:
                //设置listview为老师账户的查询状态
                listview_state="account_teacher";

                Cursor cursor_look = db.query("load_teacher", null, null, null, null, null, null);
                ArrayList<Map<String,String>> arrayList_look= new ArrayList<Map<String,String>>();
                //对游标进行遍历
                while(cursor_look.moveToNext())
                {
                    Map <String,String> map= new HashMap<String,String>();
                    map.put("account",cursor_look.getString(cursor_look.getColumnIndex("account")));
                    map.put("password",cursor_look.getString(cursor_look.getColumnIndex("password")));
                    arrayList_look.add(map);

                }

                //设置适配器
                SimpleAdapter simpleAdapter_look=new SimpleAdapter(this,arrayList_look,R.layout.list_item_account,
                        new String[]{"account","password"},new int[]{R.id.account_t,R.id.account_tv});
                listView.setAdapter(simpleAdapter_look);

                break;
                /******查看课程设置情况******///
            case R.id.button_look_sumcourse:
                listview_state="change_course_set";

                Cursor cursor_look_course = db.query("course", null, null, null, null, null, null);
                ArrayList<Map<String,String>> arrayList_look_course= new ArrayList<Map<String,String>>();
                //对游标进行遍历
                while(cursor_look_course.moveToNext())
                {
                    Map <String,String> map= new HashMap<String,String>();
                    map.put("teacher_name",cursor_look_course.getString(cursor_look_course.getColumnIndex("teacher_name")));
                    map.put("course_name",cursor_look_course.getString(cursor_look_course.getColumnIndex("course_name")));
                    map.put("course_time",cursor_look_course.getString(cursor_look_course.getColumnIndex("course_time")));
                    map.put("course_period",cursor_look_course.getString(cursor_look_course.getColumnIndex("course_period")));
                    arrayList_look_course.add(map);

                }

                //设置适配器
                SimpleAdapter simpleAdapter_look_course=new SimpleAdapter(this,arrayList_look_course,R.layout.list_item_allcourse,
                        new String[]{"teacher_name","course_name","course_time","course_period"},new int[]{R.id.text_teacher_name,R.id.text_course_name,R.id.text_course_time,R.id.text_course_period});
                listView.setAdapter(simpleAdapter_look_course);

                break;
                /****查看留言*******/
            case R.id.button_query_liuyan:

                listview_state= "message";
                Cursor cursor_look_message = db.query("message", null, null, null, null, null, null);
                if(cursor_look_message.getCount()==0)
                {
                    Toast.makeText(MainActivity.this,"还没有任何留言哦~",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ArrayList<Map<String,String>> arrayList_look_message= new ArrayList<Map<String,String>>();
                    //对游标进行遍历
                    while(cursor_look_message.moveToNext())
                    {
                        Map <String,String> map= new HashMap<String,String>();
                        map.put("student_id",cursor_look_message.getString(cursor_look_message.getColumnIndex("student_id")));
                        map.put("message",cursor_look_message.getString(cursor_look_message.getColumnIndex("message")));

                        arrayList_look_message.add(map);

                    }

                    //设置适配器
                    SimpleAdapter simpleAdapter_look_message=new SimpleAdapter(this,arrayList_look_message,R.layout.list_item_message,
                            new String[]{"student_id","message"},new int[]{R.id.message_id,R.id.message_content});
                    listView.setAdapter(simpleAdapter_look_message);

                }

                break;



            default:

                break;

        }

    }

}