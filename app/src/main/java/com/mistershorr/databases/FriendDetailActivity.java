package com.mistershorr.databases;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import static java.lang.Double.parseDouble;

public class FriendDetailActivity extends AppCompatActivity {

    private TextView textViewName;
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
    private ActionBar actionBar;

    public static final String TAG = FriendDetailActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);

        wireWidgets();

//        Intent lastIntent = getIntent();
//        final Friend friend = lastIntent.getParcelableExtra(FriendListActivity.EXTRA_FRIEND);
        friend = getIntent().getParcelableExtra(FriendListActivity.EXTRA_FRIEND);
        Log.d(TAG, "onCreate: intent received");

        seekBarChangeGymFrequency.setMin(0);
        seekBarChangeGymFrequency.setMax(14);
        seekBarChangeClumsiness.setMin(1);
        seekBarChangeClumsiness.setMax(5);
        if(friend != null) {
            Log.d(TAG, "onCreate: friend is NOT null");
            setContact();
        }
        else {
            Log.d(TAG, "onCreate: friend is null");
            friend = new Friend();
        }

        actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListeners();
    }

    private void setContact() {
        editTextName.setText(friend.getName());
        seekBarChangeGymFrequency.setProgress(friend.getGymFrequency());
        textViewGymFrequencyValue.setText("" + (((double)(seekBarChangeGymFrequency.getProgress())) / 2));
        if(friend.isAwesome()) {
            switchAwesome.setText("Awesome: Yes");
        }
        else {
            switchAwesome.setText("Awesome: No");
        }
        seekBarChangeClumsiness.setProgress(friend.getClumsiness());
        textViewClumsinessValue.setText("" + (seekBarChangeClumsiness.getProgress()));
        ratingBarChangeTrustworthiness.setRating(friend.getTrustworthiness());
        editTextChangeMoneyOwed.setText("$" + friend.getMoneyOwed());
    }

    private void wireWidgets() {
        textViewName = findViewById(R.id.textView_friendItem_name);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // TODO CRUD --> Contextual Menu (see documentation; long click) with a popup option that deletes from LOCAL LIST and DATABASE

    private void setListeners() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friend != null) {
                    if(TextUtils.isEmpty(editTextName.getText().toString())) {
                        Toast.makeText(FriendDetailActivity.this, "enter name", Toast.LENGTH_SHORT).show();
                    }
                    else if(TextUtils.isEmpty(editTextChangeMoneyOwed.getText().toString())) {
                        Toast.makeText(FriendDetailActivity.this, "enter money owed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d(TAG, "onClick: friend name is" + editTextName.getText().toString());
                        friend.setName(editTextName.getText().toString());
                        Log.d(TAG, "onClick: setName() success");
                        friend.setGymFrequency(seekBarChangeGymFrequency.getProgress());
                        Log.d(TAG, "onClick: setGymFrequency() success");
                        friend.setAwesome(switchAwesome.isChecked());
                        Log.d(TAG, "onClick: setAwesome() success");
                        friend.setClumsiness(seekBarChangeClumsiness.getProgress());
                        Log.d(TAG, "onClick: setClumsiness() success");
                        friend.setTrustworthiness((int)ratingBarChangeTrustworthiness.getRating());
                        Log.d(TAG, "onClick: setTrustworthiness() success");
                        if(editTextChangeMoneyOwed.getText().toString().substring(0, 1).equals("$")) {
                            friend.setMoneyOwed(parseDouble(editTextChangeMoneyOwed.getText().toString().substring(1)));
                        }
                        else {
                            friend.setMoneyOwed(parseDouble(editTextChangeMoneyOwed.getText().toString()));
                        }
                        Log.d(TAG, "onClick: setMoneyOwed() success");
                        updateContact();
                        Log.d(TAG, "onClick: updateContact() success");
                        Intent targetIntent = new Intent(FriendDetailActivity.this, FriendListActivity.class);
                        startActivity(targetIntent);
                        finish();
                    }
                }
            }
        });

        seekBarChangeGymFrequency.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textViewGymFrequencyValue.setText("" + (((double) (progress)) / 2));
                textViewGymFrequencyValue.setX(seekBar.getX() + (int) value + seekBarChangeGymFrequency.getThumbOffset() / 2);
                updateContact();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        switchAwesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchAwesome.isChecked()) {
                    switchAwesome.setText("Awesome");
                } else {
                    switchAwesome.setText("Not Awesome");
                }
                updateContact();
            }
        });

        seekBarChangeClumsiness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
//                textViewClumsinessValue.setText("" + progress);
                textViewClumsinessValue.setText("" + progress);
                textViewClumsinessValue.setX(seekBar.getX() + value + seekBarChangeClumsiness.getThumbOffset() / 2);
                updateContact();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ratingBarChangeTrustworthiness.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBarChangeTrustworthiness.setRating(rating);
                updateContact();
            }
        });
    }

    public void updateContact()
    {
        // Create a contact object first. This way (for the sake of the example)
        // there will be a saved object which will be updated after it is created.

        Backendless.Persistence.save( friend, new AsyncCallback<Friend>() {
            public void handleResponse( Friend savedFriend )
            {
                Toast.makeText(FriendDetailActivity.this, "friend successfully updated", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }
}
