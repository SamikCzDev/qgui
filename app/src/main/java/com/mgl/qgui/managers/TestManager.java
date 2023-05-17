package com.mgl.qgui.managers;

import android.graphics.Color;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mgl.qgui.MainActivity;
import com.mgl.qgui.R;
import com.mgl.qgui.cons.QuestionCon;
import com.mgl.qgui.req.SendToCheckTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestManager {
    private MainActivity mainActivity;

    private HashMap<Integer, QuestionCon> list;

    private int pocOtazekNow = 0;
    private int testId;

    private int currQID;

    private boolean status = false;

    private boolean training;

    public TestManager(MainActivity mainActivity, JSONObject currentTest, boolean training) throws JSONException {
        this.mainActivity = mainActivity;
        this.testId = currentTest.getInt("testID");
        this.training = training;

        JSONArray jsonArray = currentTest.getJSONArray("questionList");

        HashMap<Integer, QuestionCon> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String otazka = jsonObject.getString("question");
            int level = jsonObject.getInt("level");
            JSONArray answersArray = jsonObject.getJSONArray("answers");

            List<String> answersList = new ArrayList<>();
            for (int j = 0; j < answersArray.length(); j++) {
                String answer = answersArray.getString(j);
                answersList.add(answer);
            }

            map.put(i, new QuestionCon(id,level,otazka,answersList));
        }
        list = map;

        this.start();
    }

    public void start() {
        mainActivity.setContentView(R.layout.activity_test);

        pocOtazekNow = 1;

        QuestionCon question = list.get(0);

        currQID = question.getqId();
        System.out.println(mainActivity);
        ((TextView)mainActivity.findViewById(R.id.question_count)).setText("Otázka: " + pocOtazekNow + "/10");

        ((TextView)mainActivity.findViewById(R.id.question_text)).setText(question.getOtazka());

        ((TextView)mainActivity.findViewById(R.id.q_level)).setText("Level: " + question.getLevel());

        ((Button) mainActivity.findViewById(R.id.back_pause_button2)).setText("Potvrdit");

        ((Button)mainActivity.findViewById(R.id.option1)).setText(question.getAnswers().get(0));
        ((Button)mainActivity.findViewById(R.id.option2)).setText(question.getAnswers().get(1));
        ((Button)mainActivity.findViewById(R.id.option3)).setText(question.getAnswers().get(2));
        ((Button)mainActivity.findViewById(R.id.option4)).setText(question.getAnswers().get(3));
    }
    public void next() {

        if(((Button) mainActivity.findViewById(R.id.back_pause_button2)).getText().equals("Pokračovat")) {

            if(pocOtazekNow == 10) {
                mainActivity.setContentView(R.layout.activity_done);
                return;
            }

            RadioGroup selectedRadioButton = mainActivity.findViewById(R.id.answer_options);
            selectedRadioButton.clearCheck();

            pocOtazekNow++;

            QuestionCon question = list.get(pocOtazekNow-1);

            currQID = question.getqId();


            ((TextView)mainActivity.findViewById(R.id.question_count)).setText("Otázka: " + pocOtazekNow + "/10");

            ((TextView)mainActivity.findViewById(R.id.q_level)).setText("Level: " + question.getLevel());

            ((TextView)mainActivity.findViewById(R.id.question_text)).setText(question.getOtazka());
            ((TextView)mainActivity.findViewById(R.id.question_text)).setTextColor(Color.BLACK);

            ((Button) mainActivity.findViewById(R.id.back_pause_button2)).setText("Potvrdit");

            ((Button)mainActivity.findViewById(R.id.option1)).setText(question.getAnswers().get(0));
            ((Button)mainActivity.findViewById(R.id.option2)).setText(question.getAnswers().get(1));
            ((Button)mainActivity.findViewById(R.id.option3)).setText(question.getAnswers().get(2));
            ((Button)mainActivity.findViewById(R.id.option4)).setText(question.getAnswers().get(3));

        } else {
            RadioGroup selectedRadioButton = mainActivity.findViewById(R.id.answer_options);
            int selectedId = selectedRadioButton.getCheckedRadioButtonId();
            if(selectedId == -1) {
                return;
            }

            String selectedAnswer = ((RadioButton)mainActivity.findViewById(selectedRadioButton.getCheckedRadioButtonId())).getText().toString();
            SendToCheckTask sendToCheckTask = new SendToCheckTask(mainActivity);
            sendToCheckTask.execute(String.valueOf(currQID),String.valueOf(testId),selectedAnswer,String.valueOf(training),mainActivity.getCookies());


            ((Button) mainActivity.findViewById(R.id.back_pause_button2)).setText("Pokračovat");
        }
    }

}
