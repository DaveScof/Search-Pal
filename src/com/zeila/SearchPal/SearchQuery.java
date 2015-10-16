package com.zeila.SearchPal;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.app.Activity;

public class SearchQuery {
	private String searchQuery = "https://www.google.com/search?";

	public String getSearchQuery() {
		return searchQuery;
	}

	public String adSearchQuery(boolean mustTermsPresent,
			boolean notTermsPresent, boolean websitePresent,
			ArrayList<String> mustTerms, ArrayList<String> notTerms,
			String websiteToSearch, int fileTypeChoice, int indexTimeChoice,
			int rightsChoice, boolean safeSearchOn, boolean AdTestOn) {
		String adSearchQuery = searchQuery;
		if (mustTermsPresent) {
			adSearchQuery += "q=";
			for (String term : mustTerms) {
				adSearchQuery += "%22" + term + "%22 ";
			}
			adSearchQuery += "&";
		}
		if (notTermsPresent) {
			adSearchQuery += "q=";
			for (String term : notTerms) {
				adSearchQuery += "-" + term + " ";
			}
			adSearchQuery += "&";
		}

		if (websitePresent) {
			adSearchQuery += "as_sitesearch=" + websiteToSearch + "&";
		}

		if (fileTypeChoice != 0 && fileTypeChoice != 23) {
			adSearchQuery += "as_filetype=";
			switch (fileTypeChoice) {
			case 1:
				adSearchQuery += "swf";
				break;
			case 2:
				adSearchQuery += "pdf";
				break;
			case 3:
				adSearchQuery += "ps";
				break;
			case 4:
				adSearchQuery += "dwf";
				break;
			case 5:
				adSearchQuery += "kml";
				break;
			case 6:
				adSearchQuery += "kmz";
				break;
			case 7:
				adSearchQuery += "gpx";
				break;
			case 8:
				adSearchQuery += "hwp";
				break;
			case 9:
				adSearchQuery += "htm";
				break;
			case 10:
				adSearchQuery += "html";
				break;

			case 11:
				adSearchQuery += "xlsx";
				break;
			case 12:
				adSearchQuery += "xls";
				break;
			case 13:
				adSearchQuery += "pptx";
				break;
			case 14:
				adSearchQuery += "ppt";
				break;
			case 15:
				adSearchQuery += "docx";
				break;
			case 16:
				adSearchQuery += "doc";
				break;
			case 17:
				adSearchQuery += "odp";
				break;
			case 18:
				adSearchQuery += "ods";
				break;
			case 19:
				adSearchQuery += "odt";
				break;
			case 20:
				adSearchQuery += "rtf";
				break;
			case 21:
				adSearchQuery += "svg";
				break;
			case 22:
				adSearchQuery += "txt";
				break;
			default:
				break;
			}
			adSearchQuery += "&";
		}

		if (indexTimeChoice != 0 && indexTimeChoice != 5) {
			adSearchQuery += "as_qdr=";
			switch (indexTimeChoice) {
			case 1:
				adSearchQuery += "d";
				break;

			case 2:
				adSearchQuery += "w";
				break;
			case 3:
				adSearchQuery += "m";
				break;
			case 4:
				adSearchQuery += "y";
				break;
			default:
				break;
			}
			adSearchQuery += "&";
		}
		if(safeSearchOn){
			adSearchQuery+="safe=active&";
		}
		if(AdTestOn){
			adSearchQuery+="adtest=on";
		}
		return adSearchQuery;
	}

}