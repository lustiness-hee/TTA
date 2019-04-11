import java.util.Set;
import org.antlr.v4.runtime.misc.MultiMap;
import org.antlr.v4.runtime.misc.OrderedHashSet;

public class CallInfo {
	Set<String> nodes = new OrderedHashSet<String>(); // list of functions
	MultiMap<String, String> edges = new MultiMap<String, String>(); // caller->callee

	public void edge(String source, String target) {
		edges.map(source, target);
	}

	public String toString() {
		return "edges: " + edges.toString() + ", functions: " + nodes;
	}
}
