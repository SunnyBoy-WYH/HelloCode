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

public class teacher_Fragment extends Fragment {
    SQLiteDatabase db;
    EditText edit_querybyid;

    String listview_state ="";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_fragment,null);
        return view;

    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //清除查询结果的按钮
        Button button_clear_query = view.findViewById(R.id.f_t_clear_query);

        //查询教师登陆信息的button
        Button button_look_account = view.findViewById(R.id.f_t_look_teacher);
        //查询教师信息的button
        Button button_query_teacher = view.findViewById(R.id.f_t_query_teacher);


        //设置监听器







        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /******列表视图设置********/
                final ListView listView =view.findViewById(R.id.f_t_listview);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        //若是老师状态
                         if(listview_state.equals("teacher"))
                        {
                            HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                            Intent intent_delete = new Intent(getActivity(), delete_change_teacher.class);
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
                         else if(listview_state.equals("account_teacher"))
                         {
                             HashMap<String,Object > map_item = (HashMap<String,Object >)listView.getItemAtPosition(position);

                             Intent intent = new Intent(getActivity(), change_account_teacher.class);
                             intent.putExtra("account",map_item.get("account")+"");
                             intent.putExtra("password",map_item.get("password")+"");
                             startActivity(intent);
                         }




                        //

                    }
                });
                /*****建立数据库，并建表*******/
                CommonDatabase commonDatabase= new CommonDatabase();
                db=commonDatabase.getSqliteObject(getActivity(),"test_db");
                /*****设置按钮点击监听器******/
                switch(v.getId()){






                    /******清除查询按钮*****/

                    case R.id.f_t_clear_query:

                        listView.setAdapter(null);

                        break;

                    /*****查询老师信息****/
                    case R.id.f_t_query_teacher:
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
                        SimpleAdapter simpleAdapter_teacher=new SimpleAdapter(getActivity(),arrayList_teacher,R.layout.list__item_teacher,
                                new String[]{"teacher_id","name","sex","age","level","phone","college"},
                                new int[]{R.id.list_t_id,R.id.list_t_name,R.id.list_t_sex,R.id.list_t_age,
                                        R.id.list_t_level,R.id.list_t_phone,R.id.list_t_college});
                        listView.setAdapter(simpleAdapter_teacher);
                        break;
                    /*****查询教师登陆信息****/
                    case R.id.f_t_look_teacher:
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
                        SimpleAdapter simpleAdapter_look=new SimpleAdapter(getActivity(),arrayList_look,R.layout.list_item_account,
                                new String[]{"account","password"},new int[]{R.id.account_t,R.id.account_tv});
                        listView.setAdapter(simpleAdapter_look);

                        break;




                    default:

                        break;

                }

            }


        };

        button_clear_query.setOnClickListener(listener);
        button_look_account.setOnClickListener(listener);
        button_query_teacher.setOnClickListener(listener);

    }
}
