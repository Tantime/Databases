package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private ListView list;
    private List<Friend> friendsList;
    FriendAdapter friendAdapter;
    FloatingActionButton floatingActionButtonNewFriend;
//    ActionBar actionBar = getActionBar();

    public static final String EXTRA_FRIEND = "friend";

    public static final String TAG = FriendListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        wireWidgets();

        // search only for friends that have ownerIds that match the user's objectId
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

        setListeners();
        registerForContextMenu(list);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friendlist_delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_friendlist_delete:
                deleteContact(friendAdapter.friendsList.remove(info.position));
                friendAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_friendlist_sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                Intent targetIntent = new Intent(FriendListActivity.this, LoginActivity.class);
                startActivity(targetIntent);
                finish();
            case R.id.action_friendlist_sort_by_money_owed:
                SortByMoneyOwed();
                friendAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_friendlist_sort_by_name:
                SortByName();
                friendAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SortByMoneyOwed() {
        // 1. extract the list from the adapter -> friendAdapter.friendlist
        Collections.sort(friendAdapter.friendsList, new Comparator<Friend>() {
            @Override
            public int compare(Friend friend, Friend t1) {
                // negative number if thing comes before t1
                // 0 if thing and t1 are the same
                // positive number if thing comes after t1
                return (int)(t1.getMoneyOwed() - friend.getMoneyOwed());
            }
        });
        // the data in the adapter has changed, but it isn't aware
        // call the method notifyDataSetChanged on the adapter.

        Toast.makeText(this, "Sort by money owed clicked", Toast.LENGTH_SHORT);
    }

    private void SortByName() {
        // 1. extract the list from the adapter -> friendAdapter.friendlist
        Collections.sort(friendAdapter.friendsList, new Comparator<Friend>() {
            @Override
            public int compare(Friend friend, Friend t1) {
                return friend.getName().toLowerCase()
                        .compareTo(t1.getName().toLowerCase());
            }
        });
        // the data in the adapter has changed, but it isn't aware
        // call the method notifyDataSetChanged on the adapter.

        Toast.makeText(this, "sorted by name", Toast.LENGTH_SHORT);
    }

    private void wireWidgets() {
        list = findViewById(R.id.listView_friendlist_list);
        floatingActionButtonNewFriend = findViewById(R.id.floatingActionButton_friendList_addFriend);
    }

    private void setListeners() {
        floatingActionButtonNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetIntent = new Intent(FriendListActivity.this, FriendDetailActivity.class);
                startActivity(targetIntent);
            }
        });
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void deleteContact(Friend friend) {
        // put the initApp call somewhere early on in your app, perhaps main activity

        // create a new object, so there is something to delete

        Backendless.Persistence.of( Friend.class ).remove( friend, new AsyncCallback<Long>()
        {
            public void handleResponse( Long response )
            {
                // Contact has been deleted. The response is the
                // time in milliseconds when the object was deleted
            }
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be
                // retrieved with fault.getCode()
            }
        } );
    }

    public void logout() {
        Backendless.UserService.logout( new AsyncCallback<Void>()
        {
            public void handleResponse( Void response )
            {
                // user has been logged out.
            }

            public void handleFault( BackendlessFault fault )
            {
                // something went wrong and logout failed, to get the error code call fault.getCode()
            }
        });
    }
}
