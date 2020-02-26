package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private ListView list;
    private List<Friend> friendsList;
    FriendAdapter friendAdapter;

    public static final String EXTRA_FRIEND = "friend";

    public static final String TAG = FriendListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        wireWidgets();

        // search only for friends that have ownerIds tha match the user's objectId
        String userId = Backendless.UserService.CurrentUser().getObjectId();

        // ownerId = '12432155uohufsfijf'
        String whereClause = "ownerId = '" + userId + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of(Friend.class).find(queryBuilder, new AsyncCallback<List<Friend>>(){
            @Override
            public void handleResponse(final List<Friend> friendsList)
            {
                // all Contact instances have been found
                Log.d(TAG, "handleResponse: " + friendsList.toString());
                // TODO make a custom adapter to display the friends and load the list that is retrieved into that adapter
                friendAdapter = new FriendAdapter(friendsList);
                list.setAdapter(friendAdapter);

                // TODO make Friend parcelable
                // TODO when a friend is clicked, it opens the detail activity and loads the info

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent targetIntent = new Intent(FriendListActivity.this, FriendDetailActivity.class);
                        targetIntent.putExtra(EXTRA_FRIEND, friendsList.get(position));
                        startActivity(targetIntent);
                    }
                });

            }
            @Override
            public void handleFault(BackendlessFault fault)
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }

    private void wireWidgets() {
        list = findViewById(R.id.listView_friendlist_list);
    }

    private class FriendAdapter extends ArrayAdapter<Friend> {
        private List<Friend> friendsList;

        public FriendAdapter(List<Friend> friendsList) {
            super(FriendListActivity.this, -1, friendsList);
            this.friendsList = friendsList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 1. inflate a layout
            LayoutInflater inflater = getLayoutInflater();

            // check if convertview is null; if so, replace it
            if(convertView == null) {
                // R.layout.item_hero is a custom layout we make that represents
                // what a single item would look like in our listview
                convertView = inflater.inflate(R.layout.item_friend, parent, false);
            }

            //2. wire widgets and link the friend to those widgets
            TextView textViewName = convertView.findViewById(R.id.textView_friendItem_name);
            TextView textViewMoneyOwed = convertView.findViewById(R.id.textView_friendItem_moneyOwed);

            Friend friend = friendsList.get(position);

            textViewName.setText(String.valueOf(friend.getName()));
            textViewMoneyOwed.setText("Money Owed: " + friend.getMoneyOwed());

            // 3. return inflated view
            return convertView;
        }
    }
}
