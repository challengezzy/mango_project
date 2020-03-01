/**************************************************************************
 * $RCSfile: CustomFileFilter.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 **************************************************************************/
package smartx.framework.metadata.ui.componentscard;

import java.io.*;

import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter {

    private String[] str_extensions = null;

    private String str_discription = null;

    public CustomFileFilter(String _extension) {
        this(new String[] {_extension}, null);
    }

    public CustomFileFilter(String[] _extensions, String _description) {
        str_extensions = new String[_extensions.length];
        for (int i = 0; i < _extensions.length; i++) {
            str_extensions[i] = _extensions[i].toLowerCase();
        }
        str_discription = (_description == null ? _extensions[0] + "files"
                           : _description);
    }

    public boolean accept(File f) {
        // TODO Auto-generated method stub
        if (f.isDirectory()) {
            return true;
        }
        String str_filename = f.getName().toLowerCase();
        for (int i = str_extensions.length - 1; i >= 0; i--) {
            if (str_filename.endsWith(str_extensions[i])) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        // TODO Auto-generated method stub
        return str_discription;
    }

}
/**************************************************************************
 * $RCSfile: CustomFileFilter.java,v $  $Revision: 1.2 $  $Date: 2007/05/31 07:38:17 $
 *
 * $Log: CustomFileFilter.java,v $
 * Revision 1.2  2007/05/31 07:38:17  qilin
 * code format
 *
 * Revision 1.1  2007/05/17 06:01:07  qilin
 * no message
 *
 * Revision 1.2  2007/01/30 05:14:30  lujian
 * *** empty log message ***
 *
 *
 **************************************************************************/