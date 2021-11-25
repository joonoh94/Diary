package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class test extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    //firebase data object
    DatabaseReference mDatabaseReference; // 데이터베이스의 주소를 저장합니다.
    FirebaseDatabase mFirebaseDatabase; // 데이터베이스에 접근할 수 있는 진입점 클래스입니다.
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        EditText day = findViewById(R.id.day);
        EditText title = findViewById(R.id.title);
        EditText content = findViewById(R.id.content);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleData scheduleData = new ScheduleData();
                scheduleData.setContent(content.getText().toString());
                scheduleData.setTitle(title.getText().toString());
                scheduleData.setDay(day.getText().toString());

                //firebase auth object
                 FirebaseAuth firebaseAuth;

                //firebase data object
                //firebase auth object
                //year+"-"+month+"-"+day
                mDatabaseReference = mFirebaseDatabase.getReference().child("schedule").child(user.getUid()).child(day.getText().toString()).child(title.getText().toString());
                // filebase/calendar/userUid/date save
                mDatabaseReference.setValue(scheduleData);
                finish();
            }
        });
    }
}