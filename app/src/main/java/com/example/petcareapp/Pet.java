package com.example.petcareapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

public class Pet {
    String opis;
    String ime;
    String telBroj;
    String imgUrl;
    LatLng lokacija;

    public Pet(String opis, String ime, String telBroj, String imgUrl, LatLng lokacija) {
        this.opis = opis;
        this.ime = ime;
        this.telBroj = telBroj;
        this.imgUrl = imgUrl;
        this.lokacija = lokacija;
    }

    public Pet(String opis,String imgUrl) {
        this.opis = opis;
        this.imgUrl = imgUrl;
    }

    public Pet(DocumentSnapshot snapshot) {
        String tempDescription;
        if((tempDescription = (String) snapshot.get("Opis")) != null){
            this.opis = tempDescription;
        }
        String tempURL;
        if((tempURL = (String) snapshot.get("imageDownloadLink")) != null){
            this.imgUrl = tempURL;
        }
    }

//    public Pet(DocumentSnapshot snapshot){
//        GeoPoint tempGeopoint;
//        if ((tempGeopoint = (GeoPoint) snapshot.get("location"))!= null){
//            this.lokacija = new LatLng(Double.valueOf(tempGeopoint.getLatitude()),Double.valueOf(tempGeopoint.getLongitude()));
//        }
//        String tempName;
//        if ((tempName = (String) snapshot.get("Info")) != null){
//            this.ime = tempName;
//        }
//        String tempDescription;
//        if((tempDescription = (String) snapshot.get("Opis")) != null){
//            this.opis = tempDescription;
//        }
//        String tempPhone;
//        if ((tempPhone = (String) snapshot.get("phone")) != null){
//            this.telBroj = tempPhone;
//        }
//        String tempURL;
//        if((tempURL = (String) snapshot.get("imageDownloadLink")) != null){
//            this.imgUrl = tempURL;
//        }
//    }
}
