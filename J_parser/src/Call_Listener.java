
public class Call_Listener extends Java8BaseListener {
	static CallInfo ci = new CallInfo();
	String currentFunctionName = null;

	public void enterMethodDecl(Java8Parser.MethodInvocationContext ctx) {
        currentFunctionName = ctx.getText();
        System.out.println("asdfsadf"+currentFunctionName);
        ci.nodes.add(currentFunctionName);
    }

	public void exitCall(Java8Parser.MethodDeclaratorContext ctx) {
        String funcName = ctx.getText();
        // map current function to the callee
        System.out.println("asdflksajdlfkasjdf");
        ci.edge(currentFunctionName, funcName);
    }

}
