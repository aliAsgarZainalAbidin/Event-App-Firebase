package com.example.eventapp.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Event implements Parcelable {
    private String title;
    private String overview;
    private String ruangan;
    private String alamat;
    private String gedung;
    private String waktu;
    private String tanggal;
    private List<String> listEmail;
    private List<String> listBerkas;
    private String owner;
    private String qrCode;

    public Event() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRuangan() {
        return ruangan;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGedung() {
        return gedung;
    }

    public void setGedung(String gedung) {
        this.gedung = gedung;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public List<String> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<String> listEmail) {
        this.listEmail = listEmail;
    }

    public List<String> getListBerkas() {
        return listBerkas;
    }

    public void setListBerkas(List<String> listBerkas) {
        this.listBerkas = listBerkas;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Event(String title, String overview, String ruangan, String alamat, String gedung, String waktu, String tanggal, List<String> listEmail, List<String> listBerkas, String owner, String qrCode) {
        this.title = title;
        this.overview = overview;
        this.ruangan = ruangan;
        this.alamat = alamat;
        this.gedung = gedung;
        this.waktu = waktu;
        this.tanggal = tanggal;
        this.listEmail = listEmail;
        this.listBerkas = listBerkas;
        this.owner = owner;
        this.qrCode = qrCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.ruangan);
        dest.writeString(this.alamat);
        dest.writeString(this.gedung);
        dest.writeString(this.waktu);
        dest.writeString(this.tanggal);
        dest.writeStringList(this.listEmail);
        dest.writeStringList(this.listBerkas);
        dest.writeString(this.owner);
        dest.writeString(this.qrCode);
    }

    protected Event(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.ruangan = in.readString();
        this.alamat = in.readString();
        this.gedung = in.readString();
        this.waktu = in.readString();
        this.tanggal = in.readString();
        this.listEmail = in.createStringArrayList();
        this.listBerkas = in.createStringArrayList();
        this.owner = in.readString();
        this.qrCode = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
