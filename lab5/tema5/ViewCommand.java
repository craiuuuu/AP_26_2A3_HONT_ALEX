package lab5.tema5;
import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class ViewCommand implements Command {
    private Item item;

    public ViewCommand(Item item) {
        this.item = item;
    }

    @Override
    public void execute() throws InvalidCatalogException {
        Desktop desktop = Desktop.getDesktop();
        String location = item.getLocation();

        try {
            if (location.startsWith("http://") || location.startsWith("https://")) {
                desktop.browse(new URI(location));
            } else {
                File file = new File(location);
                if (!file.exists()) {
                    throw new CommandException("Fișierul nu există la: " + location);
                }
                desktop.open(file);
            }
        } catch (Exception e) {
            throw new InvalidCatalogException("Eroare la deschiderea resursei " + item.getTitle(), e);
        }
    }
}