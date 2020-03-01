package smartx.bam.bs.avmanager;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.api.Trigger;

public class AnalyzerTrigger implements Trigger{
	private String analyzerCode;

	@Override
	public void fire(Connection arg0, Object[] arg1, Object[] arg2)
			throws SQLException {
		Analyzer analyzer = AnalyzeViewManager.analyzerMap.get(analyzerCode);
		if(analyzer != null)
			analyzer.analyze();
	}

	@Override
	public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
		analyzerCode = tableName;
		
	}

	@Override
	public void close() throws SQLException {
		
	}

	@Override
	public void remove() throws SQLException {
		
	}

}
