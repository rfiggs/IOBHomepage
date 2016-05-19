package edu.hawaii.its.EmployeeIOB.access;

import java.util.*;

public class UhAttributes {

	private Map<String, List<String>> attributes = new HashMap<String, List<String>>();
	private final Map<?, ?> map;


	public UhAttributes(Map map){
		this.map = map;
		if (map != null) {
			for (Object key : map.keySet()) {
				if (key != null && key instanceof String) {
					String k = ((String) key).toLowerCase();
					Object v = map.get(key);
					if (v != null) {
						if (v instanceof String) {
							attributes.put(k, Arrays.asList((String) v));
						} else if (v instanceof List) {
							List<String> lst = new ArrayList<String>();
							for (Object o : (List<?>) v) {
								if (o != null && o instanceof String) {
									lst.add((String) o);
								}
							}
							attributes.put(k, lst);
						}
					}
				}
			}
		}
	}

	public String getUid(){
		return getValue("uid");
	}

	public String getUhUuid(){
		return getValue("uhuuid");
	}

	public String getName(){
		return getValue("cn");
	}

	public List<String> getMail(){
		return Arrays.asList("");
	}

	public List<String> getAffiliation(){
		return Arrays.asList("");
	}

	// Generic methods.

	public String getValue(String name){
		List<String> values = attributes.get(name.toLowerCase());
		return values.isEmpty() ? "" : values.get(0);

	}

	public List<String> getValues(String name){
		List<String> values = attributes.get(name.toLowerCase());
		return values!=null ? values : new ArrayList<String>();
	}

	public Map<?, ?> getMap(){
		return Collections.unmodifiableMap(map);
	}

}
