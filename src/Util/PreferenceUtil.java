package Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by juliancurrie on 6/8/14.
 */
public class PreferenceUtil {

    private static final String DEFAULT_PREFS = "default_prefs";
    private static final String CORRECT_ANSWER_KEY = "correct_answers";
    private static final String INCORRECT_ANSWER_KEY = "incorrect_answers";

    public static int getCorrectAnswers(Context context){
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_PREFS, 0);
        return preferences.getInt(CORRECT_ANSWER_KEY, 0);
    }

    public void setCorrectAnswers(Context context, int correctAnswers){
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_PREFS, 0).edit();
        editor.putInt(CORRECT_ANSWER_KEY, correctAnswers);
        editor.commit();
    }

    public static int getIncorrectAnswers(Context context){
        SharedPreferences preferences = context.getSharedPreferences(DEFAULT_PREFS, 0);
        return preferences.getInt(INCORRECT_ANSWER_KEY, 0);
    }

    public void setIncorrectAnswers(Context context, int incorrectAnswers){
        SharedPreferences.Editor editor = context.getSharedPreferences(DEFAULT_PREFS, 0).edit();
        editor.putInt(INCORRECT_ANSWER_KEY, incorrectAnswers);
        editor.commit();
    }
}
