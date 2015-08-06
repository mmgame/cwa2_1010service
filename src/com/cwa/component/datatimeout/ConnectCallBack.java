package com.cwa.component.datatimeout;

import com.cwa.ISession;
import com.cwa.net.IConnectCallBack;

/**
 * 网络连接回调
 * 
 * @author mausmars
 *
 */
public class ConnectCallBack implements IConnectCallBack {
	private IDataTimeoutManager dataTimeoutTask;

	@Override
	public void connectExcute(ISession session) {
		// 将数据超时管理器放入session
		session.setAttachment(IDataTimeoutManager.DataTimeoutManagerKey, dataTimeoutTask);

		// sessionId为key
		dataTimeoutTask.insertTimeoutCheck(String.valueOf(session.getSessionId()), session, new IDataTimeoutCallBlack() {
			@Override
			public void callblack(Object obj) {
				ISession s = (ISession) obj;
				// 如果session超时就
				s.close(true);
			}
		});
	}

	// ------------------------------------------------
	public void setDataTimeoutTask(IDataTimeoutManager dataTimeoutTask) {
		this.dataTimeoutTask = dataTimeoutTask;
	}
}
