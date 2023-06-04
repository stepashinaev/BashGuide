package com.example.bashguide21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

class Item{
    int id;
    byte[] image;
    String name, site, phone, address;

    public Item(int id, byte[] image, String name, String site, String phone, String address){
        this.id = id;
        this.image = image;
        this.name = name;
        this.site = site;
        this.phone = phone;
        this.address = address;
    }
}

public class ItemActivity extends AppCompatActivity {

    int itemID;
    double lat, lon;
    String site;
    TextView nameView;
    ImageView imageView;
    SQLiteDatabase db;
    WebView descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        findViewById(R.id.mapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemActivity.this, MapActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lon", lon);
                startActivity(intent);
            }
        });

        itemID = getIntent().getExtras().getInt("ItemID");

        nameView = findViewById(R.id.nameView);
        imageView = findViewById(R.id.imageView);
        descView = findViewById(R.id.descView);

        db = (new DBHelper(getApplicationContext())).getReadableDatabase();

        String sql = "SELECT * FROM " + SqlGroup.str + " INNER JOIN address WHERE " + SqlGroup.str + ".id_address = address.id AND "
                + SqlGroup.str + ".id = " + String.valueOf(itemID);

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        //cursor.moveToPosition(itemID);

        /*String address = cursor.getString(cursor.getColumnIndex("street"));
        if (cursor.getString(cursor.getColumnIndex("home")).length() > 0) {
            address += ", " + cursor.getString(cursor.getColumnIndex("home"));
        }*/

        imageView.setImageBitmap(BitmapFactory.decodeByteArray(cursor.getBlob(cursor.getColumnIndex("image")),
                0, cursor.getBlob(cursor.getColumnIndex("image")).length));
        nameView.setText(cursor.getString((cursor.getColumnIndex("name"))));

        String desc = "<p align = center><i>" + cursor.getString((cursor.getColumnIndex("description"))) + "</i><br><br>";
        switch (SqlGroup.str){
            case "fun": case "museum": case "sport":
                site = cursor.getString(cursor.getColumnIndex("site"));
                desc += "Сайт: <a href = " + site + ">Сайт</a>";
                break;
            default:
                site = cursor.getString(cursor.getColumnIndex("wiki"));
                desc += "Сайт: <a href = " + site + ">Википедия</a>";
                //desc += "Wiki: <a href = http://yandex.ru>"+ site + "</a>";
        }
        descView.loadData(desc, "text/html", "UTF-8");
        lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex("lat")));
        lon = Double.parseDouble(cursor.getString(cursor.getColumnIndex("lon")));
        //descView.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));

        /*((TextView) findViewById(R.id.textView4)).setText(cursor.getString((cursor.getColumnIndex("site"))));
        ((TextView) findViewById(R.id.textView6)).setText(cursor.getString((cursor.getColumnIndex("phone"))));*/


        cursor.close();;
        db.close();;
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