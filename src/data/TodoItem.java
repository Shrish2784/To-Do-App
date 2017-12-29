package data;

import java.time.LocalDate;

public class TodoItem {

    private String title;
    private String details;
    private LocalDate dueDate;

    public TodoItem(String title, String details, LocalDate dueDate) {
        this.title = title;
        this.details = details;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
