package com.example.dongbanja.profile;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dongbanja.R;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // layout xml 과 자바파일을 연결

        /*String name = ProfileReviseActivity.name;
        Toast toast = Toast.makeText(getApplicationContext(),
                name, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

         */
        ProfileReviseActivity Info = new ProfileReviseActivity();
        TextView name1 = (TextView) findViewById(R.id.name) ;
        name1.setText(Info.name13);



        Button c = (Button)findViewById(R.id.revise_button);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getApplicationContext(), // 현재 화면의 제어권자
                        ProfileReviseActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                finish();
            }
        });
        } // end onCreate()
} // end MyTwo