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

/**
 * Adapter for displaying Messages in the RecyclerView.
 *
 * ListAdapter is an extension of RecyclerView.Adapter that provides
 * the more convenient way of handling the data changes and computing
 * the differences between the items in the list.
 */
public class MessageAdapter extends ListAdapter<Message, RecyclerView.ViewHolder> {

    /**
     * ViewHolder that contains the text of the Message along with the message header,
     * which contains the informations about the postTime of the Message and the username
     * of the sender.
     */
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

        /**
         * Initializes the views - assigns layout's elements to the instance variables.
         */
        private void initializeViews()
        {
            tvSenderUsername = itemView.findViewById(R.id.tvSenderUsername);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvText = itemView.findViewById(R.id.tvText);
        }

        /**
         * Sets the views according to the data of the given Message object.
         *
         * @param message message object containing the data to be displayed.
         */
        public void setView(Message message)
        {
            tvSenderUsername.setText(message.getSender().getUsername());
            tvPostTime.setText(dateFormatter.format(message.getPostTime()));
            tvText.setText(message.getText());
        }
    }

    /**
     * ViewHolder that contains just the text of the Message.
     */
    class MessageHolder extends RecyclerView.ViewHolder {

        private TextView tvText;

        public MessageHolder(@NonNull View itemView)
        {
            super(itemView);
            initializeViews();
        }

        /**
         * Initializes the views - assigns layout's elements to the instance variables.
         */
        private void initializeViews()
        {
            tvText = itemView.findViewById(R.id.tvText);
        }

        /**
         * Sets the views according to the data of the given Message object.
         *
         * @param message message object containing the data to be displayed.
         */
        public void setView(Message message)
        {
            tvText.setText(message.getText());
        }
    }

    /**
     * DiffUtil object used by the adapter for comparision of the Messages.
     */
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

    /**
     * Checks the position of the Message and the content of the Message at the given position.
     * According to these informations it specifies which view type should be used for displaying the
     * Message at the given position.
     *
     * @param position position of the Message in the list.
     * @return the appropriate view type for the Message at the given position.
     */
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

    /**
     * Creates the appropriate ViewHolder, based on the given viewType and returns it.
     *
     * @param parent the recycler view.
     * @param viewType type of the view that should be displayed.
     * @return appropriate ViewHolder for the given viewType.
     */
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

    /**
     * Checks the type of the ViewHolder at the specified position and sets its content
     * with the data of the Message at the given position.
     *
     * @param holder ViewHolder to bind.
     * @param position position of the given holder.
     */
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


}
