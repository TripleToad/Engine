import java.util.HashMap;
import java.util.Map;

public class Controller {
	
	private Map<String, Integer> button_mappings;
	private Map<Integer, Boolean> button_values;
	
	/**
	 * Creates a Controller instance with the initial capacity to store init_cap distinct button values
	 * @param init_cap initial number of button values that can be stored in this Controller instance
	 */
	public Controller(int init_cap) {
		button_mappings = new HashMap<>(init_cap);
		button_values = new HashMap<>(init_cap);
	}
	
	/**
	 * Add a button for this controller to record <br>
	 * Values correspond to KeyEvent virtual keyboard values. To find these, <br>
	 * look for VK_<keyname> constants in the KeyEvent documentation.
	 * @param value corresponding to KeyEvent's virtual keyboard
	 */
	public void add(String name, int value) {
		if(button_mappings.get(name) == null && button_values.get(value) == null) {
			button_mappings.put(name, value);
			button_values.put(value, false);
		} else {
			throw new IllegalStateException("Cannot share values or names between buttons!");
		}
	}
	
	/**
	 * Add many buttons for this controller to record
	 * Values correspond to KeyEvent virtual keyboard values. To find these, <br>
	 * look for VK_<keyname> constants in the KeyEvent documentation.
	 * @param names a list of the names for key bindings, like "jump" and "move left"
	 * @param values the key values corresponding to the names
	 */
	public void add(String[] names, int ... values) {
		if(names.length != values.length) {
			throw new IllegalArgumentException("Not a key value for every name!");
		}
		for(int i = 0; i < names.length; i++) {
			add(names[i], values[i]);
		}
	}
	
	/**
	* Determines if this controller has a name assigned to this key value.
	* @param the key value
	* @return true means it does, false means it does not
	*/
	public boolean contains(int keyval) {
		return button_values.containsKey(keyval);
	}
	
	/**
	* Determines if there is a key value assigned to this name.
	* @param name the name
	* @return true means it does, false means it does not
	*/
	public boolean contains(String name) {
		return button_mappings.containsKey(name);
	}
	
	/**
	* This function allows direct control of the values sent out by the Controller. <br>
	* This may be useful in developing computer controlled players, or for debugging.
	* @param button the key value to simulate having this value
	* @param value the value to simulate the button having
	* @return true if the controller is recording this key, false if it is not.
	*/
	public boolean set(int button, boolean value) {
		// If the button is being recorded by this Controller, set its value
		if(button_values.get(button) != null) {
			button_values.put(button, value);
			return true;
		}
		return false;
	}
	
	/**
	* Get the key value assigned to this name
	* @param name the name
	* @return the key value
	*/
	public boolean get(String name) {
		return button_values.get(button_mappings.get(name));
	}
}
