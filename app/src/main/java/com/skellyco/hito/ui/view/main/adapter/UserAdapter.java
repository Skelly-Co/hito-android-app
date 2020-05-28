package com.skellyco.hito.ui.view.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.skellyco.hito.R;
import com.skellyco.hito.core.model.entity.User;
import com.skellyco.hito.ui.view.util.ListFilter;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    class UserHolder extends RecyclerView.ViewHolder
    {
        private User user;
        private TextView tvDisplayName;

        public UserHolder(@NonNull View itemView)
        {
            super(itemView);
            initializeViews();
            initializeListener();
        }

        private void initializeViews()
        {
            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);
        }

        private void initializeListener()
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(user);
                    }
                }
            });
        }

        public void setView(User user)
        {
            this.user = user;
            tvDisplayName.setText(user.getUsername());
        }


    }

    private static DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getUid().equals(newItem.getUid());
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.equals(newItem);
        }
    };

    private String filter = "";
    private List<User> unfilteredList = new ArrayList<>();
    private List<User> filteredList = new ArrayList<>();
    private OnItemClickListener listener;

    public UserAdapter()
    {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.setView(getItem(position));
    }

    @Override
    public void submitList(@Nullable List<User> list) {
        unfilteredList = list;
        filteredList = ListFilter.filterUserList(unfilteredList, filter);
        super.submitList(filteredList);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public void updateFilter(String filter)
    {
        this.filter = filter;
        submitList(unfilteredList);
    }

}
