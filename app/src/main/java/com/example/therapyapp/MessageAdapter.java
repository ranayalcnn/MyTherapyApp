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

    // Constructor to initialize context and messages list
    public MessageAdapter(@NonNull Context context, List<Message> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout if convertView is null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        // Get the current message
        Message message = messages.get(position);

        // Find the TextView in the layout and set the message text
        TextView textViewMessage = convertView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message.getMessage());

        return convertView;
    }
}
