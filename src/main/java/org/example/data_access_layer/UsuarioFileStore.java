package org.example.data_access_layer;

import org.example.domain_layer.Administrador;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Usuario;

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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class UsuarioFileStore implements IFileStore<Usuario> {
    private static final Logger LOGGER = Logger.getLogger(UsuarioFileStore.class.getName());
    private final File xmlFile;

    public UsuarioFileStore(File xmlFile) {
        this.xmlFile = xmlFile;
        ensureFile();
    }

    @Override
    public List<Usuario> readAll() {
        List<Usuario> out = new ArrayList<>();
        if (xmlFile.length() == 0) return out;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            JAXBContext ctx = JAXBContext.newInstance(Usuario.class, Administrador.class, Medico.class, Farmaceuta.class);
            Unmarshaller u = ctx.createUnmarshaller();

            String[] userTags = {"usuario", "administrador", "medico", "farmaceuta"};
            for (String tag : userTags) {
                NodeList nodes = doc.getElementsByTagName(tag);
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    Usuario uObj = (Usuario) u.unmarshal(node);
                    out.add(uObj);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error reading " + xmlFile, ex);
        }
        return out;
    }

    @Override
    public void writeAll(List<Usuario> data) {
        try (FileOutputStream out = new FileOutputStream(xmlFile)) {
            JAXBContext ctx = JAXBContext.newInstance(Usuario.class, Administrador.class, Medico.class, Farmaceuta.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_FRAGMENT, true);

            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            XMLStreamWriter xw = xof.createXMLStreamWriter(out, "UTF-8");

            xw.writeStartDocument("UTF-8", "1.0");
            xw.writeStartElement("usuarios");
            if (data != null) {
                for (Usuario u : data) {
                    m.marshal(u, xw);
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
