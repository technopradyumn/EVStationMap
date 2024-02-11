package com.technopradyumn.evstationmap.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Constants {
    public static final String COLLECTION_ENVIRONMENTAL_DATA = "environmentalData";
    public static final String DOCUMENT_STATISTICS = "statistics";
    public static final String USERS = "users";
    public static final String STATIONS = "stations";
    public static final String FEEDBACK = "feedback";
    public static final String API_KEY = "DIRECTION_API_KEY";
    public static final String IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/ev-station-map-app.appspot.com/o/profile_images%2Fpradyumn.jpg?alt=media&token=f9e1aae0-cb02-4b86-a29b-32e0ad2fafb4";

    public static void openLinkInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}