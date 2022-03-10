package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Adapter from boFs to view, handle displaying BoF list
public class BoFsViewAdapter extends RecyclerView.Adapter<BoFsViewAdapter.ViewHolder> {
    private List<? extends IPerson> people;
    private final Predicate<IPerson> starred;
    private final Predicate<IPerson> waved;
    private IPerson user;

    // Constructor
    public BoFsViewAdapter(List<? extends IPerson> people, Predicate<IPerson> starred, Predicate<IPerson> waved) {
        super();
        this.people = people;
        this.starred = starred;
        this.waved = waved;
    }

    // initialization
    @NonNull
    @Override
    public BoFsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.bof_row, parent, false);

        return new ViewHolder(user, view, starred, waved);
    }

    // binding
    @Override
    public void onBindViewHolder(@NonNull BoFsViewAdapter.ViewHolder holder, int position) {
        holder.setPerson(people.get(position));
    }

    // setter
    @SuppressLint("NotifyDataSetChanged")
    public void setPeopleList(List<? extends IPerson> newPeople) {
        people = newPeople;
        notifyDataSetChanged(); // notifying recycler view
    }

    public List<? extends IPerson> getPeopleList() {
        return people;
    }

    // setter
    @SuppressLint("NotifyDataSetChanged")
    public void setUser(IPerson user) {
        this.user = user;
        notifyDataSetChanged(); // notifying recycler view
    }

    // getter
    @Override
    public int getItemCount() {
        return this.people.size();
    }

    // viewHolder for this BoFsViewAdapter
    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private final TextView personNameView;
        private final TextView numCommonCoursesView;
        private final ImageView personThumbnailView;
        private final ImageView favorited;
        private final ImageView wavedView;
        private final IPerson user;
        private IPerson person;

        private final Predicate<IPerson> starred;
        private final Predicate<IPerson> waved;

        // constructor
        ViewHolder(IPerson user, View itemView, Predicate<IPerson> starred, Predicate<IPerson> waved) {
            super(itemView);
            this.user = user;
            this.personNameView = itemView.findViewById(R.id.bof_row_name);
            this.numCommonCoursesView = itemView.findViewById(R.id.num_common_courses_view);
            this.personThumbnailView = itemView.findViewById(R.id.thumbnail);
            this.favorited = itemView.findViewById(R.id.favorited);
            this.wavedView = itemView.findViewById(R.id.waved);
            this.starred = starred;
            this.waved = waved;
            itemView.setOnClickListener(this);
        }

        // setter
        public void setPerson(IPerson person) {
            this.person = person;
            this.personNameView.setText(person.getName());

            // using glide to load in image
            Glide.with(itemView)
                    .load(this.person.getUrl())
                    .placeholder(R.drawable.placeholder) // filling in place holder
                    .error(R.drawable.placeholder)       // if any part fails, display place holder
                    .into(this.personThumbnailView);
            if (user != null)
                this.numCommonCoursesView.setText( // displaying correct text on recycler view
                        String.valueOf(Utilities.numCoursesTogether(user, person)));

            this.favorited.setVisibility(starred.test(person) ? View.VISIBLE : View.INVISIBLE);
            this.wavedView.setVisibility(waved.test(person) ? View.VISIBLE : View.INVISIBLE);
        }

        // handling clicking on an individual in the list, proceeding to BoFsDetails Activity
        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Comparator<Course> courseComparator = new CourseComparator(Arrays.asList
                    (context.getResources().getStringArray(R.array.quarter_list)));

            // saving data, moving on to BoFsDetails
            Intent intent = new Intent(context, BoFsDetails.class);
            intent.putExtra("uuid", this.person.getStringID());
            intent.putExtra("name", this.person.getName());
            intent.putExtra("courseListParsing", Utilities.encodeCourseList(
                    Utilities.getCoursesTogether(user, this.person)
                            .stream()
                            .sorted(courseComparator)
                            .collect(Collectors.toList())));
            intent.putExtra("url", this.person.getUrl());
            context.startActivity(intent);
        }
    }
}