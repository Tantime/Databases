package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    private Switch switchAwesome;
    private TextView textViewClumsiness;
    private SeekBar seekBarChangeClumsiness;
    private TextView textViewtrustworthiness;
    private RatingBar ratingBarChangeTrustworthiness;
    private TextView textViewMoneyOwed;
    private EditText editTextChangeMoneyOwed;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        wireWidgets();

        Intent lastIntent = getIntent();
        final Friend friend = lastIntent.getParcelableExtra(FriendListActivity.EXTRA_FRIEND);

        editTextName.setText("Name: " + friend.getName());
        seekBarChangeGymFrequency.setMax(10);
        seekBarChangeGymFrequency.setProgress(friend.getGymFrequency());
        if(friend.isAwesome()) {
            switchAwesome.setText("Awesome: Yes");
        }
        else {
            switchAwesome.setText("Awesome: No");
        }
//        seekBarChangeClumsiness.setMin(1);
        seekBarChangeClumsiness.setMax(10);
        seekBarChangeClumsiness.setProgress(friend.getClumsiness());
        ratingBarChangeTrustworthiness.setNumStars(friend.getTrustworthiness());
        editTextChangeMoneyOwed.setText("$" + friend.getMoneyOwed());

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
        buttonSave = findViewById(R.id.button_friendDetail_save);
    }
}
