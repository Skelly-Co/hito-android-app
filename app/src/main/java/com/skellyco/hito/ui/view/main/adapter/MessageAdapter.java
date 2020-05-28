package com.skellyco.hito.ui.view.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.skellyco.hito.R;
import com.skellyco.hito.core.model.entity.Message;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class MessageAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {

    class MessageWithHeaderHolder extends RecyclerView.ViewHolder {

        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM YYYY HH:mm");

        private TextView tvSenderUsername;
        private TextView tvPostTime;
        private TextView tvText;

        public MessageWithHeaderHolder(@NonNull View itemView)
        {
            super(itemView);
            initializeViews();
        }

        private void initializeViews()
        {
            tvSenderUsername = itemView.findViewById(R.id.tvSenderUsername);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvText = itemView.findViewById(R.id.tvText);
        }

        public void setView(Message message)
        {
            tvSenderUsername.setText(message.getSender().getUsername());
            tvPostTime.setText(dateFormatter.format(message.getPostTime()));
            tvText.setText(message.getText());
        }
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        private TextView tvText;

        public MessageHolder(@NonNull View itemView)
        {
            super(itemView);
            initializeViews();
        }

        private void initializeViews()
        {
            tvText = itemView.findViewById(R.id.tvText);
        }

        public void setView(Message message)
        {
            tvText.setText(message.getText());
        }

    }

    private static DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.equals(newItem);
        }
    };

    private static final int TYPE_MESSAGE_WITH_HEADER = 1;
    private static final int TYPE_MESSAGE = 2;
    private static final int NEW_HEADER_MINUTES = 10;

    public MessageAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_MESSAGE_WITH_HEADER)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_with_header, parent, false);
            return new MessageWithHeaderHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);
            return new MessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_MESSAGE_WITH_HEADER)
        {
            ((MessageWithHeaderHolder) holder).setView(getItem(position));
        }
        else
        {
            ((MessageHolder) holder).setView(getItem(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return TYPE_MESSAGE_WITH_HEADER;
        }
        else
        {
            Message message = getItem(position);
            Message previousMessage = getItem(position - 1);
            if(!message.getSender().getUid().equals(previousMessage.getSender().getUid()))
            {
                return TYPE_MESSAGE_WITH_HEADER;
            }
            else
            {
                long deltaPostTimeMillis = Math.abs(message.getPostTime().getTime() - previousMessage.getPostTime().getTime());
                long deltaPostTimeMinutes = TimeUnit.MINUTES.convert(deltaPostTimeMillis, TimeUnit.MILLISECONDS);
                if(deltaPostTimeMinutes >= NEW_HEADER_MINUTES)
                {
                    return TYPE_MESSAGE_WITH_HEADER;
                }
                else
                {
                    return TYPE_MESSAGE;
                }
            }
        }
    }
}
