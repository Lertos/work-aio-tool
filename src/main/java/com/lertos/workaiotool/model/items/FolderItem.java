package com.lertos.workaiotool.model.items;

import java.io.Serializable;
import java.util.Objects;

public class FolderItem implements Serializable {

    private String description;
    private String pathToOpen;

    public FolderItem(String description, String pathToOpen) {
        this.description = description;
        this.pathToOpen = pathToOpen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathToOpen() {
        return pathToOpen;
    }

    public void setPathToOpen(String pathToOpen) {
        this.pathToOpen = pathToOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        FolderItem folderItem = (FolderItem) o;

        return Objects.equals(description, folderItem.description) && Objects.equals(pathToOpen, folderItem.pathToOpen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, pathToOpen);
    }
}
