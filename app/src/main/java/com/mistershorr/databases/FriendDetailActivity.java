package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class FriendDetailActivity extends AppCompatActivity {

    private EditText name;
    private TextView clumsiness;
    private SeekBar changeClumsiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
    }
}
