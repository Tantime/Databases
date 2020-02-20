package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private ListView list;
    private List<Friend> friendsList;
    // HeroAdapter heroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        Backendless.Data.of(Friend.class).find(new AsyncCallback<List<Friend>>(){
            @Override
            public void handleResponse(List<Friend> foundFriends)
            {
                // all Contact instances have been found
                Log.d("Loaded Friends", "handleResponse: " + foundFriends.toString());
                // TODO make a custom adapter to display the friends and load the list that is retrived into that adapter

                // TODO make Friend parcelable
                // TODO when a friend is clicked, it opens the detail activity and loads the info
            }
            @Override
            public void handleFault(BackendlessFault fault)
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }
}
