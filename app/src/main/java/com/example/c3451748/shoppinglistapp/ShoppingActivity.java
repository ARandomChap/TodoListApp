package com.example.c3451748.shoppinglistapp;

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

public class ShoppingActivity extends AppCompatActivity implements ShoppingFragment.TodoFragmentAction{

    public static final String EXTRA_TODO_ID = "todo_id";

    public static Intent newIntent(Context packageContext, UUID todoId) {
        Intent intent = new Intent(packageContext, ShoppingActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    /*
    To decouple the fragment and make it reusable, the ShoppingFragment has a newInstance method
    that receives a todoId and returns the fragment
     */
    protected Fragment createFragment(){
//      getIntent() used to pass data to new activity.
//      getSerializableExtra used to get serialized data from EXTRA_TODO_ID.
//      Which is the value of item that was added with putExtra()^^^
        UUID todoId = (UUID) getIntent().getSerializableExtra(EXTRA_TODO_ID);
        
//      To create a new fragment, call ShoppingFragment.newInstance(UUID)
//      pass in the UUID retrieved from "extra argument":
        return ShoppingFragment.newInstance(todoId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){

            Fragment todoFragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container, todoFragment)
                    .commit();
        }
    }

    public void onDeleted(){
        onBackPressed();
    }

}
