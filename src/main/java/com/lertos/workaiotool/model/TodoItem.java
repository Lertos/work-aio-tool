package com.lertos.workaiotool.model;

import java.io.Serializable;
import java.util.Objects;

public class TodoItem implements Serializable {

    private boolean isDone;
    private String description;
    private String additionalText;

    public TodoItem(boolean isDone, String description, String additionalText) {
        this.isDone = isDone;
        this.description = description;
        this.additionalText = additionalText;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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

        TodoItem todoItem = (TodoItem) o;

        return Objects.equals(isDone, todoItem.isDone) && Objects.equals(description, todoItem.description) & Objects.equals(additionalText, todoItem.additionalText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isDone, description, additionalText);
    }
}
