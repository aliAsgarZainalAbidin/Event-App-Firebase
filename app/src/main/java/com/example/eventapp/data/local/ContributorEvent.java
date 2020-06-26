package com.example.eventapp.data.local;

public class ContributorEvent {
    private int level;
    private boolean kehadiran;

    public ContributorEvent(int level, boolean kehadiran) {
        this.level = level;
        this.kehadiran = kehadiran;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(boolean kehadiran) {
        this.kehadiran = kehadiran;
    }
}
