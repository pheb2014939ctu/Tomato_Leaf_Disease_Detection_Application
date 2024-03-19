package com.saurabh.homepage;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.saurabh.homepage.R;
import com.saurabh.homepage.ml.TfLiteModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


public class CamActive extends AppCompatActivity {

    TextView result, demoTxt, classified, clickHere;
    ImageView imageView;
    Button picture;
    int imageSize = 224;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_active);


        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);

        demoTxt = findViewById(R.id.demoText);
        classified = findViewById(R.id.classified);
        clickHere = findViewById(R.id.click_here);

        demoTxt.setVisibility(View.VISIBLE);
        clickHere.setVisibility(View.GONE);
        classified.setVisibility(View.GONE);
        result.setVisibility(View.GONE);


        picture.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,1);
                }
                else{
                    //request camera permission
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });

        Button galleryButton = findViewById(R.id.galleryButton);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleDrivePicker();
            }
        });
    }

    public void openGoogleDrivePicker() {
        Intent pickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickerIntent.setType("image/*");
        startActivityForResult(pickerIntent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK){
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(),image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);


            imageView.setImageBitmap(image);

            demoTxt.setVisibility(View.GONE);
            clickHere.setVisibility(View.VISIBLE);
            classified.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);

            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
            classifyImage(image);
        }
        else if (requestCode == 3 && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(selectedImage);

                demoTxt.setVisibility(View.GONE);
                clickHere.setVisibility(View.VISIBLE);
                classified.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);

                selectedImage = Bitmap.createScaledBitmap(selectedImage,imageSize,imageSize,false);
                classifyImage(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void classifyImage(Bitmap photo) {
        try {
            TfLiteModel model = TfLiteModel.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[224 * 224];
            photo.getPixels(intValues, 0, photo.getWidth(), 0, 0, photo.getWidth(), photo.getHeight());
            int pixel = 0;
            for(int i = 0; i < 224; i ++){
                for(int j = 0; j < 224; j ++){
                    int val = intValues[pixel++]; //RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            TfLiteModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidence = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidence.length; i++) {
                if (confidence[i] > maxConfidence) {
                    maxConfidence = confidence[i];
                    maxPos = i;
                }
            }
//            _print("pos: " + maxPos);
            String[] classes = {"Tomato Late Blight", "Tomato Powdery Mildew", "Tomato Leaf Spot", "Tomato Yellow Leaf Curl Virus", "Tomato Healthy"};
            String detectedDisease = classes[maxPos];
            // Hiển thị thông tin chi tiết và cách điều trị bệnh
            showDiseaseDetails(detectedDisease);

            result.setText(classes[maxPos]);
            result.setOnClickListener(view -> {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/search?q=" + result.getText())));
            });
            model.close();
        }catch (IOException e){
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDiseaseDetails(String diseaseName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(diseaseName);

        // Tìm thông tin chi tiết và cách điều trị của bệnh dựa trên tên bệnh
        DiseaseDetails diseaseDetails = getDiseaseDetailsByName(diseaseName);

        builder.setMessage("Symptoms:\n" + diseaseDetails.getDetails() +
                "\n\nControl:\n" + diseaseDetails.getTreatment());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private DiseaseDetails getDiseaseDetailsByName(String diseaseName) {
        String details = "";
        String treatment = "";

        switch (diseaseName) {
            case "Tomato Late Blight":
                details = "Leaves become yellow, develop brown or black spots, and drop. Fruits become rotten and deformed.";
                treatment = "Grow resistant tomato varieties, rotate crops, keep the fields clean, and use pesticides as needed.";
                break;
            case "Tomato Powdery Mildew":
                details = "Powdery mildew appears as white, powdery spots on the upper leaf surface. It is caused by fungal spores and can spread rapidly in dry conditions.";
                treatment = "Treatment for Tomato Powdery Mildew: Remove affected leaves, apply fungicides, and ensure proper air circulation.";
                break;
            case "Tomato Leaf Spot":
                details = "Leaf spots are characterized by circular lesions with dark edges on the tomato leaves. This disease can cause defoliation and reduce yield.";
                treatment = "Treatment for Tomato Leaf Spot: Remove affected leaves, apply fungicides, and practice crop rotation.";
                break;
            case "Tomato Yellow Leaf Curl Virus":
                details = "Yellowing and curling of tomato leaves are symptoms of the Yellow Leaf Curl Virus. It is transmitted by whiteflies.";
                treatment = "Treatment for Tomato Yellow Leaf Curl Virus: Use insecticides to control whiteflies, remove infected plants, and plant virus-resistant tomato varieties.";
                break;
            case "Tomato Healthy":
                details = "This tomato plant appears to be healthy.";
                treatment = "No specific treatment needed for a healthy tomato plant.";
                break;
        }

        return new DiseaseDetails(details, treatment);
    }


}