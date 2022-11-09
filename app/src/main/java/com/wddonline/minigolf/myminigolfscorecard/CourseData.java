package com.wddonline.minigolf.myminigolfscorecard;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Mobile World on 6/12/2018.
 */
public class CourseData {
    public String NAME;
    public String HALFCOURSE;
    public String COURSEID;
    public JSONArray HOLES;
    public int total;
    public CourseData(String NAME,
            String HALFCOURSE,
            String COURSEID,
            JSONArray HOLES,
                      int total){
        this.NAME = NAME;
        this.HALFCOURSE = HALFCOURSE;
        this.COURSEID = COURSEID;
        this.HOLES = HOLES;
        this.total = total;
    }

}
