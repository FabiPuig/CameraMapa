package com.example.a20464654j.cameramapa;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button btFo;
    private Button btVid;
    private String photoPath;

    private static final int REQUEST_TAKE_PHOTO = 1;

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

            }
        });

        return view;
    }

    private void dispatchTakeFotoIntent(){

        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

        if( takePictureIntent.resolveActivity( getContext().getPackageManager() ) != null){

            File photoFile = null;
            try{
                photoFile = creaImatgeFile();
            }catch ( IOException e) {

            }
            if( photoFile != null ){
                takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, Uri.fromFile( photoFile ) );
                startActivityForResult( takePictureIntent, REQUEST_TAKE_PHOTO );
            }
        }

    }

    private File creaImatgeFile() throws IOException{

        // Creacio de fitxer de imatge temporal
        String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss").format( new Date() );
        String imatgeFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES );

        File image = File.createTempFile(
                imatgeFileName,
                ".jpg",
                storageDir
        );

        // Guardar file: path per a usar amb intents ACTION_VIEW
        photoPath = "file:" + image.getAbsolutePath();

        Log.d("DEBBUG-PATH", photoPath);

        return image;
    }
}
