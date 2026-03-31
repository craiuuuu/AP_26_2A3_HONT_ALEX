package lab5.tema5;


public class Main
{
    public static void main(String[] args) {
        Catalog catalog = new Catalog("Bibliografie_Tema5");

        Book book = new Book("knuth67", "The Art of Computer Programming", "d:/books/tacp.pdf", "Donald E. Knuth", "1967");
        Article article = new Article("java25", "The Java Language Specification", "https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf", "Oracle");

        catalog.add(book);
        catalog.add(article);

        try {
            System.out.println("--- Testăm Comanda List ---");
            new ListCommand(catalog).execute();

            System.out.println("\n--- Testăm Comanda View ---");
            System.out.println("Se deschide browser-ul pentru articol...");
            new ViewCommand(article).execute();

            System.out.println("\n--- Testăm Comanda Report ---");
            System.out.println("Se generează și se deschide raportul HTML...");
            new ReportCommand(catalog).execute();

        } catch (InvalidCatalogException e) {
            System.err.println("Eroare: " + e.getMessage());
        }
    }
}