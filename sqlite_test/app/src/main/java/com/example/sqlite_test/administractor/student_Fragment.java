package com.example.sqlite_test.administractor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sqlite_test.R;
import com.example.sqlite_test.database.CommonDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class student_Fragment extends Fragment {
    SQLiteDatabase db;
    EditText edit_querybyid;
    EditText edit_querybyname;
    EditText edit_querybybanji;
    //记录listview显示状态，方便设置触发器
    String listview_state = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_fragment, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //增加学生的按钮
        Button button_add = view.findViewById(R.id.f_add_student);


        //查询所有学生的按钮
        Button button_query = view.findViewById(R.id.f_query);
        //清除查询结果的按钮
        Button button_clear_query = view.findViewById(R.id.f_clear_query);
        //查询学生登录账号密码的按钮
        Button button_query_account = view.findViewById(R.id.f_query_account);
        //按照学号查询的按钮
        Button button_query_byterm = view.findViewById(R.id.f_query_byterm);
        //按照学号查询的编辑框
        edit_querybyid = view.findViewById(R.id.f_edit_querybyid);
        edit_querybyname = view.findViewById(R.id.f_edit_querybyname);
        edit_querybybanji = view.findViewById(R.id.f_edit_querybybanji);


        /*****建立数据库，并建表*******/
        CommonDatabase commonDatabase = new CommonDatabase();
        db = commonDatabase.getSqliteObject(getActivity(), "test_db");

        final ListView listView = view.findViewById(R.id.f_listview);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /******列表视图设置********/

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (listview_state.equals("student")) {
                            //Map <String,Object> map= new HashMap<String,Object>();
                            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), delete_change.class);
                            //获取map中的三项数据，并放入intent
                            intent_delete.putExtra("id", map_item.get("id") + "");
                            intent_delete.putExtra("name", map_item.get("name") + "");
                            intent_delete.putExtra("sex", map_item.get("sex") + "");
                            intent_delete.putExtra("age", map_item.get("age") + "");
                            intent_delete.putExtra("banji", map_item.get("banji") + "");
                            intent_delete.putExtra("phone", map_item.get("phone") + "");
                            intent_delete.putExtra("college", map_item.get("college") + "");
                            startActivity(intent_delete);
                        } else if (listview_state.equals("account")) {
                            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                            Intent intent = new Intent(getActivity(), change_account.class);
                            intent.putExtra("account", map_item.get("account") + "");
                            intent.putExtra("password", map_item.get("password") + "");
                            startActivity(intent);
                        }
                        //若是老师状态
                        else if (listview_state.equals("teacher")) {
                            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), delete_change_teacher.class);
                            //获取map中的三项数据，并放入intent
                            intent_delete.putExtra("teacher_id", map_item.get("teacher_id") + "");
                            intent_delete.putExtra("name", map_item.get("name") + "");
                            intent_delete.putExtra("sex", map_item.get("sex") + "");
                            intent_delete.putExtra("age", map_item.get("age") + "");
                            intent_delete.putExtra("level", map_item.get("level") + "");
                            intent_delete.putExtra("phone", map_item.get("phone") + "");
                            intent_delete.putExtra("college", map_item.get("college") + "");
                            startActivity(intent_delete);

                        } else if (listview_state.equals("change_course_set")) {
                            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), change_course_set.class);
                            //获取map中的三项数据，并放入intent
                            intent_delete.putExtra("teacher_name", map_item.get("teacher_name") + "");
                            intent_delete.putExtra("course_name", map_item.get("course_name") + "");
                            intent_delete.putExtra("course_time", map_item.get("course_time") + "");
                            intent_delete.putExtra("course_period", map_item.get("course_period") + "");

                            startActivity(intent_delete);
                        } else if (listview_state.equals("message")) {

                        } else {
                            HashMap<String, Object> map_item = (HashMap<String, Object>) listView.getItemAtPosition(position);

                            Intent intent = new Intent(getActivity(), change_account_teacher.class);
                            intent.putExtra("account", map_item.get("account") + "");
                            intent.putExtra("password", map_item.get("password") + "");
                            startActivity(intent);

                        }

                        //

                    }
                });

                /*****设置按钮点击监听器******/
                switch (v.getId()) {

                    case R.id.f_query_byterm:
                        listview_state = "student";

                        final String e_id = edit_querybyid.getText().toString();
                        final String e_name = edit_querybyname.getText().toString();
                        final String e_banji = edit_querybybanji.getText().toString();
                        /****按照id查询****/
                        if (!e_id.equals("") && e_name.equals("") && e_banji.equals("")) {

                            Cursor cursor_query_byid = db.query("student", null, "id = ?", new String[]{e_id}, null, null, null);
                            query_byitem(cursor_query_byid,listView);

                        }
                        /****按照姓名查询****/
                        else if (e_id.equals("") && !e_name.equals("") && e_banji.equals("")) {

                            Cursor cursor_query_byid = db.query("student", null, "name = ?", new String[]{e_name}, null, null, null);
                            query_byitem(cursor_query_byid,listView);


                        }
                        /****按照班级查询****/
                        else if(e_id.equals("") && e_name.equals("") && !e_banji.equals(""))
                        {

                            Cursor cursor_query_byid = db.query("student", null, "banji = ?", new String[]{e_banji}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }
                        /****按照id查询****/
                        else if(!e_id.equals("") && !e_name.equals("") && e_banji.equals(""))
                        {
                            Cursor cursor_query_byid = db.query("student", null, "id = ? AND name = ?" , new String[]{e_id,e_name}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }
                        else if(!e_id.equals("") && e_name.equals("") && !e_banji.equals(""))
                        {
                            Cursor cursor_query_byid = db.query("student", null, "id = ? AND  banji = ?", new String[]{e_id,e_banji}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }
                        else if(e_id.equals("") && !e_name.equals("") && !e_banji.equals(""))
                        {
                            Cursor cursor_query_byid = db.query("student", null, "name = ? AND banji = ?", new String[]{e_name ,e_banji}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }
                        else if(e_id.equals("") && e_name.equals("") && e_banji.equals(""))
                        {
                            Toast.makeText(getActivity(), "您的查询条件为空!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Cursor cursor_query_byid = db.query("student", null, "id = ? AND name = ? AND banji = ?  ", new String[]{e_id,e_name ,e_banji}, null, null, null);
                            query_byitem(cursor_query_byid,listView);
                        }


                        break;


                    /***************************************查询所有学生信息*********************************/
                    case R.id.f_query:
                        //查询的是学生表,所以
                        listview_state = "student";
                        //建立游标对象
                        Cursor cursor = db.query("student", null, null, null, null, null, null);
                        ArrayList<Map<String, String>> arrayList = new ArrayList<Map<String, String>>();
                        //对游标进行遍历
                        while (cursor.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();

                            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
                            map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                            map.put("sex", cursor.getString(cursor.getColumnIndex("sex")));
                            map.put("age", cursor.getString(cursor.getColumnIndex("age")));
                            map.put("banji", cursor.getString(cursor.getColumnIndex("banji")));
                            map.put("phone", cursor.getString(cursor.getColumnIndex("phone")));
                            map.put("college", cursor.getString(cursor.getColumnIndex("college")));

                            arrayList.add(map);

                        }
                        //设置适配器
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList, R.layout.list_item,
                                new String[]{"name", "id", "sex", "age", "banji", "phone", "college"}, new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage,
                                R.id.ibanji, R.id.iphone, R.id.icollege});
                        listView.setAdapter(simpleAdapter);
                        break;

                    /******清除查询按钮*****/

                    case R.id.f_clear_query:

                        listView.setAdapter(null);

                        break;
                    /******查询所有学生用户账号密码*******/
                    case R.id.f_query_account:
                        //查询的是账户，所以设置listview为查询账户状态
                        listview_state = "account";


                        Cursor cursor_account = db.query("load_account", null, null, null, null, null, null);
                        ArrayList<Map<String, String>> arrayList_account = new ArrayList<Map<String, String>>();
                        //对游标进行遍历
                        while (cursor_account.moveToNext()) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("account", cursor_account.getString(cursor_account.getColumnIndex("account")));
                            map.put("password", cursor_account.getString(cursor_account.getColumnIndex("password")));
                            arrayList_account.add(map);

                        }

                        //设置适配器
                        SimpleAdapter simpleAdapter_account = new SimpleAdapter(getActivity(), arrayList_account, R.layout.list_item_account,
                                new String[]{"account", "password"}, new int[]{R.id.account_t, R.id.account_tv});
                        listView.setAdapter(simpleAdapter_account);
                        break;
                    case R.id.f_add_student:
                        Intent intent_add1 = new Intent(getActivity(), add.class);
                        startActivity(intent_add1);


                        break;

                    default:

                        break;

                }

            }


        };
        button_add.setOnClickListener(listener);

        button_query.setOnClickListener(listener);
        //清除查询结果的按钮
        button_clear_query.setOnClickListener(listener);
        //查询学生登录账号密码的按钮
        button_query_account.setOnClickListener(listener);
        //按照学号查询的按钮
        button_query_byterm.setOnClickListener(listener);


    }

    public void query_byitem(Cursor cursor_query_byid, ListView listView) {
        ArrayList<Map<String, String>> arrayList_query = new ArrayList<Map<String, String>>();
        if (cursor_query_byid.getCount() == 0) {
            listView.setAdapter(null);
            Toast.makeText(getActivity(), "没有查到任何结果", Toast.LENGTH_SHORT).show();
        }
        //对游标进行遍历
        else {
            while (cursor_query_byid.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();

                map.put("name", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("name")));
                map.put("id", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("id")));
                map.put("sex", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("sex")));
                map.put("age", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("age")));
                map.put("banji", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("banji")));
                map.put("phone", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("phone")));
                map.put("college", cursor_query_byid.getString(cursor_query_byid.getColumnIndex("college")));
                arrayList_query.add(map);

            }
            //设置适配器
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), arrayList_query, R.layout.list_item,
                    new String[]{"name", "id", "sex", "age", "banji", "phone", "college"}, new int[]{R.id.iname, R.id.iid, R.id.isex, R.id.iage,
                    R.id.ibanji, R.id.iphone, R.id.icollege});
            listView.setAdapter(simpleAdapter);
        }

    }
}
