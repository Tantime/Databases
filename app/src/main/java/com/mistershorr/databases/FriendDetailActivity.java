package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class FriendDetailActivity extends AppCompatActivity {

    private EditText editTextName;
    private TextView textViewGymFrequency;
    private SeekBar seekBarChangeGymFrequency;
    private Switch switchAwesome;
    private TextView textViewClumsiness;
    private SeekBar seekBarChangeClumsiness;
    private TextView textViewtrustworthiness;
    private RatingBar ratingBarChangeTrustworthiness;
    private TextView textViewMoneyOwed;
    private EditText editTextChangeMoneyOwed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        wireWidgets();
        setListeners();

        Intent lastIntent = getIntent();
        Friend friend = lastIntent.getParcelableExtra(FriendListActivity.EXTRA_FRIEND);

        editTextName.setText("Name: " + friend.getName());
        seekBarChangeGymFrequency.setProgress(friend.getGymFrequency());
        if(friend.isAwesome()) {
            switchAwesome.setText("Awesome: Yes");
        }
        else {
            switchAwesome.setText("Awesome: No");
        }
        seekBarChangeClumsiness.setProgress(friend.getClumsiness());
        ratingBarChangeTrustworthiness.setNumStars(friend.getTrustworthiness());
        editTextChangeMoneyOwed.setText("Name: " + friend.getMoneyOwed());
    }

    private void wireWidgets() {
        editTextName = findViewById(R.id.editText_friendDetail_name);
        textViewGymFrequency = findViewById(R.id.textView_friendDetail_gymFrequency);
        seekBarChangeGymFrequency = findViewById(R.id.seekBar_friendDetail_changeGymFrequency);
        switchAwesome = findViewById(R.id.switch_friendDetail_awesome);
        textViewClumsiness = findViewById(R.id.textView_friendDetail_clumsiness);
        seekBarChangeClumsiness = findViewById(R.id.seekBar_friendDetail_changeClumsiness);
        textViewtrustworthiness = findViewById(R.id.textView_friendDetail_trustworthiness);
        ratingBarChangeTrustworthiness = findViewById(R.id.ratingBar_friendDetail_changeTrustworthiness);
        textViewMoneyOwed = findViewById(R.id.textView_friendDetail_moneyOwed);
        editTextChangeMoneyOwed = findViewById(R.id.editText_friendDetail_changeMoneyOwed);
    }

    private void setListeners() {

    }
}
