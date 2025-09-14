package org.example.data_access_layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.domain_layer.Receta;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecetaFileStore implements IFileStore<Receta> {

    private static final Logger LOGGER = Logger.getLogger(RecetaFileStore.class.getName());
    private final File xmlFile;

    public RecetaFileStore(File file) {
        this.xmlFile = file;
        ensureFile();
    }

    @Override
    public List<Receta> readAll() {
        List<Receta> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            var doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Receta.class);
            Unmarshaller u = ctx.createUnmarshaller();

            var nodes = doc.getElementsByTagName("receta");
            for (int i = 0; i < nodes.getLength(); i++) {
                var node = nodes.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Receta r = (Receta) u.unmarshal(node);
                    out.add(r);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error reading " + xmlFile, ex);
        }
        return out;
    }

    @Override
    public void writeAll(List<Receta> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Receta.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("recetas");

            if (data != null) {
                for (Receta r : data) {
                    m.marshal(r, xw);
                }
            }
            xw.writeEndElement();
            xw.writeEndDocument();
            xw.flush();
            xw.close();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error writing " + xmlFile, ex);
        }
    }

    private void ensureFile() {
        try {
            File parent = xmlFile.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    LOGGER.warning("Failed to create directories for " + parent);
                }
            }
            if (!xmlFile.exists()) {
                if (!xmlFile.createNewFile()) {
                    LOGGER.warning("Failed to create file " + xmlFile);
                } else {
                    writeAll(new ArrayList<>());
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error ensuring file " + xmlFile, ex);
        }
    }
}
