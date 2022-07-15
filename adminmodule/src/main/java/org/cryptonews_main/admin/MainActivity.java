package org.cryptonews_main.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.cryptonews_admin.admin.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reference;

    private Button send;
    private EditText text, title;
    private RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        send = (Button) findViewById(R.id.send_admin);
        text = (EditText) findViewById(R.id.text_admin);
        title = (EditText) findViewById(R.id.title_admin);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        reference = FirebaseDatabase.getInstance().getReference();
        ((RadioButton)group.getChildAt(1)).setChecked(true);
        send.setOnClickListener(view -> {
           String category = group.getCheckedRadioButtonId()==R.id.post_admin ? "post" : "prognose";
           DatabaseObject object = new DatabaseObject(text.getText().toString(),title.getText().toString(),new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
           reference.child(category).push().setValue(object);
           text.setText("");
           title.setText("");
        });

    }
}