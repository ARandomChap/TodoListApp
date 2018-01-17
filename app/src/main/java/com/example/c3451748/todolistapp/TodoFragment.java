package com.example.c3451748.todolistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class TodoFragment extends Fragment {

    private static final String ARG_TODO_ID = "todo_id";

    private Todo mTodo;
    private EditText mEditTextTitle;
    private EditText mEditTextDetail;
    private Button mButtonDate;
    private CheckBox mCheckBoxIsComplete;

    TodoFragmentAction mCallback;

    public interface TodoFragmentAction{
        void onDeleted();
    }

    /*
    Rather than the calling the constructor directly, Activity(s) should call newInstance
    and pass required parameters that the fragment needs to create its arguments.
     */
    public static TodoFragment newInstance(UUID todoId) {
        
//      putSerializable - Inserts a Serializable value into the mapping of this Bundle, 
//      replacing any existing value for the given key
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todoId);

//      Creates a new fragment, and set the arguments for the fragment.
        TodoFragment fragment = new TodoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        /*
         Fragment accessing the intent from the hosting Activity as in the following code snippet
         allows for simple code that works.

        UUID todoId = (UUID) getActivity()
                .getIntent().getSerializableExtra(TodoActivity.EXTRA_TODO_ID);

         The disadvantage: TodoFragment is no longer reusable as it is coupled to Activities whoes
         intent has to contain the todoId.

         Solution: store the todoId in the fragment's arguments bundle.
            See the TodoFragment newInstance(UUID todoId) method.

         Then to create a new fragment, the TodoActivity should call TodoFragment.newInstance(UUID)
         and pass in the UUID it retrieves from its extra argument.

        */

        UUID todoId = (UUID) getArguments().getSerializable(ARG_TODO_ID);

        mTodo = TodoModel.get(getActivity()).getTodo(todoId);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        mEditTextTitle = (EditText) view.findViewById(R.id.todo_title);
        mEditTextTitle.setText(mTodo.getTitle());
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This line is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTodo.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This line is intentionally left blank
            }
        });

        mEditTextDetail = (EditText) view.findViewById(R.id.todo_detail);
        mEditTextDetail.setText(mTodo.getDetail());
        mEditTextDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This line is intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTodo.setDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This line is intentionally left blank
            }
        });

//        mButtonDate = (Button) view.findViewById(R.id.todo_date);
//        mButtonDate.setText(mTodo.getDate().toString());
//        mButtonDate.setEnabled(false);

        mCheckBoxIsComplete = (CheckBox) view.findViewById(R.id.todo_complete);
        mCheckBoxIsComplete.setChecked(mTodo.isComplete());
        mCheckBoxIsComplete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d("DEBUG **** TodoFragment","called onCheckedChanged");
                mTodo.setComplete(isChecked);
            }
        });

        return view;

    }

//  This is needed in order to create the callback to onBackPressed automatically when delete is pressed.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (TodoFragmentAction) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TodoFragmentAction");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_todo_search, menu);
        inflater.inflate(R.menu.fragment_todo_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:

                TodoModel.get(getActivity()).deleteTodo(mTodo);

                Toast.makeText(
                        getActivity(),
                        mTodo.getTitle() + " deleted",
                        Toast.LENGTH_SHORT)
                        .show();

//                getActivity().finish();

                mCallback.onDeleted();

                return true;

            case R.id.search:

                Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                search.putExtra(SearchManager.QUERY, mTodo.getTitle());
                startActivity(search);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}