package com.sys1yagi.longeststreakandroid.db;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
class UserName {

    @PrimaryKey
    public long id;

    @Column
    public String name;

}
