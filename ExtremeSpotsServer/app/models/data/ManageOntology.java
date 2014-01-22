package models.data;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import models.extremespots.StaticVariables;
import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import java.io.InputStream;


public class ManageOntology {


        public static Model importOntology(){
            BasicConfigurator.configure();
            Model model = ModelFactory.createDefaultModel();
            InputStream in = FileManager.get().open(StaticVariables.owl_file);

            if(in == null){
                throw new IllegalArgumentException("File not found");
            }

            model.read(in,null);

            try {
                in.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            return model;

        }

        public void main(String args[]) {
            importOntology();
        }


}
