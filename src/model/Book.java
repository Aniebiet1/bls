package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int quantity;
    private int availableQuantity;

    public Book() {}

    public Book(int id, String title, String author, String isbn, int quantity, int availableQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN:" + isbn + ")";
    }
}
