/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pan.mydemo.activity.materialdesign;

/**
 * Represents an Item in our application. Each item has a name, id, full size image url and
 * thumbnail url.
 */
public class Item {

//    private static final String LARGE_BASE_URL = "http://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/large/";
//    private static final String THUMB_BASE_URL = "http://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/thumbs/";
//
//    public static Item[] ITEMS = new Item[] {
//            new Item("Flying in the Light", "Romain Guy", "flying_in_the_light.jpg"),
//            new Item("Caterpillar", "Romain Guy", "caterpillar.jpg"),
//            new Item("Look Me in the Eye", "Romain Guy", "look_me_in_the_eye.jpg"),
//            new Item("Flamingo", "Romain Guy", "flamingo.jpg"),
//            new Item("Rainbow", "Romain Guy", "rainbow.jpg"),
//            new Item("Over there", "Romain Guy", "over_there.jpg"),
//            new Item("Jelly Fish 2", "Romain Guy", "jelly_fish_2.jpg"),
//            new Item("Lone Pine Sunset", "Romain Guy", "lone_pine_sunset.jpg"),
//    };


    private static final String LARGE_BASE_URL = "http://www.keke289.com/Uploads/upload/image/20170330/";
    private static final String THUMB_BASE_URL = "http://www.keke289.com/Uploads/upload/image/20170330/";

    public static Item[] ITEMS = new Item[]{
            new Item("Flying in the Light", "Romain Guy", "1490856803929010_n_200_150.jpg"),
            new Item("Caterpillar", "Romain Guy", "1490846924225784_n_200_150.jpg"),
            new Item("Look Me in the Eye", "Romain Guy", "1490842267307855_n_200_150.jpg"),
            new Item("Flamingo", "Romain Guy", "1490842580319704_n_200_150.jpg"),
            new Item("Rainbow", "Romain Guy", "1490816017162017_n_200_150.jpg"),
            new Item("Over there", "Romain Guy", "1490812813444434_n_200_150.jpg"),
            new Item("Jelly Fish 2", "Romain Guy", "1490856803929010_n_200_150.jpg"),
            new Item("Lone Pine Sunset", "Romain Guy", "1490846924225784_n_200_150.jpg"),
    };

    public static Item getItem(int id) {
        for (Item item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    private final String mName;
    private final String mAuthor;
    private final String mFileName;

    Item(String name, String author, String fileName) {
        mName = name;
        mAuthor = author;
        mFileName = fileName;
    }

    public int getId() {
        return mName.hashCode() + mFileName.hashCode();
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return LARGE_BASE_URL + mFileName;
    }

    public String getThumbnailUrl() {
        return THUMB_BASE_URL + mFileName;
    }

}
