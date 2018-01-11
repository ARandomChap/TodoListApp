package com.example.c3451748.shoppinglistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.content.Context;
import java.util.ArrayList;
import java.util.UUID;

public class ShoppingModel {

    private static ShoppingModel sTodoModel;

    private ArrayList<Shopping> mTodoList;

    public static ShoppingModel get(Context context) {
        if (sTodoModel == null) {
            sTodoModel = new ShoppingModel(context);
        }
        return sTodoModel;
    }

    private ShoppingModel(Context context){
        mTodoList = new ArrayList<>();

        // refactor to pattern for data plugins
        // simulate some data for testing

        for (int i=0; i < 5; i++){
            Shopping todo = new Shopping();
            todo.setTitle("Shopping item " + i);
            todo.setDetail("Detail for task " + todo.getId().toString());
            todo.setComplete(false);

            mTodoList.add(todo);
        }

    }

    public Shopping getTodo(UUID todoId) {

        for (Shopping todo : mTodoList) {
            if (todo.getId().equals(todoId)){
                return todo;
            }
        }

        return null;
    }

    public ArrayList<Shopping> getTodos() {

        return mTodoList;

    }

    public void addTodo(Shopping todo){

        mTodoList.add(todo);

    }

    public void deleteTodo(Shopping todo){
        mTodoList.remove(todo);
    }

}