package com.sdplab.twitterstat.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

@Dao
public interface TwittDao {

    @Query("Select * from twitt")
    List<Twitt> getAll();

    @Query("Select user from twitt")
    List<String> getUsers();

    @Insert
    void insertAll(Twitt... twitts);


}
