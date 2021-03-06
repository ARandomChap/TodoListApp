package com.example.c3451748.todolistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.content.Context;
import java.util.ArrayList;
import java.util.UUID;

public class TodoModel {

    private static TodoModel sTodoModel;

    private ArrayList<Todo> mTodoList;

    public static TodoModel get(Context context) {
        if (sTodoModel == null) {
            sTodoModel = new TodoModel(context);
        }
        return sTodoModel;
    }

    private TodoModel(Context context){
        mTodoList = new ArrayList<>();

        // refactor to pattern for data plugins
        // simulate some data for testing

        for (int i=0; i < 5; i++){
            Todo todo = new Todo();
            todo.setTitle("Todo item " + i);
            todo.setDetail("Detail for task " + i);
//                    + todo.getId().toString());
            todo.setComplete(false);

            if(i==3){
                todo.setComplete(true);
            }

            mTodoList.add(todo);
        }

    }

    public Todo getTodo(UUID todoId) {

        for (Todo todo : mTodoList) {
            if (todo.getId().equals(todoId)){
                return todo;
            }
        }

        return null;
    }

    public ArrayList<Todo> getTodos() {

        return mTodoList;

    }

    public void addTodo(Todo todo){

        mTodoList.add(todo);

    }

    public void deleteTodo(Todo todo){
        mTodoList.remove(todo);
    }

}