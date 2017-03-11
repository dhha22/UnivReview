package com.univreview.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidHa on 2017. 3. 11..
 */
public class RecentReviewModel implements Serializable {
    public List<RecentReview> cultures = new ArrayList<>();
    public List<RecentReview> majors = new ArrayList<>();

}
