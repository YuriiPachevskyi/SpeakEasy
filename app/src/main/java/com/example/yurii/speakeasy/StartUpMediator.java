package com.example.yurii.speakeasy;

import android.app.Fragment;

public interface StartUpMediator {
    public static enum PAGE_TYPE {
        LESSONS(0),
        LESSON(1),
        SPEAKING(2),
        MATERIAL(3),
        TAGS(4),
        TAG(5);

        private final int value;
        private PAGE_TYPE(int value) { this.value = value; }
        public int getValue() { return value; }
        public static PAGE_TYPE fromInteger(int x) {
            switch(x) {
                case 0: return LESSONS;
                case 1: return LESSON;
                case 2: return SPEAKING;
                case 3: return MATERIAL;
                case 4: return TAGS;
                case 5: return TAG;
            }
            return null;
        }
    }

    public void showMaterial(String tag);
    public void showLesson(int lesson);
    public void updateActionBarColor(int position);
}
