package models.semantic;

import com.hp.hpl.jena.rdf.model.Resource;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: pedrocarmona
 * Date: 14/01/14
 * Time: 07:41
 * To change this template use File | Settings | File Templates.
 */
public class ResultRecord {
    public Resource resource;
    public int count;
    public ArrayList<String> sentences;

    public ResultRecord(Resource resource) {
        this.resource = resource;
        this.sentences = new ArrayList<String>();
    }
}
