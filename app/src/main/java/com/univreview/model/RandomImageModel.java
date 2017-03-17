package com.univreview.model;

import java.util.Random;

/**
 * Created by DavidHa on 2017. 3. 15..
 */
public class RandomImageModel {
    private static String BASE_URL = "https://d1xzq6xjehdeck.cloudfront.net/main/";

    public String getImageURL() {
        int randomNum = makeRandom();
        if (randomNum < 10) {
            return BASE_URL + "0" + randomNum + ".jpg";
        }
        return BASE_URL + randomNum + ".jpg";
    }

    private int makeRandom() {
        Random random = new Random();
        return random.nextInt(31) + 1;
    }
}
