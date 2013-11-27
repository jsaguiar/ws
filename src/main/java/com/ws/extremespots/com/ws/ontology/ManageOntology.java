package com.ws.extremespots.com.ws.ontology;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import org.apache.log4j.BasicConfigurator;
import java.io.IOException;
import java.io.InputStream;


public class ManageOntology {


        public void importOntology(){
            BasicConfigurator.configure();
            Model model = ModelFactory.createDefaultModel();
            InputStream in = FileManager.get().open("owl/ws_ont.owl");

            if(in == null){
                throw new IllegalArgumentException("File not found");
            }

            model.read(in,null);

            try {
                in.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

        }

        public void main(String args[]) {
            importOntology();
        }


}
