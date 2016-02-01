package com.sys1yagi.longeststreakandroid.db;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

@Table
public class Account implements Parcelable {

    @PrimaryKey
    public long id;

    @Column
    public String name;

    @Column
    public String email;

    @Column
    public String zoneId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.zoneId);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
        this.zoneId = in.readString();
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
