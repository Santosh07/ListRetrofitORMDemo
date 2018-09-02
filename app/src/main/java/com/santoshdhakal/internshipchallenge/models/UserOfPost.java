package com.santoshdhakal.internshipchallenge.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class UserOfPost {
    @Embedded
    public PostModel postModel;

    @Relation(parentColumn = "userId", entityColumn = "id")
    public List<UserModel> user;
}
