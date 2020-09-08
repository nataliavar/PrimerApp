package cr.ac.ucr.com.primeraapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import cr.ac.ucr.com.primeraapp.R;
import cr.ac.ucr.com.primeraapp.Utils.AppPreferences;

public class ToDoListFragment extends Fragment implements View.OnClickListener {

    private AppCompatActivity activity;
    private ArrayList<String> todosArr;
    private String todosStr;
    private ListView lvTodos;
    private Gson gson;
    private ArrayAdapter<String> arrayAdapter;

    private ToDoListFragment() {
        // Required empty public constructor
    }

    public static ToDoListFragment newInstance() {
        ToDoListFragment fragment = new ToDoListFragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    //en este metodo creamos los array
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todosArr = new ArrayList<>();
        todosStr = AppPreferences.getInstance(activity).getString(AppPreferences.Keys.ITEMS);
        arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, todosArr);
        gson = new Gson();
    }

    //en este metodo se crean todas las variables del xml
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        lvTodos = view.findViewById(R.id.lv_todos);
        FloatingActionButton fabAddTask = view.findViewById(R.id.fab_add_todo);
        fabAddTask.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    //se realizan los llamados en este metodo
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true); //para avisar que se tiene opciones en el menu

        if (!todosStr.isEmpty()){
            String [] todosArray = gson.fromJson(todosStr, String[].class);
            todosArr.addAll(Arrays.asList(todosArray));
        }
        lvTodos.setAdapter(arrayAdapter);
        setupListViewListener();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.todos_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clean_list:
               cleanList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    //-----------on click method
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add_todo:
                showAlert();
                break;
            default:
                break;
        }
    }


    private void setupListViewListener(){
        lvTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.confirm_delete_item)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int x) {
                                todosArr.remove(i);
                                arrayAdapter.notifyDataSetChanged();

                                saveListPreferences();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .create()
                        .show();
                return true;
            }
        });
    }

    private void saveListPreferences(){
        todosStr = gson.toJson(todosArr);
        AppPreferences.getInstance(activity).put(AppPreferences.Keys.ITEMS, todosStr);
    }

    private void cleanList(){
        todosArr.clear();
        arrayAdapter.notifyDataSetChanged();
        this.saveListPreferences();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_task, null);

        builder.setView(view)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextInputEditText etTaskName = view.findViewById(R.id.et_task_name);
                        String taskName = etTaskName.getText().toString();
                        if (!taskName.isEmpty()){
                            todosArr.add(taskName);
                            arrayAdapter.notifyDataSetChanged();

                            saveListPreferences();

                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel,null);
        builder.create();
        builder.show();
    }

}//end of class