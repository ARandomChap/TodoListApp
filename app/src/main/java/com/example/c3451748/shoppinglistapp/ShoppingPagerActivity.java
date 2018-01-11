package com.example.c3451748.shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class ShoppingPagerActivity extends AppCompatActivity implements ShoppingFragment.TodoFragmentAction{
    private static final String EXTRA_TODO_ID = "todo_id";

    private ViewPager mViewPager;
    private List<Shopping> mTodos;

    public static Intent newIntent(Context packageContext, UUID todoId){
        Intent intent = new Intent(packageContext, ShoppingPagerActivity.class);
        intent.putExtra(EXTRA_TODO_ID, todoId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_pager);

        UUID todoId = (UUID) getIntent().getSerializableExtra(EXTRA_TODO_ID);
        mViewPager = (ViewPager) findViewById(R.id.todo_view_pager);
        mTodos = ShoppingModel.get(this).getTodos();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Shopping todo = mTodos.get(position);
                return ShoppingFragment.newInstance(todo.getId());
            }

            @Override
            public int getCount() {
                return mTodos.size();
            }
        });

        for (int i = 0; i < mTodos.size(); i++){
            if (mTodos.get(i).getId().equals(todoId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public void onDeleted(){
        onBackPressed();
    }
}