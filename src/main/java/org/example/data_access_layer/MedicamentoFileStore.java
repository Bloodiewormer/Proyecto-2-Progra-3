package org.example.data_access_layer;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.example.domain_layer.Medicamento;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoFileStore implements IFileStore<Medicamento> {

    private final File file;

    public MedicamentoFileStore(String filePath) {
        this.file = new File(filePath);
        ensureFile();
    }


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
            e.printStackTrace();
        }
    }

    private void ensureFile() {
        if (!file.exists()) {
            writeAll(new ArrayList<>());
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