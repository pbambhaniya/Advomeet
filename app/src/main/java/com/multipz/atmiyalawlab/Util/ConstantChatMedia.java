package com.multipz.atmiyalawlab.Util;

/**
 * Created by Admin on 09-11-2017.
 */

public class ConstantChatMedia {

    private static String[] images = {"png", "jpg", "gif", "jpeg"};
    private static String[] audio = {"mp3", "wav", "3gp"};
    private static String[] video = {"mp4", "m4v", "mp4v", "3gp"};

    public static boolean isImage(String type) {
        for (int i = 0; i < images.length; i++) {
            if (type.toLowerCase().contentEquals(images[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAudio(String type) {
        for (int i = 0; i < audio.length; i++) {
            if (type.toLowerCase().contentEquals(audio[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideo(String type) {
        for (int i = 0; i < video.length; i++) {
            if (type.toLowerCase().contentEquals(video[i])) {
                return true;
            }
        }
        return false;
    }
}
