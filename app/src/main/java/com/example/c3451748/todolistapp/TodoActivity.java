package com.example.c3451748.todolistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class TodoActivity extends AppCompatActivity implements TodoFragment.TodoFragmentAction{

    public static final String EXTRA_TODO_ID = "todo_id";

//    when new item is created, intent is used to call TodoActivity
//    the activity of the fragment is used as context, and the todoId is used and UUID
//    new intent is created passing context and TodoAcitvity.class
    public static Intent newIntent(Context packageContext, UUID todoId) {
        Intent intent = new Intent(packageContext, TodoActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    /*
    To decouple the fragment and make it reusable, the TodoFragment has a newInstance method
    that receives a todoId and returns the fragment
     */
    protected Fragment createFragment(){
//      getIntent() used to pass data to new activity.
//      getSerializableExtra used to get serialized data from EXTRA_TODO_ID.
//      Which is the value of item that was added with putExtra()^^^
        UUID todoId = (UUID) getIntent().getSerializableExtra(EXTRA_TODO_ID);
        
//      To create a new fragment, call TodoFragment.newInstance(UUID)
//      pass in the UUID retrieved from "extra argument":
        return TodoFragment.newInstance(todoId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

//      returns the fragmentManager which interacts with the fragment associated with the activity.
//      checks if the a fragment exists, if not creates one.
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){

            Fragment todoFragment = createFragment();

//          FragmentTransaction allows for the manipulation of the fragment as long as the activity is running
            fm.beginTransaction()
                    .add(R.id.fragment_container, todoFragment)
                    .commit();
        }
    }

    public void onDeleted(){
        onBackPressed();
    }

}
