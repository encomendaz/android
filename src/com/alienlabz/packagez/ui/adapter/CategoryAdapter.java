package com.alienlabz.packagez.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Category;
import com.alienlabz.packagez.util.CursorList;

public class CategoryAdapter extends ArrayAdapter<Category> {
	protected Context context;
	private List<Category> suggestions = new ArrayList<Category>();
	private CursorList<Category> defaultList;

	public CategoryAdapter(final Context context, final CursorList<Category> list) {
		super(context, R.id.package_category_item_text1);
		this.context = context;
		this.defaultList = list;
		clearAddDefault();
	}

	@Override
	public long getItemId(int position) {
		return defaultList.get(position).id;
	}

	@Override
	public Category getItem(int position) {
		return defaultList.get(position);
	}
	
	public void clearAddDefault() {
		clear();
		for (Category category : defaultList) {
			add(category);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.package_category_list_item, null);
		}

		final Category type = (Category) getItem(position);
		final TextView textView = (TextView) convertView.findViewById(R.id.package_category_item_text1);
		textView.setText(type.name);

		return convertView;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	final Filter filter = new Filter() {

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(final CharSequence constraint, final FilterResults results) {
			List<Category> result = (ArrayList<Category>) results.values;
			if (result != null && result.size() > 0) {
				clear();
				for (Category category : result) {
					add(category);
				}
				notifyDataSetChanged();
			}
		}

		@Override
		protected FilterResults performFiltering(final CharSequence constraint) {
			final FilterResults filterResults = new FilterResults();

			if (constraint != null) {
				final CursorList<Category> list = Category.findByName(constraint.toString());
				suggestions.clear();

				for (Category category : list) {
					if (constraint == null) {
						suggestions.add(category);
					} else if (category.name.toLowerCase(context.getResources().getConfiguration().locale).startsWith(
							constraint.toString().toLowerCase(context.getResources().getConfiguration().locale))) {
						suggestions.add(category);
					}
				}

				filterResults.values = suggestions;
				filterResults.count = suggestions.size();
			}

			return filterResults;
		}
	};

}
