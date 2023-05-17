package com.mgl.qgui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mgl.qgui.managers.TestManager;
import com.mgl.qgui.req.GetTestRequestTask;
import com.mgl.qgui.req.LectureRequestTask;
import com.mgl.qgui.req.LoginRequestTask;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public String cookies;
    public JSONObject currentTest;
    private EditText m_edtUsername;
    private EditText m_edtPassword;
    private TestManager testManager;

    private int level = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m_edtUsername = findViewById(R.id.edit_username);
        m_edtPassword = findViewById(R.id.edit_password);
    }

    public void onLoginBtn(View view) {

        LoginRequestTask loginTask = new LoginRequestTask(this);
        loginTask.execute(m_edtUsername.getText().toString(), m_edtPassword.getText().toString());

    }
    public void onGenTest(View view) {
        GetTestRequestTask genTask = new GetTestRequestTask(this);
        genTask.execute(cookies);

    }

    public void onLecturesBtn(View view) {
        /*Intent intent = new Intent(MainActivity.this, QuizList.class);
        startActivity(intent);*/
        setContentView(R.layout.activity_lectures);
    }


    public void startLec(View view)  {
        LectureRequestTask lectureRequestTask = new LectureRequestTask(this);
        lectureRequestTask.execute(String.valueOf(level),cookies);
    }


    public void onL1(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Seznámení s IT");

        ((TextView)findViewById(R.id.contentTextView)).setText("V této kategorii najdete ty nejzákladnější\r\n úlohy jaké máme.");

        level = 1;
    }
    public void onL2(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("IT zkratky");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde najdete důležité zkratky, které musíte znát.");

        level = 2;
    }
    public void onL3(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Nebezpečí na internetu");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde zjistíte co všechno vás může ohrozit.");

        level = 3;
    }
    public void onL4(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Sítě");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde zjistíte všechno možné o počítačových síťích.");

        level = 4;
    }
    public void onL5(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Programování základy");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde zjistíte co je programování i něco víc.");

        level = 5;
    }
    public void onL6(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Otázky na RAID");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde najdete těžké otázky ohledně technologie RAID.");

        level = 6;
    }
    public void onL7(View view) {
        setContentView(R.layout.activity_lecture_info);

        ((TextView)findViewById(R.id.titleTextView)).setText("Seznámení s RAID");

        ((TextView)findViewById(R.id.contentTextView)).setText("Zde se seznámíte s RAIDem.");

        level = 7;
    }





    public void backToMenu(View view) {
        setContentView(R.layout.activity_menu);
    }

    public void setTestManager(TestManager testManager) {
        this.testManager = testManager;
    }

    public void nextButt(View view) {
        testManager.next();
    }
    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public void setCurrentTest(JSONObject currentTest) {
        this.currentTest = currentTest;
    }




    public String getCookies() {
        return cookies;
    }
}