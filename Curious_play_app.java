// Curious Play - Android App Source Code (Java)

// 1. MainActivity.java package com.curiousplay;

import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.Button; import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity { Button viewContentBtn, adminLoginBtn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    viewContentBtn = findViewById(R.id.viewContentBtn);
    adminLoginBtn = findViewById(R.id.adminLoginBtn);

    viewContentBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ContentViewActivity.class)));
    adminLoginBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdminLoginActivity.class)));
}

}

// 2. AdminLoginActivity.java package com.curiousplay;

import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.Toast; import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity { EditText passwordInput; Button loginBtn; private final String ADMIN_PASSWORD = "curious123"; // You can change this

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_login);

    passwordInput = findViewById(R.id.passwordInput);
    loginBtn = findViewById(R.id.loginBtn);

    loginBtn.setOnClickListener(v -> {
        String input = passwordInput.getText().toString();
        if (input.equals(ADMIN_PASSWORD)) {
            startActivity(new Intent(AdminLoginActivity.this, UploadContentActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    });
}

}

// 3. UploadContentActivity.java package com.curiousplay;

import android.os.Bundle; import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.Toast; import androidx.appcompat.app.AppCompatActivity; import com.google.firebase.database.DatabaseReference; import com.google.firebase.database.FirebaseDatabase;

public class UploadContentActivity extends AppCompatActivity { EditText titleInput, contentInput, videoUrlInput; Button uploadBtn;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upload_content);

    titleInput = findViewById(R.id.titleInput);
    contentInput = findViewById(R.id.contentInput);
    videoUrlInput = findViewById(R.id.videoUrlInput);
    uploadBtn = findViewById(R.id.uploadBtn);

    uploadBtn.setOnClickListener(v -> {
        String title = titleInput.getText().toString();
        String content = contentInput.getText().toString();
        String videoUrl = videoUrlInput.getText().toString();

        if (!title.isEmpty() && !content.isEmpty()) {
            ContentItem item = new ContentItem(title, content, videoUrl);
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("content");
            dbRef.push().setValue(item);
            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
    });
}

}

// 4. ContentViewActivity.java package com.curiousplay;

import android.os.Bundle; import android.widget.ArrayAdapter; import android.widget.ListView; import androidx.annotation.NonNull; import androidx.appcompat.app.AppCompatActivity; import com.google.firebase.database.DataSnapshot; import com.google.firebase.database.DatabaseError; import com.google.firebase.database.DatabaseReference; import com.google.firebase.database.FirebaseDatabase; import com.google.firebase.database.ValueEventListener; import java.util.ArrayList;

public class ContentViewActivity extends AppCompatActivity { ListView contentList; ArrayList<String> contentArray; ArrayAdapter<String> adapter;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_content_view);

    contentList = findViewById(R.id.contentList);
    contentArray = new ArrayList<>();
    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contentArray);
    contentList.setAdapter(adapter);

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("content");
    dbRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            contentArray.clear();
            for (DataSnapshot snap : snapshot.getChildren()) {
                ContentItem item = snap.getValue(ContentItem.class);
                if (item != null) {
                    contentArray.add(item.getTitle() + "\n" + item.getContent() + "\nVideo: " + item.getVideoUrl());
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
    });
}

}

// 5. ContentItem.java (Model Class) package com.curiousplay;

public class ContentItem { private String title, content, videoUrl;

public ContentItem() {}

public ContentItem(String title, String content, String videoUrl) {
    this.title = title;
    this.content = content;
    this.videoUrl = videoUrl;
}

public String getTitle() { return title; }
public String getContent() { return content; }
public String getVideoUrl() { return videoUrl; }

}

// Next: Layout files, Manifest, Firebase config will be added separately

