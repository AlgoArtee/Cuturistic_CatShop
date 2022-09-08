package ModuleServer.persistence;

import ModuleCommon.valueobjects.Item;
import ModuleCommon.valueobjects.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Malla Mirza, Chachulski, Mathea
 *
 * General interface for access to a storage medium (file or database)
 * Uses include adding Item, Costumers or Employee etc. data.
 *
 * Classes who need a persistence interface can implement it
 */
public interface PersistenceManager {

	void openForReading(String datasource) throws IOException;

	void openForWriting(String datasource) throws IOException;

	boolean close() throws IOException;

	/**
	 * Method for Reading Item data from an external data source
	 *
	 * @return Item-Object, if reading was successful
	 */
	//Item loadItem() throws IOException;

	/**
	 * Method for Reading Item data from an external data source
	 *
	 * @param i Item Object to be saved to an external data source / file
	 * @return true, if writing was successful, otherwise false
	 */
	boolean saveItem(Item i) throws IOException;

	public Map<Integer, Item> readItemList() throws IOException, ClassNotFoundException;
	public boolean writeItemList(Map<Integer, Item> itemList) throws IOException;

	public boolean writeLogEntries(List<String> entries) throws IOException;
	public List<String> readLogList() throws IOException, ClassNotFoundException;

	public boolean writeUserList(Map<String, User> users) throws IOException;

	public Map<String, User> readUserList() throws IOException, ClassNotFoundException;
}