package com.example.therapyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.ArrayAdapter;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    private Context context;
    private List<Message> messages;

    public MessageAdapter(@NonNull Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        Message message = messages.get(position);

        TextView textViewMessage = convertView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message.getMessage());

        return convertView;
    }
}
