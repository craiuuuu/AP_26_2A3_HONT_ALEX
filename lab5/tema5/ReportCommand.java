package lab5.tema5;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lab5.tema5.InvalidCatalogException;
import lab5.tema5.Catalog;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class ReportCommand implements Command {
    private Catalog catalog;

    public ReportCommand(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void execute() throws InvalidCatalogException {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setDirectoryForTemplateLoading(new File("."));

            Template template = cfg.getTemplate("report.ftl");
            File reportFile = new File("raport.html");

            try (Writer fileWriter = new FileWriter(reportFile)) {
                template.process(catalog, fileWriter);
            }
            Desktop.getDesktop().browse(reportFile.toURI());
        } catch (Exception e) {
            throw new InvalidCatalogException("Eroare la raport", e);
        }
    }
}