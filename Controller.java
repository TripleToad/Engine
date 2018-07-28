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
	 * Add a button for this controller to record
	 * @param value
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
	 * @param values
	 */
	public void add(String[] names, int ... values) {
		if(names.length != values.length) {
			throw new IllegalArgumentException("Not a key value for every name!");
		}
		for(int i = 0; i < names.length; i++) {
			add(names[i], values[i]);
		}
	}
	
	public boolean contains(int keyval) {
		return button_values.containsKey(keyval);
	}
	
	public boolean contains(String name) {
		return button_mappings.containsKey(name);
	}
	
	public boolean set(int button, boolean value) {
		// If the button is being recorded by this Controller, set its value
		if(button_values.get(button) != null) {
			button_values.put(button, value);
			return true;
		}
		return false;
	}
	
	public boolean get(String name) {
		return button_values.get(button_mappings.get(name));
	}
}
