package ModuleServer.domain;

import ModuleServer.persistence.FilePersistenceManager;
import ModuleServer.persistence.PersistenceManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class records all events that modify the inventory inside a logList.
 *
 * @author Chachulski, Mathea
 */
public class Log implements Serializable {

    private LogEntry logEntry;
    //private volatile List<String> logList = new ArrayList<>();
    private List<String> logList = new ArrayList<>();

    PersistenceManager persistenceManager = new FilePersistenceManager();

    public Log(LogEntry logEntry){

        //addHeader();
        this.logEntry = logEntry;

        // load Log File
        try {
            getLogFile();
            } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        //writeLogEntries();

    }

    private void addHeader(){
        String entryString = "Hello Shop Log File";
        this.logList.add(entryString);
        try {
            persistenceManager.writeLogEntries(this.logList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> returnLogList()
    {
        return this.logList;
    }

    public void writeLogEntries(){

        String entryString;
        for (var entry : this.logEntry.getQuantity().entrySet()) {

            entryString=this.logEntry.getEvent() + "\t" + this.logEntry.getDate() + "\t" + entry.getKey() + "\t" + entry.getValue() + "\t by " + this.logEntry.getName();
            this.logList.add(entryString);

        }

        // write Log Entry to File
        try {
            persistenceManager.writeLogEntries(this.logList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getLogFile() throws IOException, ClassNotFoundException {

        this.logList = this.persistenceManager.readLogList();
    }

    public void displayLogList(List<String> logList){

        for(var l : logList) {
            System.out.println(l + "\n");
        }
    }
}
