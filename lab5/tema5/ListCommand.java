package lab5.tema5;

public class ListCommand implements Command {
    private Catalog catalog;

    public ListCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() {
        System.out.println("Catalog: " + catalog.getName());
        catalog.getItems().forEach(System.out::println);
    }
}