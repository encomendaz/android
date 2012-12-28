package com.alienlabz.packagez.ui.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alienlabz.packagez.R;
import com.alienlabz.packagez.model.Carrier;
import com.alienlabz.packagez.model.Category;
import com.alienlabz.packagez.ui.adapter.CarrierAdapter;
import com.alienlabz.packagez.ui.adapter.CategoryAdapter;
import com.alienlabz.packagez.util.Strings;

/**
 * Edit Package Dialog.
 * 
 * @author Marlon Silva Carvalho
 * @since 1.0.0
 */
public class PackageDialog extends DialogFragment {
	private EditText code;
	private EditText codePrefix;
	private EditText codeSuffix;
	private EditText description;
	private Spinner carrier;
	private AutoCompleteTextView category;
	private Button button;

	/**
	 * Default Constructor.
	 */
	public PackageDialog() {
		setStyle(DialogFragment.STYLE_NORMAL, R.style.EditDialog);
	}

	private boolean validate() {
		boolean result = true;

		if (Strings.isEmpty(code.getText().toString())) {
			Toast.makeText(getActivity(), R.string.fill_postal_code, Toast.LENGTH_SHORT).show();
			code.requestFocus();
			result = false;
		} else if (Strings.isEmpty(category.getText().toString())) {
			Toast.makeText(getActivity(), R.string.fill_category, Toast.LENGTH_SHORT).show();
			category.requestFocus();
			result = false;
		}

		return result;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_packages_edit, null);

		code = (EditText) view.findViewById(R.id.package_code);
		codePrefix = (EditText) view.findViewById(R.id.package_code_prefix);
		codeSuffix = (EditText) view.findViewById(R.id.package_code_suffix);
		description = (EditText) view.findViewById(R.id.package_description);
		carrier = (Spinner) view.findViewById(R.id.package_carrier);
		category = (AutoCompleteTextView) view.findViewById(R.id.package_category);

		codePrefix.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String prefix = codePrefix.getText().toString();
				if (prefix.length() == 2) {
					code.requestFocus();
				}
			}

		});

		code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String prefix = code.getText().toString();
				if (prefix.length() == 9) {
					codeSuffix.requestFocus();
				}
			}

		});

		button = (Button) view.findViewById(R.id.package_button_ok);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validate()) {
					PackageDialogListener listener = (PackageDialogListener) getActivity();

					String strCode = codePrefix.getText().toString() + code.getText().toString()
							+ codeSuffix.getText().toString();

					Category objCategory = Category.findByName(category.getText().toString().trim()).iterator().next();
					if (objCategory == null) {
						objCategory = new Category(category.getText().toString());
					}

					listener.savePackage(strCode, description.getText().toString(), carrier.getSelectedItemId(),
							objCategory);
				}
			}

		});

		category.setAdapter(new CategoryAdapter(getActivity(), Category.findAllNoDefault(getActivity())));
		category.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					category.showDropDown();
				}
			}

		});

		category.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				category.showDropDown();
			}

		});

		carrier.setAdapter(new CarrierAdapter(getActivity(), Carrier.findAll()));
		getDialog().setTitle(R.string.title_dialog_edit);

		return view;
	}

	public void clearFields() {
		code.setText("");
		codePrefix.setText("");
		codeSuffix.setText("");
		description.setText("");
		category.setText("");
	}

	/**
	 * Anyone who wants to receive events from this dialog must implement this method.
	 * By default, the parent Activity must implement this interface.
	 * 
	 * @author Marlon Silva Carvalho
	 * @since 1.0.0
	 */
	public interface PackageDialogListener {

		/**
		 * Event announcing that the user wants to save a package.
		 * 
		 * @param code Code.
		 * @param description Description
		 * @param carrier Carrier.
		 * @param category Category.
		 */
		public void savePackage(String code, String description, Long carrier, Category category);

	}

}
