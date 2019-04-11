import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.stringtemplate.v4.ST;

import java.io.FileInputStream;
import java.io.InputStream;

public class __main__ {

	public static void main(String[] args) {

		 String inputFile = null;
	        if (args.length > 0) inputFile = args[0];

	        InputStream is = System.in;
	        if (inputFile != null)
	            is = new FileInputStream(inputFile);
	    
	        ANTLRInputStream input = new ANTLRInputStream(is);
	        Java8Lexer lexer = new Java8Lexer(input);
	        CommonTokenStream tokens = new CommonTokenStream(lexer);
	        Java8Parser parser = new  Java8Parser(tokens);
	        parser.setBuildParseTree(true);
	        ParseTree tree = parser.s
	        		
	        ParseTreeWalker walker = new ParseTreeWalker();
	        Call_Listener collector = new  Call_Listener();
	        walker.walk(collector,tree );
	}

}
