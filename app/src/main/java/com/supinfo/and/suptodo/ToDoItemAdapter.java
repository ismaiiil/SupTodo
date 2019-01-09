package com.supinfo.and.suptodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.supinfo.and.suptodo.model.TodoResponse;

import java.util.List;

public class ToDoItemAdapter extends ArrayAdapter<TodoResponse> {

    private List<TodoResponse> todoResponses;
    private LayoutInflater layoutInflater;
    private Context mContext;

    ToDoItemAdapter(@NonNull Context context, @NonNull List<TodoResponse> objects) {
        super(context, R.layout.todo_list_item, objects);
        todoResponses = objects;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.todo_list_item,parent,false);
        }

        TextView ownerOfTodoText = convertView.findViewById(R.id.owner_of_todo);
        TextView todoText = convertView.findViewById(R.id.todo_text);
        TextView isShared = convertView.findViewById(R.id.isShared);

        TodoResponse todoResponse = todoResponses.get(position);
        String totalString = todoResponse.getTodo();

        if (totalString.contains("\n")){
            String[] result = totalString.split("\n", 2);
            String ownerString = result[0];
            if(ownerString.equals("")){
                ownerString = todoResponse.getUserinvited();
            }
            String todosString = result[1];
            ownerOfTodoText.setText(ownerString);
            todoText.setText(todosString);
        }else{
            String ownerString = todoResponse.getUserinvited();
            String todosString = todoResponse.getTodo();
            ownerOfTodoText.setText(ownerString);
            todoText.setText(todosString);
        }

        if(todoResponse.getUserinvited() == null){

            isShared.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            isShared.setText(R.string.PrivateText);
            ownerOfTodoText.setText(R.string.PrivateTodoTitle);
        }else{
            isShared.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            isShared.setText(R.string.SharedText);
        }

        return convertView;
    }
}
