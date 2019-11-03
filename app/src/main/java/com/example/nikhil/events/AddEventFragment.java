package com.example.nikhil.events;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventFragment extends Fragment {
    private ImageButton selectImage;
    private EditText title;
    private EditText description,contactInfo,venue,reg_fee;
    private Uri imageUri =null;
    private Button postEventButton;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private ProgressDialog mProgress;
    private Spinner mSpinner_Branch;
    private String branch_val=null;


    public AddEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_eventadd, container, false);



        selectImage=(ImageButton)view.findViewById(R.id.addimage);
        title=(EditText)view.findViewById(R.id.addtitle);
        description=(EditText)view.findViewById(R.id.addDescription);
        contactInfo=(EditText)view.findViewById(R.id.addContactinfo);
        postEventButton=(Button)view.findViewById(R.id.postButton);
        mSpinner_Branch=(Spinner)view.findViewById(R.id.spinner_branch);
        reg_fee=(EditText)view.findViewById(R.id.addRegistationFeeinfo);
        venue=(EditText)view.findViewById(R.id.addVenueinfo);

        mStorage= FirebaseStorage.getInstance().getReference();

        mProgress=new ProgressDialog(getContext());

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
             }
        });
        postEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent();
            }
        });

       mSpinner_Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String selectedItem = parent.getItemAtPosition(position).toString();
               branch_val=selectedItem;

           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageUri = data.getData();

            selectImage.setImageURI(imageUri);
        }
    }

    private void postEvent(){
        mProgress.setTitle("Posting...");
        mProgress.setMessage("Uploading your Event please wait...");


        final String title_val=title.getText().toString().trim();
        final String desc_val=description.getText().toString().trim();
        final  String cotact_val=contactInfo.getText().toString().trim();
        final String reg_fee_val=reg_fee.getText().toString().trim();
        final String venue_val=venue.getText().toString().trim();

        if(TextUtils.isEmpty(title_val)) {
            title.setError("Title is required");
            return;
        }else if(TextUtils.isEmpty(desc_val)){
            description.setError("Description is require");
            return;
        }
        else if(TextUtils.isEmpty(cotact_val)){
            contactInfo.setError("Description is require");
            return;
        }else if(TextUtils.isEmpty(reg_fee_val)){
            reg_fee.setError("Description is require");
            return;
        }else if(TextUtils.isEmpty(venue_val)){
            venue.setError("Description is require");
            return;
        }

      else{
            mProgress.show();

            mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("All_Events").child(branch_val);

            StorageReference filepath=mStorage.child("Event_images").child(imageUri.getLastPathSegment());


            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgress.dismiss();

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                             Uri downloadUrl = urlTask.getResult();
                            String postId= UUID.randomUUID().toString();
                            Event upload=new Event(title_val,desc_val,downloadUrl.toString(),cotact_val,reg_fee_val,venue_val,branch_val,postId);
                            mDatabaseReference.child(postId).setValue(upload);

                            Toast.makeText(getContext(), "Event succesfully uploaded", Toast.LENGTH_SHORT).show();

                             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner,new HomeFragment()).commit();


                        }
                    });

        }


    }








}
