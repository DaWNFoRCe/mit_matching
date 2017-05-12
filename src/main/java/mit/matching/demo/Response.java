package mit.matching.demo;

import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.lib.helper.builder.AbstractProtocolBuilder;

public class Response {
	SInt value;
	AbstractProtocolBuilder builder;
	
	Response(SInt value, AbstractProtocolBuilder builder ){
		this.value= value;
		this.builder=builder;
	}
	
	public SInt getValue() {
		return value;
	}
	public void setValue(SInt value) {
		this.value = value;
	}
	public AbstractProtocolBuilder getBuilder() {
		return builder;
	}
	public void setBuilder(AbstractProtocolBuilder builder) {
		this.builder = builder;
	}

}
