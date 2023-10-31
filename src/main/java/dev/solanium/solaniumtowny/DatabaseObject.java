package dev.solanium.solaniumtowny;

public abstract class DatabaseObject {

    private boolean changed;

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
