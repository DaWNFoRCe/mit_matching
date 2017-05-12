package mit.matching.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import dk.alexandra.fresco.framework.Application;
import dk.alexandra.fresco.framework.ProtocolFactory;
import dk.alexandra.fresco.framework.ProtocolProducer;
import dk.alexandra.fresco.framework.configuration.CmdLineUtil;
import dk.alexandra.fresco.framework.sce.SCE;
import dk.alexandra.fresco.framework.sce.SCEFactory;
import dk.alexandra.fresco.framework.sce.configuration.SCEConfiguration;
import dk.alexandra.fresco.framework.value.OInt;
import dk.alexandra.fresco.framework.value.SInt;
import dk.alexandra.fresco.lib.compare.ComparisonProtocolFactory;
import dk.alexandra.fresco.lib.field.integer.BasicNumericFactory;
import dk.alexandra.fresco.lib.helper.builder.ComparisonProtocolBuilder;
import dk.alexandra.fresco.lib.helper.builder.NumericIOBuilder;
import dk.alexandra.fresco.lib.helper.builder.NumericProtocolBuilder;

public class Addition implements Application {
private static final long serialVersionUID = 6415583508947017554L;
	
	private int myId, myX;
	private SInt x1, x2;
	public OInt addition;
	
	public Addition(int id, int x) {
		this.myId = id;
		this.myX = x;		
	}

	@Override
	public ProtocolProducer prepareApplication(ProtocolFactory factory) {
		BasicNumericFactory bnFac = (BasicNumericFactory)factory;
		NumericProtocolBuilder npb = new NumericProtocolBuilder(bnFac);
		ComparisonProtocolBuilder cpb = new ComparisonProtocolBuilder((ComparisonProtocolFactory)factory, bnFac);
		NumericIOBuilder iob = new NumericIOBuilder(bnFac);
		// Input points
		iob.beginParScope();
			x1 = (myId == 1) ? iob.input(myX, 1) : iob.input(1);  
			x2 = (myId == 2) ? iob.input(myX, 2) : iob.input(2); 
			 
		iob.endCurScope();
		// Compute distance squared (note, square root computation can be done publicly)
		npb.beginParScope();
			npb.beginSeqScope();
				SInt addX = npb.sub(x1, x2);
				//SInt multX = npb.mult(dX, dX);
			npb.endCurScope();
			
		npb.endCurScope();
		//cpb.beginParScope();
		//	cpb.beginParScope();
		//		SInt compX= cpb.compare(x1, x2);
		//	cpb.endCurScope();
		//cpb.endCurScope();
		SInt result = addX; 
		iob.addProtocolProducer(npb.getProtocol());
		// Output result
		addition = iob.output(result);
		return iob.getProtocol();
	}
	
	public static void main(String[] args) {
		CmdLineUtil cmdUtil = new CmdLineUtil();
		SCEConfiguration sceConf = null;
		int x;
		x  = 0;
		try {	
			cmdUtil.addOption(Option.builder("x")
					.desc("The integer x coordinate of this party. "
							+ "Note only party 1 and 2 should supply this input.")
					.hasArg()
					.build());	
			CommandLine cmd = cmdUtil.parse(args);
			sceConf = cmdUtil.getSCEConfiguration();
			
			if (sceConf.getMyId() == 1 || sceConf.getMyId() == 2) {
				if (!cmd.hasOption("x")) {
					throw new ParseException("Party 1 and 2 must submit input");
				} else {
					x = Integer.parseInt(cmd.getOptionValue("x"));
					
				}
			} else {
				if (cmd.hasOption("x") ) 
					throw new ParseException("Only party 1 and 2 should submit input");
			}
			
		} catch (ParseException | IllegalArgumentException e) {
			System.out.println("Error: " + e);
			System.out.println();
			cmdUtil.displayHelp();
			System.exit(-1);	
		} 
		Addition addition = new Addition(sceConf.getMyId(), x);
		SCE sce = SCEFactory.getSCEFromConfiguration(sceConf);
		try {
			sce.runApplication(addition);
		} catch (Exception e) {
			System.out.println("Error while doing MPC: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		double total = addition.addition.getValue().doubleValue();
		
		System.out.println("Addition between party 1 and 2 is " + total);
	}
	
	


}
