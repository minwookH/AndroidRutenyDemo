package address.teruten.com.terutenaddress.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.Member;

public class DbSQLiteOpenHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public DbSQLiteOpenHelper(Context context, String name,
                              CursorFactory factory, int version) {

        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table memberList (" +
                "id text, " +
                "name text, " +
                "dutyName text, " +
                "pName text, " +
                "department text, " +
                "departmentId text, " +
                "position text, " +
                "departmentHead text, " +
                "departmentUid text, " +
                "tel1 text, "+
                "tel2 text, "+
                "tel3 text, "+
                "innerTel text, " +
                "reportUse text);";

        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*String sql = "drop table if exists memberList";
        db.execSQL(sql);

        onCreate(db); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정*/
        Log.i("db", "onUpgrade oldVersion : "+oldVersion+" , newVersion : "+newVersion );
        switch (oldVersion) {
            case 1 :
                try {
                    db.beginTransaction();
                    db.execSQL("ALTER TABLE " + "memberList" + " ADD COLUMN " + "reportUse" + " text DEFAULT '0'");
                    db.setTransactionSuccessful();
                } catch (IllegalStateException e) {
                    Log.e("db", "onUpgrade error : "+e.getLocalizedMessage());
                } finally {
                    db.endTransaction();
                }
                break;
        }
    }

    // insert

    public void insert(ArrayList<Member> members) {

        db = getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

        ContentValues values = new ContentValues();
        for (Member member: members) {

            if(selectItem(member.getId()) == null){

                values.clear();
                // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
                // 데이터의 삽입은 put을 이용한다.
                Log.i("esef1", "db insert member.getId() : "+member.getId());
                values.put("id", member.getId());
                values.put("name", member.getName());
                values.put("dutyName", member.getDutyName());
                values.put("pName", member.getpName());
                values.put("department", member.getDepartment());
                values.put("departmentId", member.getDepartmentId());
                values.put("position", member.getPosition());
                values.put("departmentHead", member.getDepartmentHead());
                values.put("departmentUid", member.getDepartmentUid());
                values.put("tel1", member.getTel1());
                values.put("tel2", member.getTel2());
                values.put("tel3", member.getTel3());
                values.put("innerTel", member.getInnerTel());
                values.put("reportUse", member.getReportUse());

                db.insert("memberList", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
            }else{
                update(member);
            }


        }

    }



    // update

    public void update (Member member) {
        db = getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();

        values.put("id", member.getId());
        values.put("name", member.getName());
        values.put("dutyName", member.getDutyName());
        values.put("pName", member.getpName());
        values.put("department", member.getDepartment());
        values.put("departmentId", member.getDepartmentId());
        values.put("position", member.getPosition());
        values.put("departmentHead", member.getDepartmentHead());
        values.put("departmentUid", member.getDepartmentUid());
        values.put("tel1", member.getTel1());
        values.put("tel2", member.getTel2());
        values.put("tel3", member.getTel3());
        values.put("innerTel", member.getInnerTel());
        values.put("reportUse", member.getReportUse());
        db.update("memberList", values, "id = ?", new String[]{member.getId()});
    }

    // delete
    public void delete (String id) {
        db = getWritableDatabase();
        db.delete("memberList", "id=?", new String[]{id});

    }

    public Member selectItem(String id){
        db = getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Log.i("esef1", "db select id : "+id);
        String sql = "select * from memberList where id = \'"+id+"\';";
        Cursor result;
        Member member = null;

        try {
            result = db.rawQuery(sql, null);

            Log.i("esef1", "db result.isFirst() : "+result.isFirst());
            // result(Cursor 객체)가 비어 있으면 false 리턴
            if(result.moveToFirst()){
                Log.i("esef1", "db result.moveToFirst() true : ");
                Log.i("esef1", "db result Id : " + result.getString(0));
                Log.i("esef1", "db result setName : " + result.getString(1));
                Log.i("esef1", "db result setReportUse : " + result.getString(13));
                member = new Member();

                member.setId(result.getString(0));
                member.setName(result.getString(1));
                member.setDutyName(result.getString(2));
                member.setpName(result.getString(3));
                member.setDepartment(result.getString(4));
                member.setDepartmentId(result.getString(5));
                member.setPosition(result.getString(6));
                member.setDepartmentHead(result.getString(7));
                member.setDepartmentUid(result.getString(8));
                member.setTel1(result.getString(9));
                member.setTel2(result.getString(10));
                member.setTel3(result.getString(11));
                member.setInnerTel(result.getString(12));
                member.setReportUse(result.getString(13));

                result.close();
            }
        }catch (SQLiteException e){
            e.getLocalizedMessage();
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



        return member;
    }

    //리스트 화면에 나타날 알람 리스트
    public ArrayList<Member> getMemberList() {
        db = getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("memberList", null, null, null, null, null, null);

        ArrayList<Member> memberList = new ArrayList<>();

        while (c.moveToNext()) {
            Member member = new Member();
            Log.d("spinnerItem", "memberList name : "+ c.getString(c.getColumnIndex("name")));
            member.setId(c.getString(c.getColumnIndex("id")));
            member.setName(c.getString(c.getColumnIndex("name")));
            member.setDutyName(c.getString(c.getColumnIndex("dutyName")));
            member.setpName(c.getString(c.getColumnIndex("pName")));
            member.setDepartment(c.getString(c.getColumnIndex("department")));
            member.setDepartmentId(c.getString(c.getColumnIndex("departmentId")));
            member.setPosition(c.getString(c.getColumnIndex("position")));
            member.setDepartmentHead(c.getString(c.getColumnIndex("departmentHead")));
            member.setDepartmentUid(c.getString(c.getColumnIndex("departmentUid")));
            member.setTel1(c.getString(c.getColumnIndex("tel1")));
            member.setTel2(c.getString(c.getColumnIndex("tel2")));
            member.setTel3(c.getString(c.getColumnIndex("tel3")));
            member.setInnerTel(c.getString(c.getColumnIndex("innerTel")));
            member.setReportUse(c.getString(c.getColumnIndex("reportUse")));
            memberList.add(member);
        }

        Log.d("spinnerItem", "memberList size : "+ memberList.size());
        return memberList;
    }

}
