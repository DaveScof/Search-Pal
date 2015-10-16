package com.zeila.SearchPal;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.zeila.SearchPal.R;
import com.zeila.SearchPal.R.layout;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

public class SearchPal extends Activity {

	private Switch safeSearchSwitch;
	private Switch adTestSwitch;
	private EditText stSearchEditText;
	private Button searchButton;
	private Button loadButton;
	private RadioButton stdSearchRadioButton;
	private RadioButton adSearchRadioButton;
	private RadioButton imgSearchRadioButton;
	private RadioButton otherSearchesRadioButton;
	private TableLayout scrollViewTableLayout;

	// Variables in the advanced search layout
	private EditText adSearchMustEditText;
	private Button adSearchMustAddButton;
	private EditText adSearchNotEditText;
	private Button adSearchNotAddButton;
	private EditText adSearchWebsiteEditText;
	private Button adSearchWebsiteSetButton;

	private TableLayout adSearchMustTableLayout;
	private TableLayout adSearchNotTableLayout;
	private TableLayout adSearchWebsiteTebleLayout;

	private Spinner adSearchFileTypeSpinner;

	private CheckBox adImageSearchCheckBox;
	private CheckBox adWebSearchCheckBox;
	private CheckBox adVideoSearchCheckBox;

	private CheckBox adBlogsearchCheckBox;
	private CheckBox adBooksSearchCheckBox;

	private CheckBox adNewsSearchCheckBox;

	private CheckBox adShoppingSearchCheckBox;

	// Variables in the other Searches Layout
	private RadioButton definitionSearchRadioButton;
	private RadioButton relatedWebRadioButton;
	private RadioButton linkToRadioButton;
	private RadioButton titleRadioButton;
	private RadioButton urlRadioButton;
	private RadioButton anchorRadioButton;

	private TableRow definitionTableRow;
	private TableRow relatedTableRow;
	private TableRow linkToTableRow;
	private TableRow titleTableRow;
	private TableRow urlTableRow;
	private TableRow anchorTableRow;

	private EditText otherSearchEditText;
	// Other Variables
	private boolean mustTermsPresent = false;
	private boolean notTermsPresent = false;
	private boolean websitePresent = false;
	private int mustTermsCount = 0;
	private int notTermsCount = 0;
	ArrayList<String> mustTerms;
	ArrayList<String> notTerms;
	private String websiteToSearch;
	private boolean showChangeSearchDialogue = false;
	private boolean changeSearchTypeContinue = false;

	// private boolean fileTypeSelected=false;
	// private boolean indexTimeSelected=false;
	// private boolean fileRightSelected=false;

	// Spinner Variables
	private int fileTypeChoice = 0;
	private int indexTimeChoice = 0;
	private int rightsChoice = 0;

	// Switches Variables

	// CustomEditText Inflation variables
	private TableLayout fileTypeCustomTableLayout;
	private TableRow indexTimeCustomTableRow;
	private EditText customFileTypeEditText;
	private EditText customIndexTimeEditText;
	// Variables for switches Safe Search and Adtest
	private boolean safeSearchOn = true;
	private boolean adTestOn = false;

	private boolean adImageSearch = false;
	private boolean adVideoSearch = false;
	private boolean adWebSearch = true;

	private boolean adBlogsSearch = false;
	private boolean adBooksSearch = false;

	private boolean adNewsSearch = false;

