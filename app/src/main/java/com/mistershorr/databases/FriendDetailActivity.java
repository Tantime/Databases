package com.mistershorr.databases;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import static java.lang.Double.parseDouble;

public class FriendDetailActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewGymFrequency;
    private SeekBar seekBarChangeGymFrequency;
    private TextView textViewGymFrequencyValue;
    private Switch switchAwesome;
    private TextView textViewClumsiness;
    private SeekBar seekBarChangeClumsiness;
    private TextView textViewClumsinessValue;
    private TextView textViewtrustworthiness;
    private RatingBar ratingBarChangeTrustworthiness;
    private TextView textViewMoneyOwed;
    private EditText editTextChangeMoneyOwed;
    private Button buttonSave;
    private Friend friend;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        wireWidgets();

//        Intent lastIntent = getIntent();
//        final Friend friend = lastIntent.getParcelableExtra(FriendListActivity.EXTRA_FRIEND);
        friend = getIntent().getParcelableExtra(FriendListActivity.EXTRA_FRIEND);

        editTextName.setText("Name: " + friend.getName());
        seekBarChangeGymFrequency.setMin(0);
        seekBarChangeGymFrequency.setMax(14);
        seekBarChangeGymFrequency.setProgress(friend.getGymFrequency());
        textViewGymFrequencyValue.setText("" + (((double)(seekBarChangeGymFrequency.getProgress())) / 2));
        if(friend.isAwesome()) {
            switchAwesome.setText("Awesome: Yes");
        }
        else {
            switchAwesome.setText("Awesome: No");
        }
        seekBarChangeClumsiness.setMin(1);
        seekBarChangeClumsiness.setMax(5);
        seekBarChangeClumsiness.setProgress(friend.getClumsiness());
        textViewClumsinessValue.setText("" + (seekBarChangeClumsiness.getProgress()));
        ratingBarChangeTrustworthiness.setNumStars(friend.getTrustworthiness());
        editTextChangeMoneyOwed.setText("$" + friend.getMoneyOwed());

        setListeners();
    }

    private void wireWidgets() {
        editTextName = findViewById(R.id.editText_friendDetail_name);
        textViewGymFrequency = findViewById(R.id.textView_friendDetail_gymFrequency);
        seekBarChangeGymFrequency = findViewById(R.id.seekBar_friendDetail_changeGymFrequency);
        textViewGymFrequencyValue = findViewById(R.id.textView_friendDetail_gymFrequencyValue);
        switchAwesome = findViewById(R.id.switch_friendDetail_awesome);
        textViewClumsiness = findViewById(R.id.textView_friendDetail_clumsiness);
        seekBarChangeClumsiness = findViewById(R.id.seekBar_friendDetail_changeClumsiness);
        textViewClumsinessValue = findViewById(R.id.textView_friendDetail_clumsinessValue);
        textViewtrustworthiness = findViewById(R.id.textView_friendDetail_trustworthiness);
        ratingBarChangeTrustworthiness = findViewById(R.id.ratingBar_friendDetail_changeTrustworthiness);
        textViewMoneyOwed = findViewById(R.id.textView_friendDetail_moneyOwed);
        editTextChangeMoneyOwed = findViewById(R.id.editText_friendDetail_changeMoneyOwed);
        buttonSave = findViewById(R.id.button_friendDetail_save);
    }

    private void setListeners() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend.setName(editTextName.getText().toString());
                friend.setGymFrequency(seekBarChangeGymFrequency.getProgress());
//                friend.setAwesome(switchAwesome.get);
                friend.setClumsiness(seekBarChangeClumsiness.getProgress());
                friend.setTrustworthiness(ratingBarChangeTrustworthiness.getNumStars());
                friend.setMoneyOwed(parseDouble(editTextChangeMoneyOwed.getText().toString().substring(1)));
            }
        });

        seekBarChangeGymFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewGymFrequencyValue.setText("" + (((double)(progress)) / 2));
                textViewGymFrequencyValue.setX(seekBar.getX() + (int)value + seekBarChangeGymFrequency.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarChangeClumsiness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
//                textViewClumsinessValue.setText("" + progress);
                textViewClumsinessValue.setText("" + progress);
                textViewClumsinessValue.setX(seekBar.getX() + value + seekBarChangeClumsiness.getThumbOffset() / 2);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
