package com.kitkat.android.tcpipsocketserver.model;

import java.io.Serializable;

/**
 * Created by user on 2018-10-25.
 */

public class Member implements Serializable {
    private String id;
    private String pw;


    public Member(String id, String pw) {
        this.id = id;
        this.pw = pw;

    }
    @Override
    public String toString() {
        return String.format("Member{id='%s', pdsfsdfw='%s'}", id, pw);
    }
}