	private boolean adShoppingSearch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_pal_main);
		// Switches
		safeSearchSwitch = (Switch) findViewById(R.id.safeSearchSwitch);
		adTestSwitch = (Switch) findViewById(R.id.adTestSwitch);

		// Edit Texts
		stSearchEditText = (EditText) findViewById(R.id.standardSearchEditText);

		// Buttons
		searchButton = (Button) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(searchButtonListener);
		loadButton = (Button) findViewById(R.id.loadButton);

		// Radio Buttons
		stdSearchRadioButton = (RadioButton) findViewById(R.id.standardSearchRadioButton);
		stdSearchRadioButton.setOnClickListener(stdSearchRadioButtonListener);

		adSearchRadioButton = (RadioButton) findViewById(R.id.advancedSearchRadioButton);
		adSearchRadioButton.setOnClickListener(adSearchRadioButtonListener);

		imgSearchRadioButton = (RadioButton) findViewById(R.id.imageSearchRadioButton);
		imgSearchRadioButton.setOnClickListener(imgSearchRadioButtonListener);

		otherSearchesRadioButton = (RadioButton) findViewById(R.id.otherSearchesRadioButton);
		otherSearchesRadioButton
				.setOnClickListener(otherSearchesRadioButtonListener);

		scrollViewTableLayout = (TableLayout) findViewById(R.id.ScrollViewTableLayout);

		// Variables in the Advanced search layout

		// Other Variables
		mustTerms = new ArrayList<String>();
		notTerms = new ArrayList<String>();
	}

	// Standard Search Radio Listner
	public OnClickListener stdSearchRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			int allterms = mustTermsCount + notTermsCount;
			if (allterms != 0) {
				stdSearchRadioButton.setChecked(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchPal.this);
				builder.setTitle(R.string.change_search_type_title);
				builder.setPositiveButton(R.string.continue_string,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								stdRadioAction();
							}
						});
				builder.setCancelable(true);
				builder.setNegativeButton(R.string.cancel, null);

				builder.setMessage(R.string.change_search_type_message);

				AlertDialog confirmDialog = builder.create();
				confirmDialog.show();
			} else {
				stdRadioAction();
			}
		}
	};

	public OnClickListener adSearchRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			int allterms = mustTermsCount + notTermsCount;
			if (imgSearchRadioButton.isChecked()
					|| stdSearchRadioButton.isChecked()
					|| otherSearchesRadioButton.isChecked()) {

				if (allterms == 0) {
					adRadioAction();
				} else {
					adSearchRadioButton.setChecked(false);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SearchPal.this);
					builder.setTitle(R.string.change_search_type_title);
					builder.setPositiveButton(R.string.continue_string,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									adRadioAction();
								}
							});
					builder.setCancelable(true);
					builder.setNegativeButton(R.string.cancel, null);

					builder.setMessage(R.string.change_search_type_message);

					AlertDialog confirmDialog = builder.create();
					confirmDialog.show();
				}
			}
		}
	};

	// Image Radio Button Listener
	public OnClickListener imgSearchRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			int allterms = mustTermsCount + notTermsCount;
			if (allterms == 0) {
				imgRadoiAction();
			} else {
				imgSearchRadioButton.setChecked(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchPal.this);
				builder.setTitle(R.string.change_search_type_title);
				builder.setPositiveButton(R.string.continue_string,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								imgRadoiAction();
							}
						});
				builder.setCancelable(true);
				builder.setNegativeButton(R.string.cancel, null);

				builder.setMessage(R.string.change_search_type_message);

				AlertDialog confirmDialog = builder.create();
				confirmDialog.show();
			}

		}
	};

	// Other Radio Button Listener
	public OnClickListener otherSearchesRadioButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int allterms = mustTermsCount + notTermsCount;
			if (allterms == 0) {
				otherRadioAction();
			} else {

				otherSearchesRadioButton.setChecked(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SearchPal.this);
				builder.setTitle(R.string.change_search_type_title);
				builder.setPositiveButton(R.string.continue_string,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								otherRadioAction();
							}
						});
				builder.setCancelable(true);
				builder.setNegativeButton(R.string.cancel, null);

				builder.setMessage(R.string.change_search_type_message);

				AlertDialog confirmDialog = builder.create();
				confirmDialog.show();
			}
		}
	};

	// AdSearch Must Add Button Listener
	public OnClickListener adSearchMustAddButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			hideSoftKeyboard();

			if (adSearchMustEditText.getText().length() > 0) {

				LayoutInflater termsInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View termsView = termsInflater.inflate(
						R.layout.added_terms_layout, null);
				TextView newTerm = (TextView) termsView
						.findViewById(R.id.newTermTextView);
				Button newDeleteButton = (Button) termsView
						.findViewById(R.id.newRemoveTermButton);
				newDeleteButton.setOnClickListener(deleteButtonListener);
				// String termText =
				// "\""+adSearchMustEditText.getText().toString()+"\"";
				newTerm.setText(adSearchMustEditText.getText().toString());
				// Adding to mustTerms Array;
				mustTerms.add(adSearchMustEditText.getText().toString());
				adSearchMustEditText.setText("");
				// If terms are not present
				if (!mustTermsPresent) {
					View titleView = termsInflater.inflate(
							R.layout.title_layout, null);
					TextView newTitle = (TextView) titleView
							.findViewById(R.id.newTitleTextView);
					newTitle.setText(R.string.must_title);
					adSearchMustTableLayout.addView(titleView);
					mustTermsPresent = true; // Set this value to true
				}

				adSearchMustTableLayout.addView(termsView, 1);
				mustTermsCount++;
			}
		}
	};

	// AdSearch Not Add Button Listener
	public OnClickListener adSearchNotAddButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			hideSoftKeyboard();
			if (adSearchNotEditText.getText().length() > 0) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View notTermsView = inflater.inflate(
						R.layout.added_terms_layout, null);

				if (!notTermsPresent) {
					View notTitleView = inflater.inflate(R.layout.title_layout,
							null);
					TextView notTitle = (TextView) notTitleView
							.findViewById(R.id.newTitleTextView);
					notTitle.setText(R.string.not_title);
					adSearchNotTableLayout.addView(notTitleView);
					notTermsPresent = true;
				}

				TextView newTerm = (TextView) notTermsView
						.findViewById(R.id.newTermTextView);
				newTerm.setText(adSearchNotEditText.getText().toString());
				Button newDeleteButton = (Button) notTermsView
						.findViewById(R.id.newRemoveTermButton);
				newDeleteButton.setOnClickListener(deleteButtonListener);

				// Add to the notTerms Arraylist
				notTerms.add(adSearchNotEditText.getText().toString());
				adSearchNotEditText.setText("");
				adSearchNotTableLayout.addView(notTermsView, 1);
				notTermsCount++;
			}
		}
	};

	// AdSearch Web Site Set Button Listener
	public OnClickListener adSearchWebsiteSetButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			hideSoftKeyboard();

			if (adSearchWebsiteEditText.getText().length() > 0) {

				// set the web site string
				websiteToSearch = adSearchWebsiteEditText.getText().toString();

				// Setting the websitepresent boolean true;
				websitePresent = true;

				// The layout work
				adSearchWebsiteTebleLayout.removeAllViews();
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// Adding The Title
				View webSiteViewTitle = inflater.inflate(layout.title_layout,
						null);
				TextView TitleTextView = (TextView) webSiteViewTitle
						.findViewById(R.id.newTitleTextView);
				TitleTextView.setText(R.string.website_title);
				adSearchWebsiteTebleLayout.addView(webSiteViewTitle);

				// Adding The web site
				View websiteView = inflater.inflate(
						R.layout.added_terms_layout, null);

				TextView newWebsiteTextView = (TextView) websiteView
						.findViewById(R.id.newTermTextView);

				Button newDeleteButton = (Button) websiteView
						.findViewById(R.id.newRemoveTermButton);
				newDeleteButton.setOnClickListener(deleteButtonListener);

				newWebsiteTextView.setText(adSearchWebsiteEditText.getText()
						.toString());
				adSearchWebsiteTebleLayout.addView(websiteView);
				adSearchWebsiteEditText.setText("");
			}
		}
	};

	// The delete button listener
	public OnClickListener deleteButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			TableRow parentTableRow = (TableRow) arg0.getParent();
			TableLayout parentTableLayout = (TableLayout) parentTableRow
					.getParent();
			int index = parentTableLayout.indexOfChild(parentTableRow);

			// Getting The String of the removed

			TextView parentTextView = (TextView) parentTableRow.getChildAt(0);
			String parentString = parentTextView.getText().toString();

			parentTableLayout.removeViewAt(index);

			if (parentTableLayout == adSearchMustTableLayout) {
				mustTermsCount--;
				mustTerms.remove(parentString);
				if (mustTermsCount == 0) {
					adSearchMustTableLayout.removeViewAt(0);
					mustTermsPresent = false;
				}
			}
			if (parentTableLayout == adSearchNotTableLayout) {
				notTermsCount--;
				notTerms.remove(parentString);
				if (notTermsCount == 0) {
					adSearchNotTableLayout.removeViewAt(0);
					notTermsPresent = false;
				}
			}
			if (parentTableLayout == adSearchWebsiteTebleLayout) {
				adSearchWebsiteTebleLayout.removeAllViews();
				websitePresent = false;
				websiteToSearch = "";
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_pal, menu);
		return true;
	}

	public void changeSearchTypeDialogue(RadioButton radioButton) {
		changeSearchTypeContinue = false;
		radioButton.setChecked(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(SearchPal.this);
		builder.setTitle(R.string.change_search_type_title);
		builder.setPositiveButton(R.string.continue_string,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						changeSearchTypeContinue = true;
					}
				});
		builder.setCancelable(true);
		builder.setNegativeButton(R.string.cancel, null);

		builder.setMessage(R.string.change_search_type_message);

		AlertDialog confirmDialog = builder.create();
		confirmDialog.show();
	}

	public void resetCount() {
		mustTermsCount = 0;
		notTermsCount = 0;
		mustTermsPresent = false;
		notTermsPresent = false;
		websitePresent = false;
		stSearchEditText.setText("");
	}

	public void stdRadioAction() {
		stdSearchRadioButton.setChecked(true);
		adSearchRadioButton.setChecked(false);
		imgSearchRadioButton.setChecked(false);
		otherSearchesRadioButton.setChecked(false);
		scrollViewTableLayout.removeAllViews();
		resetCount();
		hideSoftKeyboard();
	}

	public void adRadioAction() {
		stdSearchRadioButton.setChecked(false);
		adSearchRadioButton.setChecked(true);
		imgSearchRadioButton.setChecked(false);
		otherSearchesRadioButton.setChecked(false);

		// Inflating the Advanced Search Layout

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View adSearchView = inflater.inflate(R.layout.advanced_table_layout,
				null);

		// Layouts to be inserted to
		adSearchMustTableLayout = (TableLayout) adSearchView
				.findViewById(R.id.adSearchMustTableLayout);
		adSearchNotTableLayout = (TableLayout) adSearchView
				.findViewById(R.id.adSearchNotTableLayout);
		adSearchWebsiteTebleLayout = (TableLayout) adSearchView
				.findViewById(R.id.adSearchWebsiteTableLayout);

		fileTypeCustomTableLayout = (TableLayout) adSearchView
				.findViewById(R.id.fileTypeCustomTableLayout);

		indexTimeCustomTableRow = (TableRow) adSearchView
				.findViewById(R.id.adSearchTableRow11);
		// Must Variables
		adSearchMustEditText = (EditText) adSearchView
				.findViewById(R.id.adSearchMustEditText);
		adSearchMustAddButton = (Button) adSearchView
				.findViewById(R.id.mustAddButton);
		adSearchMustAddButton.setOnClickListener(adSearchMustAddButtonListener);
		// Not Variables
		adSearchNotEditText = (EditText) adSearchView
				.findViewById(R.id.adSearchNotEditText);
		adSearchNotAddButton = (Button) adSearchView
				.findViewById(R.id.notAddButton);
		adSearchNotAddButton.setOnClickListener(adSearchNotAddButtonListener);
		// Web Variables
		adSearchWebsiteEditText = (EditText) adSearchView
				.findViewById(R.id.adSearchWebSiteEditText);
		adSearchWebsiteSetButton = (Button) adSearchView
				.findViewById(R.id.websiteSetButton);
		adSearchWebsiteSetButton
				.setOnClickListener(adSearchWebsiteSetButtonListener);

		adImageSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchImageCheckBox);
		adWebSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchWebCheckBox);
		adWebSearchCheckBox.setChecked(true);
		adVideoSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchVideoCheckBox);

		adBlogsearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchBlogsCheckBox);
		adBooksSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchBooksCheckBox);

		adNewsSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchNewsCheckBox);

		adShoppingSearchCheckBox = (CheckBox) adSearchView
				.findViewById(R.id.adSearchShoppingCheckBox);

		adImageSearchCheckBox.setOnClickListener(imageSearchCheckListener);
		adWebSearchCheckBox.setOnClickListener(webSearchCheckListener);
		adVideoSearchCheckBox.setOnClickListener(videoSearchCheckListener);

		adBlogsearchCheckBox.setOnClickListener(blogsSearchCheckListener);
		adBooksSearchCheckBox.setOnClickListener(booksSearchCheckListener);

		adNewsSearchCheckBox.setOnClickListener(newsSearchCheckListener);

		adShoppingSearchCheckBox
				.setOnClickListener(shoppingSearchCheckListener);

		scrollViewTableLayout.removeAllViews();
		scrollViewTableLayout.addView(adSearchView);

		// The FileType Spinner
		adSearchFileTypeSpinner = (Spinner) findViewById(R.id.adSearchFileTypeSpinner);
		ArrayAdapter<CharSequence> fileTypeAdapter = ArrayAdapter
				.createFromResource(SearchPal.this, R.array.file_Types_array,
						android.R.layout.simple_spinner_item);
		fileTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adSearchFileTypeSpinner.setAdapter(fileTypeAdapter);

		adSearchFileTypeSpinner
				.setOnItemSelectedListener(fileTypeSelectedListener);
		// The Indexed Spinner
		Spinner adSearchIndexedSpinner = (Spinner) findViewById(R.id.adSearchIndexedSpinner);
		ArrayAdapter<CharSequence> indexedAdapter = ArrayAdapter
				.createFromResource(SearchPal.this, R.array.indexing_time,
						android.R.layout.simple_spinner_item);
		indexedAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adSearchIndexedSpinner.setAdapter(indexedAdapter);
		adSearchIndexedSpinner
				.setOnItemSelectedListener(indexTimeSelectedListener);

		resetCount();
		hideSoftKeyboard();
	}

	public void imgRadoiAction() {
		stdSearchRadioButton.setChecked(false);
		adSearchRadioButton.setChecked(false);
		imgSearchRadioButton.setChecked(true);
		otherSearchesRadioButton.setChecked(false);

		scrollViewTableLayout.removeAllViews();
		resetCount();
		hideSoftKeyboard();
	}

	public void otherRadioAction() {

		stdSearchRadioButton.setChecked(false);
		adSearchRadioButton.setChecked(false);
		imgSearchRadioButton.setChecked(false);
		otherSearchesRadioButton.setChecked(true);

		scrollViewTableLayout.removeAllViews();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View otherSearchesView = inflater.inflate(
				R.layout.other_searches_layout, null);

		definitionSearchRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSearchDefinitionRadioButton);
		definitionSearchRadioButton.setOnClickListener(definitionListener);
		relatedWebRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSrchRelatedWebsitesRadioButton);
		relatedWebRadioButton.setOnClickListener(relatedListener);
		linkToRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSrchWebsitesLinkToRadioButton);
		linkToRadioButton.setOnClickListener(linkedToListener);
		titleRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSrchTitleRadioButton);
		titleRadioButton.setOnClickListener(titleListener);
		urlRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSrchUrlRadioButton);
		urlRadioButton.setOnClickListener(urlListener);
		anchorRadioButton = (RadioButton) otherSearchesView
				.findViewById(R.id.othSrchAnchorRadioButton);
		anchorRadioButton.setOnClickListener(anchorListener);

		definitionTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchDefinitionInflater);
		relatedTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchRelatedToinflater);
		linkToTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchlinkToInflater);
		titleTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchTitleInflater);
		urlTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchUrlInflater);
		anchorTableRow = (TableRow) otherSearchesView
				.findViewById(R.id.othSearchAnchorInflater);

		scrollViewTableLayout.addView(otherSearchesView);
		definitionSearchRadioButton.setChecked(true);

		otherSearchEditTextInflater(definitionTableRow);

		resetCount();
		hideSoftKeyboard();

	}

	public void otherSearchEditTextInflater(TableRow tableRow) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View otherSearchEditTextView = inflater.inflate(
				R.layout.custom_edit_text_layout, null);
		otherSearchEditText = (EditText) otherSearchEditTextView
				.findViewById(R.id.customEditText);
		otherSearchEditText.setHint("Enter Search Term Here");
		tableRow.addView(otherSearchEditTextView);

	}

	public void otherSearchEditTextRemover() {
		definitionSearchRadioButton.setChecked(false);
		relatedWebRadioButton.setChecked(false);
		linkToRadioButton.setChecked(false);
		titleRadioButton.setChecked(false);
		urlRadioButton.setChecked(false);
		anchorRadioButton.setChecked(false);

		definitionTableRow.removeAllViews();
		relatedTableRow.removeAllViews();
		linkToTableRow.removeAllViews();
		titleTableRow.removeAllViews();
		urlTableRow.removeAllViews();
		anchorTableRow.removeAllViews();
	}

	OnClickListener definitionListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			definitionSearchRadioButton.setChecked(true);
			otherSearchEditTextInflater(definitionTableRow);
		}
	};

	OnClickListener relatedListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			relatedWebRadioButton.setChecked(true);
			otherSearchEditTextInflater(relatedTableRow);
			otherSearchEditText.setHint("www.example.com");
			otherSearchEditText.setHintTextColor(Color.BLACK);
		}
	};

	OnClickListener linkedToListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			linkToRadioButton.setChecked(true);
			otherSearchEditTextInflater(linkToTableRow);
			otherSearchEditText.setHint("www.example.com");
			otherSearchEditText.setHintTextColor(Color.BLACK);
		}
	};

	OnClickListener titleListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			titleRadioButton.setChecked(true);
			otherSearchEditTextInflater(titleTableRow);
		}
	};

	OnClickListener urlListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			urlRadioButton.setChecked(true);
			otherSearchEditTextInflater(urlTableRow);
		}
	};

	OnClickListener anchorListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			otherSearchEditTextRemover();
			anchorRadioButton.setChecked(true);
			otherSearchEditTextInflater(anchorTableRow);
		}
	};

	// Hides the Soft Keyboard
	public void hideSoftKeyboard() {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(stdSearchRadioButton.getWindowToken(),
						0);
	}

	public OnClickListener searchButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (safeSearchSwitch.isChecked()) {
				safeSearchOn = true;
			} else {
				safeSearchOn = false;
			}
			if (adTestSwitch.isChecked()) {
				adTestOn = true;
			} else {
				adTestOn = false;
			}

			SearchQuery query = new SearchQuery();
			if (adSearchRadioButton.isChecked()) {
				String search = query.adSearchQuery(mustTermsPresent,
						notTermsPresent, websitePresent, mustTerms, notTerms,
						websiteToSearch, fileTypeChoice, indexTimeChoice,
						rightsChoice, safeSearchOn, adTestOn);
				search += "q=" + stSearchEditText.getText().toString() + "&";
				if (fileTypeChoice == 23)
					search += "as_filetype="
							+ customFileTypeEditText.getText().toString() + "&";

				if (indexTimeChoice == 5)
					search += "as_qdr=m"
							+ customIndexTimeEditText.getText().toString()
							+ "&";

				if (adImageSearch)
					search += "tbm=isch";
				if (adVideoSearch)
					search += "tbm=vid";

				if (adBlogsSearch)
					search += "tbm=blg";
				if (adBooksSearch)
					search += "tbm=bks";

				if (adNewsSearch)
					search += "tbm=nws";

				if (adShoppingSearch)
					search += "tbm=shop";

				Intent getURL = new Intent(Intent.ACTION_VIEW,
						Uri.parse(search));
				startActivity(getURL);
			}
			if (stdSearchRadioButton.isChecked()) {
				String search = "https://www.google.com/search?q="
						+ stSearchEditText.getText().toString();
				if (safeSearchOn) {
					search += "&safe=active";
				}
				if (adTestOn) {
					search += "&adtest=on";
				}
				Intent getUrl = new Intent(Intent.ACTION_VIEW,
						Uri.parse(search));
				startActivity(getUrl);
			}
			if (otherSearchesRadioButton.isChecked()) {

				String search = "https://www.google.com/search?";
				if (definitionSearchRadioButton.isChecked()) {
					search += "q=define%3A"
							+ otherSearchEditText.getText().toString();
				}
				if (relatedWebRadioButton.isChecked()) {
					search += "as_rq="
							+ otherSearchEditText.getText().toString();
				}
				if (linkToRadioButton.isChecked()) {
					search += "as_lq="
							+ otherSearchEditText.getText().toString();
				}
				if (titleRadioButton.isChecked()) {
					search += "q=allintitle%3A"
							+ otherSearchEditText.getText().toString();
				}
				if (urlRadioButton.isChecked()) {
					search += "q=allinurl%3A"
							+ otherSearchEditText.getText().toString();
				}
				if (anchorRadioButton.isChecked()) {
					search += "q=allinanchor%3A"
							+ otherSearchEditText.getText().toString();
				}
				Intent getUrl = new Intent(Intent.ACTION_VIEW,
						Uri.parse(search));
				startActivity(getUrl);
			}
		}
	};
	public OnItemSelectedListener fileTypeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			fileTypeChoice = arg2;
			if (arg2 == 23) {
				inflateFileTypeCustomView();

			} else {
				fileTypeCustomTableLayout.removeAllViews();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};
	public OnItemSelectedListener indexTimeSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			indexTimeChoice = arg2;
			if (arg2 == 5) {
				inflateindexTimeCustomView();
			} else {
				indexTimeCustomTableRow.removeAllViews();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	};

	public void inflateFileTypeCustomView() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fileTypeCustomView = inflater.inflate(
				R.layout.custom_edit_text_layout, null);
		customFileTypeEditText = (EditText) fileTypeCustomView
				.findViewById(R.id.customEditText);
		customFileTypeEditText.setHint("Example pdf, txt, ppt, doc");
		customFileTypeEditText
				.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
		fileTypeCustomTableLayout.addView(fileTypeCustomView);
	}

	public void inflateindexTimeCustomView() {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View indexTimeCustomView = inflater.inflate(
				R.layout.custom_edit_text_layout, null);
		customIndexTimeEditText = (EditText) indexTimeCustomView
				.findViewById(R.id.customEditText);
		customIndexTimeEditText.setHint("The previous n months");
		customIndexTimeEditText
				.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		indexTimeCustomTableRow.addView(indexTimeCustomView);
	}

	public OnClickListener videoSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adVideoSearch = true;
			adVideoSearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adImageSearchCheckBox.setChecked(false);

			adBlogsearchCheckBox.setChecked(false);
			adBooksSearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);
			adShoppingSearchCheckBox.setChecked(false);
		}
	};

	public OnClickListener webSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adWebSearch = true;
			adWebSearchCheckBox.setChecked(true);
			adImageSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBlogsearchCheckBox.setChecked(false);
			adBooksSearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);

			adShoppingSearchCheckBox.setChecked(false);

		}
	};

	public OnClickListener imageSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adImageSearch = true;
			adImageSearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBlogsearchCheckBox.setChecked(false);
			adBooksSearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);

			adShoppingSearchCheckBox.setChecked(false);

		}
	};

	public OnClickListener blogsSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adBlogsSearch = true;
			adBlogsearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adImageSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBooksSearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);

			adShoppingSearchCheckBox.setChecked(false);
		}
	};
	public OnClickListener booksSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adBooksSearch = true;
			adBooksSearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adImageSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBlogsearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);

			adShoppingSearchCheckBox.setChecked(false);
		}
	};

	public OnClickListener newsSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adNewsSearch = true;
			adNewsSearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adImageSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBlogsearchCheckBox.setChecked(false);
			adBooksSearchCheckBox.setChecked(false);

			adShoppingSearchCheckBox.setChecked(false);
		}
	};

	public OnClickListener shoppingSearchCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			setAllFalse();
			adShoppingSearch = true;
			adShoppingSearchCheckBox.setChecked(true);
			adWebSearchCheckBox.setChecked(false);
			adImageSearchCheckBox.setChecked(false);
			adVideoSearchCheckBox.setChecked(false);

			adBooksSearchCheckBox.setChecked(false);
			adBlogsearchCheckBox.setChecked(false);

			adNewsSearchCheckBox.setChecked(false);
		}
	};

	public void setAllFalse() {
		adWebSearch = false;
		adImageSearch = false;
		adVideoSearch = false;
		adBlogsSearch = false;
		adBooksSearch = false;
		adNewsSearch = false;
		adShoppingSearch = false;
	}
}
