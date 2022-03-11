package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

// Class handling viewing BoF details when clicked in
public class BoFsDetails extends NearbyActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CourseViewAdapter courseViewAdapter;
    private ImageButton favoriteButton;

    private AppStorage storage;
    private IPerson bof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bof_details);

        storage = Utilities.getStorageInstance(this);

        Bundle extras = getIntent().getExtras();
                                    // extracting information from previously passed-in parameters
                                    // we parse the information accordingly below
        UUID uuid = UUID.fromString(extras.getString("uuid", ""));
        String name = extras.getString("name", "User do not exist");
        setTitle(name);
        String courseListParsing = extras.getString("courseListParsing");
        List<Course> courseList = Utilities.parseCourseList(courseListParsing);
        String url = extras.getString("url");

        bof = new Person(uuid, name, courseList, url);

        ImageView image = findViewById(R.id.imageView);
        Glide.with(this) // Using glide library to load image from url
                .load(url)
                .placeholder(R.drawable.placeholder) // setting place holder
                .error(R.drawable.placeholder) // if any part creates an error then show place holder
                .into(image);


        coursesRecyclerView = findViewById(R.id.bof_courses_info);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager); // setting the correct lay out
        courseViewAdapter = new CourseViewAdapter(courseList);
        coursesRecyclerView.setAdapter(courseViewAdapter); // set the adapter to display correct view

        favoriteButton = findViewById(R.id.favoriteButton);
        updateUI();
    }

    void updateUI() {
        favoriteButton.setImageResource(storage.isFavorited(bof) ?
                android.R.drawable.btn_star_big_on :
                android.R.drawable.btn_star_big_off);
    }

    // handling the back button being clicked
    public void onGoBackClicked(View view) {
        finish();
    }

    public void onFavoriteClicked(View view) {
        if (storage.isFavorited(bof)) {
            Toast.makeText(this, "Removed from Favorites",
                    Toast.LENGTH_SHORT).show();
            storage.removeFromFavorites(bof);
        } else {
            Toast.makeText(this, "Saved to Favorites",
                    Toast.LENGTH_SHORT).show();
            storage.addToFavorites(bof);
        }
        updateUI();
    }

    public void onWaveClicked(View view) {
        Toast.makeText(this, "Wave sent",
                Toast.LENGTH_LONG).show();

        SessionManager.getInstance(this).waveTo(bof, this);
    }
}