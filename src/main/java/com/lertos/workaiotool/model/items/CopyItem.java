package com.lertos.workaiotool.model.items;

import java.io.Serializable;
import java.util.Objects;

public class CopyItem implements Serializable {

    private String description;
    private String textToCopy;

    public CopyItem(String description, String textToCopy) {
        this.description = description;
        this.textToCopy = textToCopy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTextToCopy() {
        return textToCopy;
    }

    public void setTextToCopy(String textToCopy) {
        this.textToCopy = textToCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || this.getClass() != o.getClass())
            return false;

        CopyItem folderItem = (CopyItem) o;

        return Objects.equals(description, folderItem.description) && Objects.equals(textToCopy, folderItem.textToCopy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, textToCopy);
    }
}
