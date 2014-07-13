package com.protege.fizzbuzz;

import java.util.Date;
import java.util.Random;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Util.PreferenceUtil;

public class GameFragment extends BaseFragment implements OnClickListener{
    TextView randomNumText;
    TextView confirmationText;
    TextView bothText;
    TextView winsCountText;
    TextView lossesCountText;
    ImageView fizzImage;
    ImageView buzzImage;
    int fizzBuzzValue;
    private static final String TAG = "GameFragment";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(activity.getResources().getString(R.string.randomnumber), randomNumText.getText().toString());
        outState.putString(activity.getResources().getString(R.string.confirmtext), confirmationText.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        randomNumText = (TextView)v.findViewById(R.id.random_number_text);
        confirmationText = (TextView)v.findViewById(R.id.confirmation_text);
        bothText = (TextView)v.findViewById(R.id.both_text);
        fizzImage = (ImageView)v.findViewById(R.id.fizz_image);
        buzzImage = (ImageView)v.findViewById(R.id.buzz_image);
        winsCountText = (TextView)v.findViewById(R.id.wins_text);
        lossesCountText = (TextView)v.findViewById(R.id.losses_text);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        randomNumText.setOnClickListener(this);
        confirmationText.setOnClickListener(this);
        fizzImage.setOnClickListener(this);
        buzzImage.setOnClickListener(this);
        bothText.setOnClickListener(this);

        if(savedInstanceState == null){
            animateNewNumber();
        }else{
            randomNumText.setText(savedInstanceState.getString(activity.getResources().getString(R.string.randomnumber)));
            confirmationText.setText(savedInstanceState.getString(activity.getResources().getString(R.string.confirmtext)));
        }

        winsCountText.setText(activity.getString(R.string.wins) + PreferenceUtil.getCorrectAnswers(activity));
        lossesCountText.setText(activity.getString(R.string.losses) + PreferenceUtil.getIncorrectAnswers(activity));
    }

    private static int calculateSpecificRandomNumber(int multiple){
        Random random = new Random();
        if(multiple > 0){
            return multiple * random.nextInt(FizzBuzzHelper.MAX_FIZZBUZZ_NUMBER / multiple) ;
        }else{
            throw new ArithmeticException();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.game_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_score:
                PreferenceUtil.setCorrectAnswers(activity, 0);
                PreferenceUtil.setIncorrectAnswers(activity, 0);
                winsCountText.setText(activity.getString(R.string.wins) + 0);
                lossesCountText.setText(activity.getString(R.string.losses) + 0);
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.random_number_text:
                animateNewNumber();
                break;
            case R.id.confirmation_text:
                break;
            case R.id.fizz_image:
                if(isCorrectAnswer(FizzBuzzHelper.FIZZ)){
                    animateFizz();
                    addToCorrectCount();
                } else{
                    addToIncorrectCount();
                }
                break;
            case R.id.buzz_image:
                if(isCorrectAnswer(FizzBuzzHelper.BUZZ)){
                    animateBuzz();
                    addToCorrectCount();
                } else{
                    addToIncorrectCount();
                }
                break;
            case R.id.both_text:
                if(isCorrectAnswer(FizzBuzzHelper.FIZZBUZZ)){
                    animateFizzBuzz();
                    animateBothText();
                    addToCorrectCount();
                } else{
                    addToIncorrectCount();
                }
                break;
        }
    }

    private boolean isCorrectAnswer(int expectedResult){
        int randomNumberText = 0;
        boolean isCorrect = false;

        try{
            randomNumberText = FizzBuzzHelper.calculateFizzBuzz(Integer.parseInt((String) randomNumText.getText()));
        }catch(Exception e){
            Toast.makeText(activity, activity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Error parsing result");
        }

        if(randomNumberText == expectedResult){
            confirmationText.setText(activity.getResources().getString(R.string.correct));
            confirmationText.setTextColor(Color.GREEN);
            isCorrect = true;
        }else{
            confirmationText.setText(activity.getResources().getString(R.string.wrong));
            confirmationText.setTextColor(Color.RED);
        }

        animateNewNumber();
        return isCorrect;
    }

    private void animateFizz(){
        ValueAnimator shakeAnim = ObjectAnimator.ofFloat(fizzImage, "translationY", 0f, 20f);
        shakeAnim.setDuration(100);
        shakeAnim.setRepeatCount(5);
        shakeAnim.start();
    }

    private void animateBuzz(){
        ValueAnimator rotateAnim = ObjectAnimator.ofFloat(buzzImage, "rotation", 0f, 360f);
        rotateAnim.setDuration(500);
        rotateAnim.start();
    }

    private void animateFizzBuzz(){
        animateFizz();
        animateBuzz();
    }

    private void animateBothText(){
        ValueAnimator rotateAnimX = ObjectAnimator.ofFloat(bothText, "scaleX", 0f, 0.9f);
        rotateAnimX.setDuration(700);
        rotateAnimX.start();

        ValueAnimator rotateAnimY = ObjectAnimator.ofFloat(bothText, "scaleY", 0f, 0.9f);
        rotateAnimY.setDuration(700);
        rotateAnimY.start();
    }

    /**
     * We only want to generate a number that's divisible by 3 or 5. Choosing which based on whether the current
     * millisecond date value is even or odd. No need to account for being divisible by both.
     */
    private void animateNewNumber(){
        ValueAnimator rotateAnim = ObjectAnimator.ofFloat(randomNumText, "rotationX", 0f, 360f);
        rotateAnim.setDuration(700);
        rotateAnim.start();
        rotateAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                try{
                    Date date = new Date();
                    if(date.getTime() % 2 == 0){
                        fizzBuzzValue = calculateSpecificRandomNumber(FizzBuzzHelper.DIVISIBLE_BY_3);
                    }else{
                        fizzBuzzValue = calculateSpecificRandomNumber(FizzBuzzHelper.DIVISIBLE_BY_5);
                    }
                    randomNumText.setText(fizzBuzzValue + "");
                }catch(Exception e){
                    Toast.makeText(activity, activity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void addToCorrectCount(){
        int previousCorrectAnswers = PreferenceUtil.getCorrectAnswers(activity);
        int currentCorrectAnswers = ++previousCorrectAnswers;
        PreferenceUtil.setCorrectAnswers(activity, currentCorrectAnswers);
        winsCountText.setText(activity.getString(R.string.wins) + currentCorrectAnswers);
    }

    private void addToIncorrectCount(){
        int previousIncorrectAnswers = PreferenceUtil.getIncorrectAnswers(activity);
        int currentIncorrectAnswers = ++previousIncorrectAnswers;
        PreferenceUtil.setIncorrectAnswers(activity, previousIncorrectAnswers++);
        lossesCountText.setText(activity.getString(R.string.losses) + currentIncorrectAnswers);
    }
}