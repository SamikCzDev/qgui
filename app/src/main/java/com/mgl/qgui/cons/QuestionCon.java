package com.mgl.qgui.cons;

import java.util.List;

public class QuestionCon {
    private int qId;

    private int level;
    private String otazka;
    private List<String> answers;

    public QuestionCon(int qId, int level, String otazka, List<String> answers) {
        this.qId = qId;
        this.level = level;
        this.otazka = otazka;
        this.answers = answers;
    }

    public int getqId() {
        return qId;
    }

    public int getLevel() {
        return level;
    }

    public String getOtazka() {
        return otazka;
    }

    public List<String> getAnswers() {
        return answers;
    }
}
