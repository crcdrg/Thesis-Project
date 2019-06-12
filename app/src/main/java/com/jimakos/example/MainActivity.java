package com.jimakos.example;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
    ProgressDialog progDialog;
    String imgPath;
    String fileName;

    RequestParams params = new RequestParams();
    int poll_counter = 0;
    String encodedString;
    private static int RESULT_LOAD_IMG = 1;
    Bitmap bitmap;


    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        progDialog = new ProgressDialog(this);


        progDialog.setCancelable(false);
        if (!permAlreadyGranted())
            requestPerm();
    }

    private boolean permAlreadyGranted() {


        int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);


        if (result == PackageManager.PERMISSION_GRANTED)


            return true;


        return false;
    }

    private void requestPerm() {

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1001) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Perm. grantd", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Perm. dnd.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void loadImage(View view) {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {


                Uri selectedImage = data.getData();


                String[] filePathColumn = {MediaStore.Images.Media.DATA};


                Cursor cursor = getContentResolver().query(selectedImage,

                        filePathColumn, null, null, null);


                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                imgPath = cursor.getString(columnIndex);


                cursor.close();

                ImageView imgView = (ImageView) findViewById(R.id.imgView);


                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgPath));


                String fileNameSegments[] = imgPath.split("/");


                fileName = fileNameSegments[fileNameSegments.length - 1];


                params.put("filename", fileName);

            } else {

                Toast.makeText(this, "Choose an image",

                        Toast.LENGTH_LONG).show();


            }
        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)


                    .show();
        }

    }


    public void uploadImage(View v) {

        if (imgPath != null && !imgPath.isEmpty()) {


            progDialog.setMessage("Converting Image to Binary Data");


            progDialog.show();


            poll_counter = 0;
            encodeImagetoString();


        } else {

            Toast.makeText(

                    getApplicationContext(),

                    "Select an image please",


                    Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask convert image to string


    public void encodeImagetoString() {


        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {
            }

            ;

            @Override

            protected String doInBackground(Void... params) {

                BitmapFactory.Options options = null;

                options = new BitmapFactory.Options();

                options.inSampleSize = 3;

                bitmap = BitmapFactory.decodeFile(imgPath,

                        options);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                //Compressing the image for better manipulation


                bitmap.compress(Bitmap.CompressFormat.PNG, 5, stream);


                byte[] byte_arr = stream.toByteArray();

                //Encode to base64
                encodedString = Base64.encodeToString(byte_arr, 0);

                return "";
            }

            @Override
            protected void onPostExecute(String msg) {


                progDialog.setMessage("Calling Upload");

                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);


                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void pollResult() {

        final Runnable r = new Runnable() {
            public void run() {

                pollServer();
            }
        };

        handler.postDelayed(r, 2000); //polling the server every 3 seconds for the result.

    }


    public void triggerImageUpload() {
        makeHTTPCall();
    }


    public void pollServer() {

        progDialog.setMessage("Waiting for an Answer...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://68.183.215.246/response.php",

                params, new AsyncHttpResponseHandler() {


                    @Override

                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        progDialog.hide();

                        Toast.makeText(getApplicationContext(), new String(responseBody),

                                Toast.LENGTH_LONG).show();

                        progDialog.setMessage(new String(responseBody))
                        ;
                        if (poll_counter > 10) {

                            progDialog.setMessage("Response time out");

                        } else if (new String(responseBody).equalsIgnoreCase("working...")) {

                            pollResult();

                        }
                    }


                    @Override

                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                        progDialog.hide();

                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),


                                    "Requested resource not found",


                                    Toast.LENGTH_LONG).show();


                        } else if (statusCode == 500) {

                            Toast.makeText(getApplicationContext(),


                                    "Something went wrong",


                                    Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(


                                    getApplicationContext(),

                                    "Error Occured"


                                            + statusCode, Toast.LENGTH_LONG)

                                    .show();
                        }
                    }
                });
    }

    public void makeHTTPCall() {


        progDialog.setMessage("Invoking Php");


        AsyncHttpClient client = new AsyncHttpClient();


        client.post("http://68.183.215.246/info6.php",


                params, new AsyncHttpResponseHandler() {


                    @Override


                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                        progDialog.hide();


                        Toast.makeText(getApplicationContext(), new String(responseBody),


                                Toast.LENGTH_LONG).show();


                        pollResult();
                    }

                    @Override


                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


                        progDialog.hide();


                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),


                                    "Requested resource not found",


                                    Toast.LENGTH_LONG).show();


                        } else if (statusCode == 500) {


                            Toast.makeText(getApplicationContext(),


                                    "Something went wrong",


                                    Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(


                                    getApplicationContext(),


                                    "Error Occured"


                                            + statusCode, Toast.LENGTH_LONG)


                                    .show();
                        }
                    }

                });
    }

    @Override


    protected void onDestroy() {


        // TODO Auto-generated method stub


        super.onDestroy();

        if (progDialog != null) {


            progDialog.dismiss();
        }
    }
}
