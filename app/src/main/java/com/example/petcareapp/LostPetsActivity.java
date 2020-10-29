package com.example.petcareapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LostPetsActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    ListView listView;
    TextView txtOpis, txtKontakt, txtInfo;
    ImageView imageView;
    Button btnMapa;
    ArrayAdapter<String> adapter;
    ArrayList<Pet> petList;


    String mDescription[] = {"Facebook Description", "Whatsapp Description", "Twitter Description", "Instagram Description", "Youtube Description"};
    int images[] = {R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pets);

        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listView);
        imageView = findViewById(R.id.petImageView);
        petList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

//        CustomAdapter adapter = new MyAdapter(this, mDescription, images);
//        listView.setAdapter(adapter);


//        CustomAdapter customAdapter = new CustomAdapter(this, mTitle, mDescription, images);


        firebaseFirestore.collection("userPosts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                petList.add(new Pet(document));
                               // Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            CustomAdapter adapter = new CustomAdapter(getApplicationContext(), petList);
                            listView.setAdapter(adapter);
                        } else {
                           // Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


//        CustomAdapter customAdapter = new CustomAdapter(LostPetsActivity.this,petList);
//        listView.setAdapter(customAdapter);






//        firebaseFirestore.collection("userPosts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
//                for (DocumentSnapshot document : documents) {
//                    petList.add(new Pet(document));
//                }
//                listView = findViewById(R.id.listView);
//
//
//                CustomAdapter customAdapter = new CustomAdapter((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots, petList);
//                listView.setAdapter(customAdapter);
//            }
//        });






        //ZEMI JA LISTATA OD FIREBASE I IZVRITE JA
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //    for ()
            }
        });



//        adapter = new ArrayAdapter<>(this,getSystemService(Context.LAYOUT_INFLATER_SERVICE),lista);
//        View row =getLayoutInflater().inflate(R.layout.listrows,getParent(),false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logoutItem) {
            mAuth.signOut();
            finish();
        }

        if (item.getItemId() == R.id.prijaviItem) {
            Intent intent = new Intent(LostPetsActivity.this, ReportLostActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mAuth.signOut();

        finish();
    }

    public class CustomAdapter extends ArrayAdapter<Pet> {
        Context context;
        String rTitle[];
        String rDescription[];
        int rImages[];
        // Context context;
        List<Pet> pets;
        LayoutInflater inflter;
//
//        public CustomAdapter(Context applicationContext, List<Pet> p) {
//           // super();
//            this.context = applicationContext;
//            this.pets = p;
//            inflter = (LayoutInflater.from(applicationContext));
//        }


        public CustomAdapter(Context c, ArrayList<Pet> petList) {
            super(c, R.layout.listrows, R.id.txtKontakt);
            this.context = c;
            this.pets = petList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.listrows, parent, false);


            ImageView images = row.findViewById(R.id.petImageView);
            //TextView myTitle = row.findViewById(R.id.txtInfo);
            TextView myDescription = row.findViewById(R.id.txtOpis);

            myDescription.setText(pets.get(position).opis);
          //  myTitle.setText(pets.get(position).ime);
        //    Picasso.get().load(getIntent().getStringExtra("imageDownloadLink")).into(images);
//            Glide.with(LostPetsActivity.this).load(firebaseStorage).into(images);
            Glide.with(LostPetsActivity.this).load(pets.get(position).imgUrl).into(images);
            return row;
        }


        @Override
        public int getCount() {
            return pets.size();
        }

//        @Override
//        public Object getItem(int position) {
//            return pets.get(position);
//        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


    }


//    class MyAdapter extends ArrayAdapter<String> {
//
//        Context context;
//        String rTitle[];
//        String rDescription[];
//        int rImgs[];
//
//        MyAdapter (Context c,  String description[], int imgs[]) {
//            super(c, R.layout.listrows, R.id.txtKontakt);
//            this.context = c;
//            this.rDescription = description;
//            this.rImgs = imgs;
//
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.listrows, parent, false);
//
//            ImageView images = row.findViewById(R.id.petImageView);
//            TextView myTitle = row.findViewById(R.id.txtInfo);
//            TextView myDescription = row.findViewById(R.id.txtOpis);
//
//            // now set our resources on views
//            images.setImageResource(rImgs[position]);
//           // myTitle.setText(rTitle[position]);
//            myDescription.setText(rDescription[position]);
//
//
//
//
//            return row;
//        }
//    }
}





















//public class LostPetsActivity extends AppCompatActivity {
//
//    ListView listView;
//    String mTitle[] = {"Facebook", "Whatsapp", "Twitter", "Instagram", "Youtube"};
//    String mDescription[] = {"Facebook Description", "Whatsapp Description", "Twitter Description", "Instagram Description", "Youtube Description"};
//    int images[] = {R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_dark};
//    // so our images and other things are set in array
//
//    // now paste some images in drawable
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_lost_pets);
//
//        listView = findViewById(R.id.listView);
//        // now create an adapter class
//
//        MyAdapter adapter = new MyAdapter(LostPetsActivity.this, mTitle, mDescription, images);
//        listView.setAdapter(adapter);
//        // there is my mistake...
//        // now again check this..
//
//        // now set item click on list view
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position ==  0) {
//                    Toast.makeText(LostPetsActivity.this, "Facebook Description", Toast.LENGTH_SHORT).show();
//                }
//                if (position ==  0) {
//                    Toast.makeText(LostPetsActivity.this, "Whatsapp Description", Toast.LENGTH_SHORT).show();
//                }
//                if (position ==  0) {
//                    Toast.makeText(LostPetsActivity.this, "Twitter Description", Toast.LENGTH_SHORT).show();
//                }
//                if (position ==  0) {
//                    Toast.makeText(LostPetsActivity.this, "Instagram Description", Toast.LENGTH_SHORT).show();
//                }
//                if (position ==  0) {
//                    Toast.makeText(LostPetsActivity.this, "Youtube Description", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        // so item click is done now check list view
//    }
//
//    class MyAdapter extends ArrayAdapter<String> {
//
//        Context context;
//        String rTitle[];
//        String rDescription[];
//        int rImgs[];
//
//        public MyAdapter(Context c, String title[], String description[], int imgs[]) {
//            super(c, R.layout.listrows, R.id.txtInfo, title);
//            this.context = c;
//            this.rTitle = title;
//            this.rDescription = description;
//            this.rImgs = imgs;
//
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View row = layoutInflater.inflate(R.layout.listrows, parent, false);
//            ImageView images = row.findViewById(R.id.petImageView);
//            TextView myTitle = row.findViewById(R.id.txtInfo);
//            TextView myDescription = row.findViewById(R.id.txtOpis);
//
//            // now set our resources on views
//            images.setImageResource(rImgs[position]);
//            myTitle.setText(rTitle[position]);
//            myDescription.setText(rDescription[position]);
//
//
//
//
//            return row;
//        }
//    }
//}
