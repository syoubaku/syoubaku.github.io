package info.xiaomo.gengine.network.handler;

import java.util.Arrays;
import info.xiaomo.gengine.bean.Config;
import info.xiaomo.gengine.mail.MailConfig;
import info.xiaomo.gengine.mail.MailManager;
import info.xiaomo.gengine.script.IConfigScript;
import info.xiaomo.gengine.script.ScriptManager;
import info.xiaomo.gengine.utils.MsgUtil;
import info.xiaomo.gengine.utils.SymbolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载配置
 *
 * <p>http://192.168.0.17:9002/server/reloadConfig?table=c_fish,c_room&auth=daa0cf5b-e72d-422c-b278-450b28a702c6
 * <p>
 * 2017年10月12日 下午2:43:20
 */
@HandlerEntity(path = "/server/reloadConfig")
public class ReloadConfigHandler extends HttpHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadConfigHandler.class);

	@Override
	public void run() {
		String auth = getString("auth");
		if (!Config.SERVER_AUTH.equals(auth)) {
			sendMsg("验证失败");
			return;
		}
		String tableStr = getString("table");
		String result = "";
		if (tableStr != null) {
			result =
					ScriptManager.getInstance()
							.getBaseScriptEntry()
							.functionScripts(
									IConfigScript.class,
									(IConfigScript script) ->
											script.reloadConfig(Arrays.asList(tableStr.split(SymbolUtil.DOUHAO_REG))));
		} else {
			result =
					ScriptManager.getInstance()
							.getBaseScriptEntry()
							.functionScripts(
									IConfigScript.class, (IConfigScript script) -> script.reloadConfig(null));
		}

		String info = String.format("%s加载配置：%s", MsgUtil.getIp(getSession()), result);
		LOGGER.info(info);
		MailConfig mailConfig = MailManager.getInstance().getMailConfig();
		String[] receives =
				mailConfig.getReceivedUser().toArray(new String[0]);
		MailManager.getInstance().sendTextMail("加载配置", Config.SERVER_NAME + "\r\n" + info, false, receives);
		sendMsg(info);
	}
}
