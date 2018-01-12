package com.example.c3451748.shoppinglistapp;

/**
 * Created by c3451748 on 17/11/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShoppingFragment extends Fragment {

    private static final String ARG_TODO_ID = "todo_id";

    private Shopping mTodo;
    private EditText mEditTextTitle;
    private EditText mEditTextDetail;
    private Button mButtonDate;
    private CheckBox mCheckBoxIsComplete;

    private RecyclerView mTodoRecyclerView;
    TodoAdapter mTodoAdapter;

    TodoFragmentAction mCallback;

    public interface TodoFragmentAction{
        void onDeleted();
    }

    /*
    Rather than the calling the constructor directly, Activity(s) should call newInstance
    and pass required parameters that the fragment needs to create its arguments.
     */
    public static ShoppingFragment newInstance(UUID todoId) {
        
//      putSerializable - Inserts a Serializable value into the mapping of this Bundle, 
//      replacing any existing value for the given key
        Bundle args = new Bundle();
        args.putSerializable(ARG_TODO_ID, todoId);

//      Creates a new fragment, and set the arguments for the fragment.
        ShoppingFragment fragment = new ShoppingFragment();
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
                .getIntent().getSerializableExtra(ShoppingActivity.EXTRA_TODO_ID);

         The disadvantage: ShoppingFragment is no longer reusable as it is coupled to Activities whoes
         intent has to contain the todoId.

         Solution: store the todoId in the fragment's arguments bundle.
            See the ShoppingFragment newInstance(UUID todoId) method.

         Then to create a new fragment, the ShoppingActivity should call ShoppingFragment.newInstance(UUID)
         and pass in the UUID it retrieves from its extra argument.

        */

        UUID todoId = (UUID) getArguments().getSerializable(ARG_TODO_ID);

        mTodo = ShoppingModel.get(getActivity()).getTodo(todoId);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
//        view = inflater.inflate(R.layout.fragment_shopping, container, false);
        mTodoRecyclerView = (RecyclerView) view.findViewById(R.id.todo_recycler_view);
        mTodoRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()) );

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

//        mButtonDate = (Button) view.findViewById(R.id.todo_date);
//        mButtonDate.setText(mTodo.getDate().toString());
//        mButtonDate.setEnabled(false);

        mCheckBoxIsComplete = (CheckBox) view.findViewById(R.id.todo_complete);
        mCheckBoxIsComplete.setChecked(mTodo.isComplete());
        mCheckBoxIsComplete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d("DEBUG **** ShoppingFragment","called onCheckedChanged");
                mTodo.setComplete(isChecked);
            }
        });


//        updateUI();

        return view;

    }

    private void  updateUI(){

        ArrayList todos = new ArrayList<>();
        ShoppingModel todoModel = ShoppingModel.get(getContext());
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

        private Shopping mTodo;
        private TextView mTextViewTitle;
        private TextView mTextViewDate;

        public TodoHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_item_shopping, parent, false));

            itemView.setOnClickListener(this);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.todo_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.todo_date);

        }

        @Override
        public void onClick(View view) {
            // have a Toast for now
            Toast.makeText(
                    getActivity(),
                    mTodo.getTitle() + " clicked",
                    Toast.LENGTH_SHORT)
                    .show();

            Intent intent = ShoppingPagerActivity.newIntent(getActivity(), mTodo.getId());
            startActivity(intent);
        }

        public void bind(Shopping todo) {
            mTodo = todo;
            mTextViewTitle.setText(mTodo.getTitle());
            mTextViewDate.setText(mTodo.getDate().toString());
        }
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
        inflater.inflate(R.menu.fragment_shopping_delete, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:

                ShoppingModel.get(getActivity()).deleteTodo(mTodo);

                Toast.makeText(
                        getActivity(),
                        mTodo.getTitle() + " deleted",
                        Toast.LENGTH_SHORT)
                        .show();

                mCallback.onDeleted();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class TodoAdapter extends RecyclerView.Adapter<ShoppingFragment.TodoHolder> {

        private List<Shopping> mTodos;

        public TodoAdapter(List<Shopping> todos) {
            mTodos = todos;
        }

        @Override
        public ShoppingFragment.TodoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TodoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ShoppingFragment.TodoHolder holder, int position) {
            Shopping todo = mTodos.get(position);
            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return mTodos.size();
        }

    }
}