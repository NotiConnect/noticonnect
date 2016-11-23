package io.github.drspeedy.noticonnectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class FilteringFragment extends Fragment {
    // private variables
    // model variables
    /**
     * constant used to request a list of notification sources
     * to forward to the server.
     */
    private static final int REQUEST_FORWARDING_LIST = 1;
    // ui element variables
    private RecyclerView recyclerView;
    private NotificationSourceAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class NotificationSourceAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class NotificationSourceViewHolder extends RecyclerView.ViewHolder {


        public NotificationSourceViewHolder(View itemView) {
            super(itemView);
        }
    }
}
