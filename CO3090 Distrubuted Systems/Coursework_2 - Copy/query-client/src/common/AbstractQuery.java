package common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractQuery implements Query{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2756800821958500883L;
	
	//Protected and public allows access in child classes
	protected String queryFileName = "";
	protected Stream<String> dataStream;
	protected List<String> list;

	public AbstractQuery() {
		
	}
	
	public AbstractQuery(String query){
		queryFileName = query;
	}
	
	public void setDataStream(Stream<String> stream) {
		dataStream = stream;
		list = dataStream.collect(Collectors.toList());
	}
	
	public String getFilename() {		
		return queryFileName;
	}
	
	public <T> void print(List<T> list) {
		
		for(int i=0; i<list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		
	}
	
}
