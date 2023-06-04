package com.example.bashguide21;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

class ListItem {
    String name, address, rating;
    int id;
    byte[] image;

    public ListItem(String name, String address, String rating, int id, byte[] image) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.id = id;
        this.image = image;
    }
}

class ListGroupsAdapter extends ArrayAdapter<ListItem> {

    public ListGroupsAdapter(@NonNull Context context, ListItem[] listItems) {
        super(context, R.layout.list_groups_item, listItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ListItem listItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_groups_item, null);
        }

        ((TextView) convertView.findViewById(R.id.list_name)).setText(listItem.name);
        ((TextView) convertView.findViewById(R.id.list_address)).setText(listItem.address);
        ((TextView) convertView.findViewById(R.id.list_rating)).setText(listItem.rating);
        ((ImageView) convertView.findViewById(R.id.list_image)).setImageBitmap(BitmapFactory.decodeByteArray(listItem.image, 0, listItem.image.length));

        return convertView;
    }
}



public class ListGroupsActivity extends AppCompatActivity {

    SQLiteDatabase db;
    String sql;
    ListItem[] listItems;
    ListView list_groups;

    ListGroupsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_groups);
        db = (new DBHelper(this)).getReadableDatabase();



        switch (getIntent().getExtras().getInt("GroupID")){
            case 0: sql = "SELECT monument.id, name, image, street, home FROM monument INNER JOIN address WHERE monument.id_address = address.id"; SqlGroup.str = "monument"; break;
            case 1: sql = "SELECT interes.id, name, image, street, home FROM interes INNER JOIN address WHERE interes.id_address = address.id"; SqlGroup.str = "interes"; break;
            case 2: sql = "SELECT park.id, name, image, street, home FROM park INNER JOIN address WHERE park.id_address = address.id"; SqlGroup.str = "park"; break;
            case 3: sql = "SELECT museum.id, name, image, street, home, rating FROM museum INNER JOIN address WHERE museum.id_address = address.id"; SqlGroup.str = "museum"; break;
            case 4: sql = "SELECT sport.id, name, image, street, home FROM sport INNER JOIN address WHERE sport.id_address = address.id"; SqlGroup.str = "sport"; break;
            default: sql = "SELECT fun.id, name, image, street, home, rating FROM fun INNER JOIN address WHERE fun.id_address = address.id"; SqlGroup.str = "fun";
        }

        Cursor c = db.rawQuery(sql, null);
        listItems = new ListItem[c.getCount()];
        int count = 0;
        c.moveToFirst();
        do {
            String address = "";
            if (c.getString(c.getColumnIndex("street")) != null){
                address = c.getString(c.getColumnIndex("street"));
                if (c.getString(c.getColumnIndex("home")) != null){
                    if (c.getString(c.getColumnIndex("home")).length() > 0) {
                        address += ", " + c.getString(c.getColumnIndex("home"));
                    }
                }
            }
            String rating = "";
            if ((getIntent().getExtras().getInt("GroupID") == 3) || (getIntent().getExtras().getInt("GroupID") == 5)){
                rating = c.getString(c.getColumnIndex("rating"));
            }
            listItems[count++] = new ListItem(c.getString(c.getColumnIndex("name")),
                                              address, rating,
                                              c.getInt(c.getColumnIndex("id")),
                                              c.getBlob(c.getColumnIndex("image")));
        } while (c.moveToNext());
        c.close();

        adapter = new ListGroupsAdapter(this, listItems);
        list_groups = findViewById(R.id.list_groups);
        list_groups.setAdapter(adapter);

        list_groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),position, LENGTH_SHORT).show();

                Intent intent_item = new Intent(ListGroupsActivity.this, ItemActivity.class);
                intent_item.putExtra("ItemID", listItems[position].id);
                startActivity(intent_item);
            }
        });

        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}