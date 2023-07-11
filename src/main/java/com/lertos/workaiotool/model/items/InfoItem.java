package com.lertos.workaiotool.model.items;

import java.io.Serializable;
import java.util.Objects;

public class InfoItem implements Serializable {

    private String description;
    private String additionalText;

    public InfoItem(String description, String additionalText) {
        this.description = description;
        this.additionalText = additionalText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        InfoItem infoItem = (InfoItem) o;

        return Objects.equals(description, infoItem.description) & Objects.equals(additionalText, infoItem.additionalText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, additionalText);
    }
}
