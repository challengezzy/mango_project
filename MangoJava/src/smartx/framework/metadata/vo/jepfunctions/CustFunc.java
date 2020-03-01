/**************************************************************************
 * $RCSfile: CustFunc.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 **************************************************************************/

package smartx.framework.metadata.vo.jepfunctions;

import org.nfunk.jep.*;

import smartx.framework.metadata.ui.jepfunctions.*;

/**
 * An example class to test custom functions with JEP.
 */
class CustFunc {

    /**
     * Constructor.
     */
    public CustFunc() {

    }

    /**
     * Main method. Create a new JEP object and parse an example expression
     * that uses the SquareRoot function.
     */
    public static void main(String args[]) {
        String str_serverCallServletURL = "http://127.0.0.1:1111/nmgbizm/RemoteCallServlet"; //
        System.setProperty("RemoteCallServletURL", str_serverCallServletURL); //
        JEP parser = new JEP(); // Create a new parser
        String expr = "getCurrentDate()";
        //	Object value;

        System.out.println("Starting CustFunc...");
        parser.addStandardFunctions();
        parser.addStandardConstants();
        parser.addFunction("getCurrentDate", new GetCurrentDate()); // Add the custom function

        parser.parseExpression(expr); // Parse the expression
        if (parser.hasError()) {
            System.out.println("Error while parsing");
            System.out.println(parser.getErrorInfo());
            return;
        }
        String str_value = (String) parser.getValueAsObject();
        if (str_value == null) {
            System.out.println("\nreturn null!!!!!!");
        }
        //	value = parser.getValueAsObject();
        System.out.println(expr + " = " + str_value); // Print value
        if (parser.hasError()) {
            System.out.println("Error during evaluation");
            System.out.println(parser.getErrorInfo());
            return;
        }

    }
}
/**************************************************************************
 * $RCSfile: CustFunc.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:24 $
 *
 * $Log: CustFunc.java,v $
 * Revision 1.2  2007/05/31 07:38:24  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:03:31  qilin
 * no message
 *
 * Revision 1.2  2007/03/07 02:47:23  shxch
 * *** empty log message ***
 *
 * Revision 1.1  2007/03/07 02:04:18  shxch
 * *** empty log message ***
 *
 * Revision 1.2  2007/01/30 04:59:24  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/