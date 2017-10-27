package com.xxxgreen.mvx.golfdotzio;

import android.content.ContentValues;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_1;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_2;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.COL_3;
import static com.xxxgreen.mvx.golfdotzio.Schema.SheetColumns.TABLE_NAME;

public class StyleActivity extends AppCompatActivity {
    private static final String TAG = "StyleActivity";
    private Style xStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        xStyle = getIntent().getParcelableExtra("style");

        getSupportActionBar().setTitle(xStyle.STYLE_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView styleName = (TextView) findViewById(R.id.style_name);
        final TextView styleNum = (TextView) findViewById(R.id.style_num);
        final EditText numSheets = (EditText) findViewById(R.id.et_sheets);
        final EditText numBackingCards = (EditText) findViewById(R.id.et_backing_cards);
        final EditText location = (EditText) findViewById(R.id.et_location);

        //make popular & wholesale icons in the toolbar
        MaterialFavoriteButton toolbarFavorite = new MaterialFavoriteButton.Builder(this) //
                .favorite(xStyle.IS_POP > 0)
                .color(MaterialFavoriteButton.STYLE_WHITE)
                .type(MaterialFavoriteButton.STYLE_STAR)
                .rotationDuration(400)
                .create();
        toolbar.addView(toolbarFavorite);
        MaterialFavoriteButton toolbarWholesale = new MaterialFavoriteButton.Builder(this) //
                .favorite(xStyle.IS_WHOLESALE > 0)
                .color(MaterialFavoriteButton.STYLE_WHITE)
                .type(MaterialFavoriteButton.STYLE_HEART)
                .rotationDuration(400)
                .create();
        toolbar.addView(toolbarWholesale);
        toolbarWholesale.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        //TODO: change value of style.IS_WHOLESALE
                    }
                });

        //initialize cancel and save buttons
        Button b_cancel = (Button) findViewById(R.id.b_cancel);
        Button b_save = (Button) findViewById(R.id.b_save);
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDatabaseManager dbm = XDatabaseManager.getInstance(StyleActivity.this);

                String code = styleNum.getText().toString();
                String name = styleName.getText().toString();
                int sheets = Integer.valueOf(numSheets.getText().toString());
                int cards = Integer.valueOf(numBackingCards.getText().toString());
                String loc = location.getText().toString();
                int isPop = -1;
                int isWholesale = -1;

                Style style = new Style(code, name, sheets, cards, loc, isPop, isWholesale);
                dbm.replaceRecord(style, style.STYLE_CODE);
                finish();
            }
        });

        //convert integer values to Strings
        String sheetsText = "" + xStyle.SHEETS;
        String backingCardsText = "" + xStyle.BACKING_CARDS;

        //display Style values
        styleName.setText(xStyle.STYLE_NAME);
        styleNum.setText(xStyle.STYLE_CODE);
        numSheets.setText(sheetsText);
        numBackingCards.setText(backingCardsText);
        location.setText(xStyle.LOCATION);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
