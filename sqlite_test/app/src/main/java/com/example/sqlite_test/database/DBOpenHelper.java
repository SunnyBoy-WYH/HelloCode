package com.example.sqlite_test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

        //带全部参数的构造函数，此构造函数必不可少
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            //创建数据库sql语句 并 执行
            //学生信息
            String sql_student = "create table student(" +
                    "id int primary key not null ," +
                    "name varchar(20)  ," +
                    "sex varchar(10), "+
                    "age int ,"+
                    "banji varchar(20) ,"+
                    "phone int ,"+
                    "college varchar(20))"
                    ;
            //学生——登陆密码表
            String sql_student_load = "create table load_account(account int primary Key not null ,password varchar(20))";

            String sql_ctrigger = "CREATE TRIGGER load_add AFTER INSERT ON student  " +
                    "BEGIN INSERT INTO load_account(account,password) VALUES (new.id,'123456'); END;";
            String sql_trigger = "CREATE TRIGGER delete_load BEFORE DELETE ON student BEGIN DELETE  FROM load_account WHERE account =old.id ;END ; ";

            db.execSQL(sql_student_load);
            db.execSQL(sql_student);
            db.execSQL(sql_ctrigger);
            db.execSQL(sql_trigger);


            //老师个人信息表
            String sql_teacher ="create table teacher(" +
                    "teacher_id int primary key not null," +
                    "name varchar(20)," +
                    "sex  varchar(20)," +

                    "age int ," +
                    "level varchar(20)," +
                    "phone int ," +
                    "college varchar(20))";
            //老师登录
            String sql_teacher_load = "create table load_teacher(" +
                    "account int primary key not null ," +
                    "password varchar(20))";

            String sql_ttrigger = "CREATE TRIGGER load_add_teacher AFTER INSERT ON teacher  " +
                    "BEGIN INSERT INTO load_teacher(account,password) VALUES (new.teacher_id,'123456'); END;";
            String sql_dttrigger = "CREATE TRIGGER delete_teacher_load " +
                    " BEFORE DELETE ON teacher BEGIN DELETE FROM load_teacher WHERE ACCOUNT = OLD.TEACHER_ID; END;";

            db.execSQL(sql_teacher_load);
            db.execSQL(sql_teacher);
            db.execSQL(sql_ttrigger);
            db.execSQL(sql_dttrigger);





            //学生选课信息表
            String sql_student_course = "create table student_course(" +
                    "student_id int ,"+
                    "course_name varchar(20),"+
                    "teacher_name varchar(10),"+
                    "score int, " +
                    "FOREIGN KEY(course_name) REFERENCES course(course_name)," +
                    "FOREIGN KEY(teacher_name) REFERENCES course(teacher_name)" +

                    ")";
            String sql_course = "create table course(" +
                    "teacher_name varchar(20) not null,"+
                    "course_name varchar(20) not null," +
                    "course_weight SMALLINT ,"+
                    "course_time varchar(20)," +
                    "course_period varchar(20)," +
                    "primary key(teacher_name,course_name))";

            db.execSQL(sql_student_course);
            db.execSQL(sql_course);


            String sql_liuyan  = "create table message(" +
                    "student_id int primary key not null," +
                    "message text)";
            db.execSQL(sql_liuyan);

















        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

