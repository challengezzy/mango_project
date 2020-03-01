/***********************************************************************
 * $RCSfile: JNovaFileFilter.java,v $  $Revision: 1.1 $  $Date: 2007/06/11 03:31:19 $
 * $Log: JNovaFileFilter.java,v $
 * Revision 1.1  2007/06/11 03:31:19  qilin
 * no message
 *
*************************************************************************/

package smartx.framework.common.ui.component;

import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.filechooser.*;

/**
 * 此类是对FileFilter类的扩展，可以方便的过滤出与文件过滤器所
 * 限定的文件扩展名相同的文件。
 *<p></p>
 * 示例：- 创建一个过滤器，使其能过滤掉除了jpg和gif类型外的所有文件<BR>
 *
 *     &nbsp; &nbsp; &nbsp;JFileChooser chooser = new JFileChooser();<BR>
 *     &nbsp; &nbsp; &nbsp;JGxluFileFilter filter = new JGxluFileFilter(<BR>
 *     &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;new String{"gif", "jpg"}, "JPEG & GIF Images")<BR>
 *     &nbsp; &nbsp; &nbsp;chooser.addChoosableFileFilter(filter);<BR>
 *     &nbsp; &nbsp; &nbsp;chooser.showOpenDialog(this);<BR>
 *
 * @version 1.9 04/23/99
 * @author Enigma
 */

public class JNovaFileFilter extends FileFilter
{
	private static String TYPE_UNKNOWN = "Type Unknown";
	private static String HIDDEN_FILE = "Hidden File";

	private Hashtable filters = null;
	private String description = null;
	private String fullDescription = null;
	private boolean useExtensionsInDescription = true;

    /**
     * 构造一个文件过滤器，若不添加过滤后缀，则显示所有文件。
     *
     * @see #addExtension
     */
	public JNovaFileFilter()
	{
		this.filters = new Hashtable();
	}

    /**
     * 构造一个文件过滤器，可过滤出一确定后缀的所有文件。
     *
     * 示例： new JGxluFileFilter("jpg");
	 *
     * 注： 在后缀前的"."并不需要，若提供了它，它将被忽略。
     *
     * @param extension - 文件后缀名称
     *
     * @see #addExtension
     */
	public JNovaFileFilter(String extension)
	{
		this(extension, null);
	}

    /**
     * 构造一个文件过滤器，可过滤出一确定的文件类型。
     * 示例：new JGxluFileFilter("jpg", "JPEG Image Images");
     *
     * @param extension - 文件后缀名称
     * @param description - 对此类文件类型的简短描述
     *
     * @see #addExtension
     */
    public JNovaFileFilter(String extension, String description)
	{
		this();

		if(extension != null)
		{
			addExtension(extension);
		}

		if(description != null)
		{
			setDescription(description);
		}
	}

    /**
     * 根据给定的文件后缀名称数组构造一文件过滤器。
     * 示例： new JGxluFileFilter(String {"gif", "jpg"});
     *
     * @param filters - 文件后缀名称数组
     *
     * @see #addExtension
     */
	public JNovaFileFilter(String[] filters)
	{
		this(filters, null);
	}

    /**
     * 根据给定的文件后缀名称数组构造一文件过滤器，并给出文件类型的描述。
     * 示例： new ExampleFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
     *
     * @param filters - 文件后缀名称数组
     * @param description - 文件类型描述
     *
     * @see #addExtension
     */
	public JNovaFileFilter(String[] filters, String description)
	{
		this();

		for(int i = 0; i < filters.length; i++)
		{
			addExtension(filters[i]);
		}

		if(description != null)
		{
			setDescription(description);
		}
	}

    /**
     * 验证文件过滤器是否接受输入的文件。
     *
     * @param f - 需验证的文件
     * @return 若文件被接受，返回true，否则返回false。
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
	public boolean accept(File f)
	{
		if(f != null)
		{
			if(f.isDirectory())
			{
				return true;
			}

			String extension = getExtension(f);
			if(extension != null && filters.get(getExtension(f)) != null)
			{
				return true;
			}
		}
		return false;
	}

    /**
     * 取得输入文件的扩展名。
     *
     * @param f - 待处理的文件。
     * @return 文件的扩展名。
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
	public String getExtension(File f)
	{
		if(f != null)
		{
			String filename = f.getName();
			int i = filename.lastIndexOf('.');

			if(i > 0 && i < filename.length() - 1)
			{
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

    /**
     * 在文件过滤器中增加一可过滤的文件类型。
     *
     * @param extension - 增加给文件过滤器的文件类型。
     */
    public void addExtension(String extension)
	{
		if(filters == null)
		{
			filters = new Hashtable(5);
		}
		filters.put(extension.toLowerCase(), this);
		fullDescription = null;
	}

    /**
     * 取得该文件过滤器的描述。
     *
     * @see #setDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     * @see FileFilter#getDescription
     */
	public String getDescription()
	{
		if(fullDescription == null)
		{
			if(description == null || isExtensionListInDescription())
			{
				fullDescription = description == null ? "(" : description + " (";
				//Build the description from the extension list
				Enumeration extensions = filters.keys();
				if(extensions != null)
				{
					fullDescription += "." + (String) extensions.nextElement();
					while(extensions.hasMoreElements())
					{
						fullDescription += ", " + (String) extensions.nextElement();
					}
				}
				fullDescription += ")";
			}
			else
			{
				fullDescription = description;
			}
		}
		return fullDescription;
	}

    /**
     * 给文件过滤器设置易理解的描述。
     *
     * @param description - 文件过滤器描述。
     *
     * @see #getDescription
     * @see #setExtensionListInDescription
     * @see #isExtensionListInDescription
     */
	public void setDescription(String description)
	{
		this.description = description;
		fullDescription = null;
	}

    /**
     * 设置是否在文件过滤器扩展名列表中显示对其的描述。
     *
     * @param b - 决定是否显示文件类型描述。
     * @see #getDescription
     * @see #setDescription
     * @see #isExtensionListInDescription
     */
     public void setExtensionListInDescription(boolean b)
	{
		useExtensionsInDescription = b;
		fullDescription = null;
	}

    /**
     * 确定是否在文件过滤器中显示文件类型描述。
     *
     * @return 若显示，返回true,否则返回false。
     * @see #getDescription
     * @see #setDescription
     * @see #setExtensionListInDescription
     */
	public boolean isExtensionListInDescription()
	{
		return useExtensionsInDescription;
	}
}
