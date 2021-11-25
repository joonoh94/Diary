package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Calendar extends AppCompatActivity {

    static RecyclerViewEmptySupport  recyclerView;
    static  ArrayList<ScheduleData> data=new ArrayList<ScheduleData>();
    private static ScheduleAdapter adapter = new ScheduleAdapter();
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentDiary fragmentDiary;
    private FragmentAccount fragmentAccount;
    private FragmentSchedule fragmentSchedule;
    private FragmentTransaction transaction;

    //firebase auth object
    private static FirebaseAuth firebaseAuth;

    //firebase data object
    private static  DatabaseReference mDatabaseReference; // 데이터베이스의 주소를 저장합니다.
    private static FirebaseDatabase mFirebaseDatabase; // 데이터베이스에 접근할 수 있는 진입점 클래스입니다.
    private  static FirebaseUser user;




    // 선택한 날짜
    private static int checkYear;
    private static int checkMonth;
    private static int checkDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        TextView test2 =findViewById(R.id.tessst);
        fragmentManager = getSupportFragmentManager();
        fragmentDiary = new FragmentDiary();
        fragmentAccount = new FragmentAccount();
        fragmentSchedule = new FragmentSchedule();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentSchedule,"aa").commitAllowingStateLoss();


        Button button = (Button)findViewById(R.id.plus);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Calendar.this, test.class);
                startActivity(intent);
            }
        });



        // initializing database
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //뷰에 있는 위젯들 리턴받기기

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        List<EventDay> events = new ArrayList<> ();

        // 오늘 날짜 받게하기
        java.util.Calendar today= java.util.Calendar.getInstance();
        int todayYear=today.get(java.util.Calendar.YEAR);
        int todayMonth=today.get(java.util.Calendar.MONTH);
        int todayDay=today.get(java.util.Calendar.DAY_OF_MONTH);

        // 현재 선택한 날짜
        checkYear = todayYear;
        checkMonth = todayMonth;
        checkDay = todayDay;
        fragmentSchedule.update(checkYear,checkMonth,checkDay);

      // 첫시작 할 때 일정이 있으면 캘린더에 dot(새싹)으로 표시해주기
        mFirebaseDatabase.getReference().child("schedule").child(user.getUid()).addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //일정데이터가 변경될 때 onDataChange함수 발생
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    int[] date = splitDate(key);
                    java.util.Calendar event_calendar = java.util.Calendar.getInstance();
                    event_calendar.set(date[0], date[1]-1, date[2]);
                    EventDay event = new EventDay(event_calendar, R.drawable.ic_sprout);
                    events.add(event);
                }
                calendarView.setEvents(events);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



        // 선택 날짜가 변경될 때 호출되는 리스너
        calendarView.setOnDayClickListener(new OnDayClickListener () {
            @Override
            public void onDayClick(EventDay eventDay) {
                java.util.Calendar clickedDayCalendar = eventDay.getCalendar();

                //체크한 날짜 변경
                checkYear = clickedDayCalendar.get(java.util.Calendar.YEAR);
                checkMonth = clickedDayCalendar.get(java.util.Calendar.MONTH);
                checkDay = clickedDayCalendar.get(java.util.Calendar.DATE);


                fragmentSchedule.update(checkYear,checkMonth,checkDay);
                test2.setText(String.valueOf(checkDay));



            }
        });

        //select bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.page_diary:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentDiary).commit();

                        break;
                    case R.id.page_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentAccount).commit();

                        break;
                    case R.id.page_schedule:
                        fragmentSchedule.update(checkYear,checkMonth,checkDay);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentSchedule).commit();

                        break;

                }
                return true;
            }
        });


    }



    public static class FragmentSchedule extends Fragment {

        private static TextView list_empty;


        Context context;

        public DatabaseReference databaseReference;


        //firebase auth object
        public FirebaseAuth firebaseAuth;

        //firebase data object
        public DatabaseReference mDatabaseReference; // 데이터베이스의 주소를 저장합니다.
        public FirebaseDatabase mFirebaseDatabase; // 데이터베이스에 접근할 수 있는 진입점 클래스입니다.
        public FirebaseUser user;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_schedule, container, false);
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            mFirebaseDatabase = FirebaseDatabase.getInstance();



            initUI(rootView);

            return rootView;
        }

        private void initUI(ViewGroup rootView) {

            recyclerView = rootView.findViewById(R.id.scheduleRecyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
           list_empty = rootView.findViewById(R.id.list_empty);
            recyclerView.setEmptyView(list_empty);
        }

         static void update(int year, int month, int day){
            showDB(year,month,day);



        }




    }

    public static class  FragmentAccount extends Fragment {

        RecyclerView recyclerView;
        AccountAdapter adapter;
        Context context;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);

            initUI(rootView);

            return rootView;
        }

        private void initUI(ViewGroup rootView) {
            recyclerView = rootView.findViewById(R.id.accountRecyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), 1));
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            adapter = new AccountAdapter();



            recyclerView.setAdapter(adapter);
        }
    }


    public static class  FragmentDiary extends Fragment {

        RecyclerView recyclerView;
        DiaryAdapter adapter;
        Context context;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_diary, container, false);

            initUI(rootView);

            return rootView;
        }

        private void initUI(ViewGroup rootView) {
            recyclerView = rootView.findViewById(R.id.diaryRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);

            adapter = new DiaryAdapter();


            recyclerView.setAdapter(adapter);
        }
    }



    public static void showDB(int year, int month, int day) {

        mDatabaseReference = mFirebaseDatabase.getReference().child("schedule").child(user.getUid()).child(year+"-"+(month+1)+"-"+day);
        if (mDatabaseReference != null) {
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // firebase 데이터베이스의 데이터를 받아오는 곳
                    if(data!=null)
                        data.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄

                        data.add(snapshot.getValue(ScheduleData.class));
                    }

                        adapter.items=data;
                        adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 디비를 가져오던 중 에러 발생 시
                    Log.e("MainActivity", String.valueOf(error.toException())); // 에러 출력
                }
            });


    }}

  /*  private static String[] splitData(String data) {
        String[] splitText = data.split("/");
        return splitText;
    }*/

  private int[] splitDate(String date){
      String[] splitText = date.split("-");
      int[] result_date = {Integer.parseInt(splitText[0]), Integer.parseInt(splitText[1]), Integer.parseInt(splitText[2])};
      return result_date;
  }
}