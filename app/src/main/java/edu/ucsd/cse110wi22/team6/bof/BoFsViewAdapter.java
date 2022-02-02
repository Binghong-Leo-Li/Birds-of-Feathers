package edu.ucsd.cse110wi22.team6.bof;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoFsViewAdapter extends RecyclerView.Adapter<BoFsViewAdapter.ViewHolder> {
    private final List<? extends BoF> bofs;

    public BoFsViewAdapter(List<? extends BoF> bofs) {
        super();
        this.bofs = bofs;
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
        holder.setBof(bofs.get(position));
    }

    @Override
    public int getItemCount() {
        return this.bofs.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView personNameView;
        private BoF person;

        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.bof_row_name);
            itemView.setOnClickListener(this);
        }

        public void setBof(BoF person) {
            this.person = person;
            this.personNameView.setText(person.getName());
        }

        @Override
        public void onClick(View view) {
//            Context context = view.getContext();
//            Intent intent = new Intent(context, PersonDetailActivity.class);
//            intent.putExtra("person_name", this.person.getName());
//            intent.putExtra("person_notes", this.person.getNotes().toArray(new String[0]));
//            context.startActivity(intent);
            // TODO: view BoF details
            // TODO: maybe add some logging here
        }
    }
}