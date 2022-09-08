package ModuleServer.domain;

import java.io.Serializable;
import java.util.Map;

public class LogEntry implements Serializable {

    private String event;
    private String date;
    private Map<String, String> quantity; // how many items (String 1) and what Item (String 2)
    private String name;

    public LogEntry(String event, String date, Map<String, String> quantity, String name){

        this.event = event;
        this.date = date;
        this.quantity = quantity;
        this.name = name;

    }

    public String getEvent() {
        return event;
    }

    public String getDate() {
        return date;
    }

    public Map<String, String> getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
