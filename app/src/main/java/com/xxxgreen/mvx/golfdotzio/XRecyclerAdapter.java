package com.xxxgreen.mvx.golfdotzio;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class XRecyclerAdapter extends
        RecyclerView.Adapter<XRecyclerAdapter.StyleHolder> {
    private XDatabaseManager dbm;
    private Cursor mCursor;
    private List<Style> styleList;
    private String sortby;
    OnItemClickListener mItemClickListener;

    public XRecyclerAdapter(List<Style> styleList, Context context, String column) {
        this.styleList = styleList;
        dbm = XDatabaseManager.getInstance(context);
        sortby = "name";

        mCursor = dbm.queryAllDesigns(column);

        while (mCursor.moveToNext()) {
            Style style = new Style(mCursor);
            styleList.add(style);
        }

    }

    public XRecyclerAdapter(List<Style> styleList, Context context, String column, String query) {
        this.styleList = styleList;
        dbm = XDatabaseManager.getInstance(context);
        sortby = "name";

        mCursor = dbm.queryDesignsByName(column, query);

        while (mCursor.moveToNext()) {
            Style style = new Style(mCursor);
            styleList.add(style);
        }

    }

    /* ViewHolder for each Style */
    public class StyleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView styleName;
        public TextView numSheets;

        public StyleHolder(View itemView) {
            super(itemView);

            styleName = (TextView) itemView.findViewById(R.id.style_name);
            numSheets = (TextView) itemView.findViewById(R.id.num_sheets);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            TextView text = (TextView) v.findViewById(R.id.style_name);
            String s = (String) text.getText();
            mItemClickListener.onItemClick(v, getAdapterPosition(), s);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, String s);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public StyleHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);

        return new StyleHolder(view);
    }

    @Override
    public void onBindViewHolder(StyleHolder holder, final int position) {
        final Style style = styleList.get(position);

        String name = style.STYLE_NAME;
        String sheets = "" + style.SHEETS;

        holder.styleName.setText(name);
        holder.numSheets.setText(sheets);
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    /**
     * Return the {@link Style} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Style} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Style getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Style(mCursor);
        }
        return null;
    }

    public Style getStyle(int position) {
        return styleList.get(position);
    }

    public void reverse() {
        Collections.reverse(styleList);
        notifyDataSetChanged();
    }

    public void sort() {
        if (sortby.equals("name")) {
            Collections.sort(styleList, new Sortbysheets());
            sortby = "sheets";
        } else {
            Collections.sort(styleList, new Sortbyname() );
            sortby = "name";
        }
        notifyDataSetChanged();
    }

}

class Sortbysheets implements Comparator<Style> {
    public int compare(Style a, Style b) {
        return a.SHEETS - b.SHEETS;
    }
}

class Sortbyname implements Comparator<Style> {
    public int compare(Style a, Style b) {
        return a.STYLE_NAME.compareTo(b.STYLE_NAME);
    }
}