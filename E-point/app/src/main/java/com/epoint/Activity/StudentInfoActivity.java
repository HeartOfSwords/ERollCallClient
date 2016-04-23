package com.epoint.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.snowalker.erollcall.R;
/**
 * Created by duanjigui on 2016/4/23.
 */
public class StudentInfoActivity extends Activity {
    private TextView clas;
    private TextView college;
    private TextView id;
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showstudentinfo);
        clas=(TextView)findViewById(R.id.stu_class);
        college=(TextView)findViewById(R.id.stu_collegr);
        id=(TextView)findViewById(R.id.stu_id);
        name=(TextView)findViewById(R.id.stu_name);
        Intent intent= getIntent();
        clas.setText(intent.getStringExtra("classNum"));
        college.setText(intent.getStringExtra("college"));
        id.setText(intent.getStringExtra("id"));
        name.setText(intent.getStringExtra("name"));
    }
}
