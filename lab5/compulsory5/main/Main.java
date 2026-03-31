package lab5.compulsory5.main;

import lab5.compulsory5.exception.InvalidResourceException;
import lab5.compulsory5.model.Article;
import lab5.compulsory5.model.Book;
import lab5.compulsory5.repository.Catalog;

public class Main
{
    public static void main(String[] args)
    {
        Catalog catalog = new Catalog("MyReferences");

        Book book = new Book("knuth67", "The Art of Computer Programming", "d:/books/tacp.pdf", "Donald E. Knuth", "1967");
        Article article = new Article("java25", "The Java Language Specification", "https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf", "Oracle");

        catalog.add(book);
        catalog.add(article);

        try {
            catalog.openResource(article);
            System.out.println("S-a deschis articolul");
        } catch (InvalidResourceException e)
        {
            System.err.println("A aparut o exceptie la deschidere");
            e.printStackTrace();
        }

        try {
            System.out.println("Deschidem cartea");
            catalog.openResource(book);
            System.out.println("S-a deschis cartea<3");
        } catch (InvalidResourceException e)
        {
            System.err.println("A aparut o exceptie la deschidere" + e.getMessage());
        }
    }
}