package com.example.c3451748.todolistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {

    private RecyclerView mTodoRecyclerView;
    TodoAdapter mTodoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mTodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        mTodoRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()) );

        updateUI();

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_todo_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_todo:

                //new Todo is created and added to the arraylist through TodelModel
                Todo todo = new Todo();
                TodoModel.get(getActivity()).addTodo(todo);

                //Intent is created and passed to startActivity.
                //new Intent is called in TodoActivity, getActivity( THE ACTIVITY OF THE FRAGMENT ) is used as Context, todoId is used as UUID.
                Intent intent = TodoActivity.newIntent(getActivity(), todo.getId());
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    private void updateUI(){

        ArrayList todos = new ArrayList<>();
        TodoModel todoModel = TodoModel.get(getContext());
        todos = todoModel.getTodos();

        if (mTodoAdapter == null) {
            mTodoAdapter = new TodoAdapter(todos);
            mTodoRecyclerView.setAdapter(mTodoAdapter);
        } else {
            mTodoAdapter.notifyDataSetChanged();
        }

    }

    public class TodoHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Todo mTodo;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private TextView mTextTick;

//      takes in the id of a layout and returns a view
        public TodoHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_todo, parent, false));

            itemView.setOnClickListener(this);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.todo_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.todo_date);
            mTextTick = (TextView) itemView.findViewById(R.id.todo_tick);

        }

        @Override
        public void onClick(View view) {
            // have a Toast for now
            Toast.makeText(
                    getActivity(),
                    mTodo.getTitle() + " clicked",
                    Toast.LENGTH_SHORT)
                    .show();

            Intent intent = TodoPagerActivity.newIntent(getActivity(), mTodo.getId());
            startActivity(intent);

        }

        public void bind(Todo todo){
            mTodo = todo;
            mTextViewTitle.setText(mTodo.getTitle());
            mTextViewDate.setText(mTodo.getDate().toString());
            if (todo.isComplete()){
                mTextTick.setText("\u2713");
                mTextTick.setTextSize(22);
            }
            else{mTextTick.setText("");}
        }

    }

//  Adapter: A subclass of RecyclerView.Adapter responsible for providing views that represent items in a data set.
//  gets the data from the data source and passes it to the recycle view
    public class TodoAdapter extends RecyclerView.Adapter<TodoListFragment.TodoHolder> {

        private List<Todo> mTodos;

        public TodoAdapter(List<Todo> todos) {
            mTodos = todos;
        }

        @Override
        public TodoListFragment.TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TodoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TodoHolder holder, int position) {
            Todo todo = mTodos.get(position);
            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return mTodos.size();
        }

    }
}