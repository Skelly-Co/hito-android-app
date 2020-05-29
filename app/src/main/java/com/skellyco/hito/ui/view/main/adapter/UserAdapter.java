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
import com.skellyco.hito.core.util.ListFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying Users in the RecyclerView.
 *
 * ListAdapter is an extension of RecyclerView.Adapter that provides
 * the more convenient way of handling the data changes and computing
 * the differences between the items in the list.
 */
public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {

    /**
     * Interface for the listener of the item click event of the list item.
     */
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    /**
     * ViewHolder that contains the user's username specified as a displayName.
     */
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

        /**
         * Initializes the views - assigns layout's elements to the instance variables.
         */
        private void initializeViews()
        {
            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);
        }

        /**
         * Initializes the on click listener of the list item.
         * If the listener for this adapter is set, it invokes its onItemClick method.
         */
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

        /**
         * Sets the views according to the data of the given User object.
         *
         * @param user user object containing the data to be displayed.
         */
        public void setView(User user)
        {
            this.user = user;
            tvDisplayName.setText(user.getUsername());
        }


    }

    /**
     * DiffUtil object used by the adapter for comparision of the Users.
     */
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

    /**
     * Creates the appropriate ViewHolder, based on the given viewType and returns it.
     *
     * @param parent the recycler view.
     * @param viewType type of the view that should be displayed.
     * @return appropriate ViewHolder for the given viewType.
     */
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new UserHolder(itemView);
    }

    /**
     * Checks the type of the ViewHolder at the specified position and sets its content
     * with the data of the Message at the given position.
     *
     * @param holder ViewHolder to bind.
     * @param position position of the given holder.
     */
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.setView(getItem(position));
    }

    /**
     * submitList method had to be overridden to apply the filtering and
     * always show the the list with regards to the specified filter.
     *
     * @param list list to be displayed in the recycler view.
     */
    @Override
    public void submitList(@Nullable List<User> list) {
        unfilteredList = list;
        filteredList = ListFilter.filterUserList(unfilteredList, filter);
        super.submitList(filteredList);
    }

    /**
     * Sets the listener for the on item click method of the list item.
     *
     * @param listener listener for the on click method of the list item.
     */
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    /**
     * Updates the filter for the list and invokes the submitList method to update
     * the list with the new filter.
     *
     * @param filter filter for the user list.
     */
    public void updateFilter(String filter)
    {
        this.filter = filter;
        submitList(unfilteredList);
    }

}
