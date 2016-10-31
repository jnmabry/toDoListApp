package com.example.todolistapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JoshuaMabry on 10/24/16.
 */

public class ToDoDetailActivity extends AppCompatActivity {

    private EditText toDoNameEdit;
    private EditText toDoDueDateEdit;
    private EditText toDoDueTimeEdit;
    private EditText toDoCommentEdit;
    private EditText toDoCategoryEdit;
    private ImageView imgView;
    private Uri uri;

//    //IMAGE PATHNAME
    private String pathname = "";

    private Button submit;

//    private ImageView imgView;
    private int index;

    // IMAGE RELATED RESULT TAG
    private static int RESULT_LOAD_IMG = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_detail, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_item_detail);

        toDoNameEdit = (EditText)findViewById(R.id.toDoNameEdit);
        toDoDueDateEdit = (EditText)findViewById(R.id.toDoDueDateEdit);
        toDoDueTimeEdit = (EditText)findViewById(R.id.toDoDueTimeEdit);
        toDoCommentEdit = (EditText)findViewById(R.id.comments);
        toDoCategoryEdit = (EditText)findViewById(R.id.category);
        imgView = (ImageView) findViewById(R.id.imgView);

        submit = (Button) findViewById(R.id.submit);

        Intent intent = getIntent();

        // Pull information then put it on UI
        toDoNameEdit.setText(intent.getStringExtra("Title"));
        toDoDueDateEdit.setText(intent.getStringExtra("Date"));
        toDoDueTimeEdit.setText(intent.getStringExtra("Time"));
        toDoCommentEdit.setText(intent.getStringExtra("Comment"));
        toDoCategoryEdit.setText(intent.getStringExtra("Category"));
//        try {
//            imgView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(intent.getStringExtra("Pathname")))));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        index = intent.getIntExtra("Index", -1);

        //Click on save button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Use our intent
                Intent intent = getIntent();
                intent.putExtra("Title",toDoNameEdit.getText().toString());
                intent.putExtra("Date",toDoDueDateEdit.getText().toString());
                intent.putExtra("Time", toDoDueTimeEdit.getText().toString());
                intent.putExtra("Comment", toDoCommentEdit.getText().toString());
                intent.putExtra("Category", toDoCategoryEdit.getText().toString());


                intent.putExtra("Index", index);
                //Ok everything went fine
                setResult(RESULT_OK, intent);


                //Close
                finish();
            }
        });
    }




    //LOADS IMAGE FROM GALLERY
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    //RESULT FROM LOAD IMAGE GALLERY
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bm = null;

        try {

            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {


//                // Get the Image from data
                Uri selectedImage = data.getData();
                Log.e(selectedImage.toString(),"**** URI ***");

                ImageView imgView = (ImageView) findViewById(R.id.imgView);

//
//                // Set the Image in ImageView after decoding the String
//                imgView.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage)));

                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);
                imgView.setImageBitmap(bm);

                Intent intent = new Intent();
                intent.putExtra("Pathname", selectedImage.toString());

                storeImage(bm);

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);

                Log.e("Hey made it", "**** Image log 1 ******");

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }

    // CREATE BITMAP OF IMAGE
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("Error", "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("File not found", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("No access for you", "Error accessing file: " + e.getMessage());
        }
    }


    /** Create a File for saving an image or video */
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()

                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);

        Log.e(mediaStorageDir.getPath() + File.separator + mImageName.toString(), "Hey");


        pathname = mediaStorageDir.getPath() + File.separator + mImageName.toString();



        Log.e("Made that picture", "********");
        return mediaFile;
    }
}
