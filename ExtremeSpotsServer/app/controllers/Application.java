package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.semantic.Search;
import play.mvc.BodyParser.Json;
import play.mvc.BodyParser.Json;
import play.*;
import play.mvc.*;
import static play.libs.Json.toJson;


import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready :)."));
    }

    public static Result semanticsearch(String query) {

        Search s = new Search();
        s.search(query);
//        return ok(s.resultsHasJson());
        return ok(toJson(query));

    }



}
