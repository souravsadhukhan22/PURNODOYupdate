package com.geniustechnoindia.purnodaynidhi.model;


import androidx.annotation.NonNull;

public class RelationSetGet {
    private String relationName;
    private int relationId;

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }


    @NonNull
    @Override
    public String toString() {
        return relationName;
    }
}
