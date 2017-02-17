package com.example.a20464654j.cameramapa;

import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button btFo;
    private Button btVid;
    private String multimediaPath;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    File f;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btFo = (Button) view.findViewById( R.id.btFoto );
        btVid = (Button) view.findViewById( R.id.btVideo );

        btFo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakeFotoIntent();

            }
        });

        btVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakeVideoIntent();

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("path");

        super.onCreate(savedInstanceState);
    }

    private void dispatchTakeFotoIntent(){

        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

        if( takePictureIntent.resolveActivity( getContext().getPackageManager() ) != null){

            File photoFile = null;
            try{
                photoFile = creaTempFile( CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
            }catch ( IOException e ) {

            }
            if( photoFile != null ){
                //nom del fitxer de la imatge
                takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( photoFile ) );
                startActivityForResult( takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
                f = photoFile;
            }
        }
    }

    private void dispatchTakeVideoIntent(){

        Intent takeVideoIntent = new Intent( MediaStore.ACTION_VIDEO_CAPTURE );

        if( takeVideoIntent.resolveActivity( getContext().getPackageManager() ) != null ){

            File videoFile = null;
            try{
                videoFile = creaTempFile( CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE );
            }catch ( IOException e ){
            }

            if( videoFile != null ){
                // nom del fitxer de video
                takeVideoIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( videoFile ) );
                //qualitat alta
                takeVideoIntent.putExtra( MediaStore.EXTRA_VIDEO_QUALITY, 1 );

                startActivityForResult( takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE );
                f = videoFile;
            }
        }
    }

    private File creaTempFile( int request_code ) throws IOException{

        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date() );
        String extensio = "";
        String fileName = "";
        File storageDir = null;

        if( request_code == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ){
            // Creacio de fitxer de imatge temporal
            extensio = ".jpg";
            fileName = "JPEG_" + timeStamp + "_";

            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES );

        }

        if ( request_code == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE ){
            extensio = ".mp4";
            fileName = "MP4_" + timeStamp + "_";

            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES );

        }

        File file = File.createTempFile(
                fileName,
                extensio,
                storageDir
        );


        // Guardar file: path per a usar amb intents ACTION_VIEW
        multimediaPath = "file:" + file.getAbsolutePath();

        Log.d("DEBBUG-PATH", multimediaPath);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ){

            if( resultCode == RESULT_OK ){
                guardaPath();
            }else{
                File f = new File( multimediaPath );
                f.delete();
            }
        }

        if( requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE ){

            if( requestCode == RESULT_OK ){
                guardaPath();
            }else{
                File f = new File( multimediaPath );
                f.delete();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void guardaPath(){

        Gallery ga = new Gallery( multimediaPath, 4.0, 4.0);
        reference.push().setValue( ga );

    }
}
