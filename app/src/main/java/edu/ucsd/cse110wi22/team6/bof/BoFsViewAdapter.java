package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoFsViewAdapter extends RecyclerView.Adapter<BoFsViewAdapter.ViewHolder> {
    private List<? extends IPerson> people;

    public List<? extends IPerson> getListBoFs(){
        return this.people;
    }

    public BoFsViewAdapter(List<? extends IPerson> people) {
        super();
        this.people = people;
    }

    @NonNull
    @Override
    public BoFsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bof_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BoFsViewAdapter.ViewHolder holder, int position) {
        holder.setPerson(people.get(position));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPeopleList(List<? extends IPerson> newPeople) {
        people = newPeople;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView personNameView;
        private IPerson person;

        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.bof_row_name);
            itemView.setOnClickListener(this);
        }

        public void setPerson(IPerson person) {
            this.person = person;
            this.personNameView.setText(person.getName());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, BoFsDetails.class);
            intent.putExtra("name", this.person.getName());
            intent.putExtra("courseListParsing", Utilities.encodeCourseList(Utilities.getCoursesTogether(user)));
            intent.putExtra("url", this.person.getUrl());
            context.startActivity(intent);
            // TODO: view BoF details
            // TODO: maybe add some logging here
        }
    }
}