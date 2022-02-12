package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MockingPasting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocking_pasting);
    }

    public void onGoBackClicked(View view) {
        EditText input = view.findViewById(R.id.bof_info_pasted);
        System.out.print(input);

//        Context context = view.getContext();
//        Intent intent = new Intent(context, BoFsDetails.class);
//        intent.putExtra("name", this.person.getName());
//        intent.putExtra("courseListParsing", Utilities.encodeCourseList(this.person.getCourseList()));
//        intent.putExtra("url", this.person.getUrl());
//        context.startActivity(intent);

    }
}