package org.example.data_access_layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.domain_layer.Medicamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("ClassCanBeRecord")
public class MedicamentoFileStore implements IFileStore<Medicamento> {
    private static final Logger LOGGER = Logger.getLogger(MedicamentoFileStore.class.getName());
    private final File file;

    public MedicamentoFileStore(File file) {
        this.file = file;
        ensureFile();
    }

    @Override
    public List<Medicamento> readAll() {
        try {
            JAXBContext context = JAXBContext.newInstance(MedicamentoListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MedicamentoListWrapper wrapper = (MedicamentoListWrapper) unmarshaller.unmarshal(file);
            return wrapper.getMedicamentos();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error reading medicamentos from file", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void writeAll(List<Medicamento> entities) {
        try {
            JAXBContext context = JAXBContext.newInstance(MedicamentoListWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            MedicamentoListWrapper wrapper = new MedicamentoListWrapper();
            wrapper.setMedicamentos(entities);
            marshaller.marshal(wrapper, file);
        } catch (JAXBException e) {
            LOGGER.log(Level.SEVERE, "Error writing medicamentos to file", e);
        }
    }

    private void ensureFile() {
        try {
            if (!file.exists()) {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    if (!parentDir.mkdirs()) {
                        throw new RuntimeException("Failed to create directories: " + parentDir.getAbsolutePath());
                    }
                }
                if (!file.createNewFile()) {
                    throw new RuntimeException("Failed to create file: " + file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error ensuring file exists: " + file.getAbsolutePath(), e);
        }
    }

    @jakarta.xml.bind.annotation.XmlRootElement(name = "medicamentos")
    @jakarta.xml.bind.annotation.XmlAccessorType(jakarta.xml.bind.annotation.XmlAccessType.FIELD)
    public static class MedicamentoListWrapper {
        private List<Medicamento> medicamentos = new ArrayList<>();

        public List<Medicamento> getMedicamentos() { return medicamentos; }
        public void setMedicamentos(List<Medicamento> medicamentos) { this.medicamentos = medicamentos; }
    }
}