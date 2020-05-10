package com.example.bagrutTel.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.bagrutTel.R;
import com.example.bagrutTel.fragments.ChatListFragment;
import com.example.bagrutTel.fragments.GroupChatsFragment;
import com.example.bagrutTel.fragments.HomeFragment;
import com.example.bagrutTel.fragments.NotificationsFragment;
import com.example.bagrutTel.fragments.ProfileFragment;
import com.example.bagrutTel.fragments.UsersFragment;
import com.example.bagrutTel.notifications.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {

    //firebase auth
    FirebaseAuth firebaseAuth;


    ActionBar actionBar;

    String mUID;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //action bar and title
         actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.profile);

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction default onStart
        actionBar.setTitle(R.string.posts);//change action bar title
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();



        checkUsersStatus();



    }


    @Override
    protected void onResume() {
        checkUsersStatus();
        super.onResume();
    }

    public void updateToken(String token){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        ref.child(mUID).setValue(mToken);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //item clicks
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //home fragment transaction
                            actionBar.setTitle(R.string.posts);//change action bar title
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,fragment1,"");
                            ft1.commit();
                            return true;
                        case R.id.nav_profile:
                            //profile fragment transaction
                            actionBar.setTitle(R.string.profileUser);//change action bar title
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,fragment2,"");
                            ft2.commit();
                            return true;
                        case R.id.nav_users:
                            //user fragment transaction
                            actionBar.setTitle(R.string.usersFragment);//change action bar title
                            UsersFragment fragment3 = new UsersFragment();
                            FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,fragment3,"");
                            ft3.commit();
                            return true;
                        case R.id.nav_chat:
                            //user fragment transaction
                            actionBar.setTitle(R.string.chatsFragment);//change action bar title
                            ChatListFragment fragment4 = new ChatListFragment();
                            FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content,fragment4,"");
                            ft4.commit();
                            return true;
                        case R.id.nav_more:
                            showMoreOptions();
                            return true;
                    }
                    return false;
                }
            };

    private void showMoreOptions() {
        //pop menu to show more options
        PopupMenu popupMenu = new PopupMenu(this,navigationView, Gravity.END);
        //items to show in menu
        popupMenu.getMenu().add(Menu.NONE,0,0,R.string.notifications);
        popupMenu.getMenu().add(Menu.NONE,1,0,R.string.groupChats);

        //menu clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id==0){
                    //notification clicked

                    //notification fragment transaction
                    actionBar.setTitle(R.string.notifications);//change action bar title
                    NotificationsFragment fragment5 = new NotificationsFragment();
                    FragmentTransaction ft5=getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content,fragment5,"");
                    ft5.commit();
                }
                else if (id == 1){
                    //group chats clicked
                    //notification fragment transaction
                    actionBar.setTitle(R.string.groupChats);//change action bar title
                    GroupChatsFragment fragment6 = new GroupChatsFragment();
                    FragmentTransaction ft6=getSupportFragmentManager().beginTransaction();
                    ft6.replace(R.id.content,fragment6,"");
                    ft6.commit();
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void checkUsersStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed in stay here
            //set email of logged in user
            //mProfileTv.setText(user.getEmail());
            mUID = user.getUid();

            //save uid of currently signed in user in shared preferences
            SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();

            //update token
            updateToken(FirebaseInstanceId.getInstance().getToken());

        }
        else{
            //user not signed in,go to main activity
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        //check on start of app
        checkUsersStatus();
        super.onStart();
    }


}
