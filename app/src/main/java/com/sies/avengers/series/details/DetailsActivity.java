package com.sies.avengers.series.details;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sies.avengers.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String name=intent.getStringExtra("name");
        String image = intent.getStringExtra("image");
        String desc = intent.getStringExtra("description");
        image = image.replace("/portrait_incredible","");
        String url = intent.getStringExtra("url");

        ImageButton imageButton = findViewById(R.id.back_button_details);
        imageButton.setOnClickListener(v -> onBackPressed());
        TextView detail_name = findViewById(R.id.detail_name);
        TextView textView2 = findViewById(R.id.detail_desc_title);
        TextView furtherDetails= findViewById(R.id.detail_desc);
        ImageView imageView = findViewById(R.id.detail_image);
        detail_name.setText(name);
        if(desc!=null && !desc.equals("")){
            TextView textView = findViewById(R.id.testText3);
            textView.setText(getResources().getText(R.string.desc));
        }
        textView2.setText(desc);
        Picasso.get().load(image).into(imageView);
        furtherDetails.setText("Click here for details");
        furtherDetails.setOnClickListener(v -> {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browser);
        });

    }
}