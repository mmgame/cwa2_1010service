package com.cwa.service.init;

import java.util.List;

import serverice.config.ServiceConfigTypeEnum;
import baseice.service.FunctionMenu;

import com.cwa.component.functionmanage.FunctionService;
import com.cwa.component.functionmanage.IFunctionService;
import com.cwa.component.functionmanage.node.AFunctionNode;
import com.cwa.component.functionmanage.node.FunctionLeafNode;
import com.cwa.service.IService;
import com.cwa.service.context.FilterContext;
import com.cwa.service.context.IGloabalContext;
import com.cwa.service.filter.AbstractFilter;
import com.cwa.service.init.services.IceService;

/**
 * 注册本地服务到zk上
 * 
 * @author mausmars
 *
 */
public class InitZkRegisterServiceFilter extends AbstractFilter {
	public InitZkRegisterServiceFilter() {
		super("InitZkRegisterServiceFilter");
	}

	@Override
	public boolean doWork(Object context) throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("--- InitZkRegisterServiceFilter ---");
		}
		FilterContext c = (FilterContext) context;
		IGloabalContext gloabalContext = c.getGloabalContext();

		List<IService> iceServices = gloabalContext.getCurrentServices(ServiceConfigTypeEnum.Iec.value());
		for (IService is : iceServices) {
			IceService iceServic = (IceService) is;
			List<Integer> gids = iceServic.getGroupIds();
			if (gids.contains(-1)) {
				for (IFunctionService fm : gloabalContext.getAllFunctionService()) {
					registerIceService(iceServic, fm);
				}
			} else {
				for (int gid : gids) {
					FunctionService functionService = (FunctionService) gloabalContext.getFunctionService(gid);
					registerIceService(iceServic, functionService);
				}
			}
		}
		return true;
	}

	private void registerIceService(IceService iceServic, IFunctionService functionService) {
		FunctionMenu functionMenu = iceServic.getFunctionMenu();
		// 将服务注册到zk服务器
		AFunctionNode node = functionService.register(functionMenu);
		if (node instanceof FunctionLeafNode) {
			// 这里必须是叶节点类型（当前的功能菜单封装在zk的叶节点中）
		} else {
			// 这里就是有问题的
			throw new RuntimeException("Node isn't FunctionLeafNode! key=" + node.getKey());
		}
	}
}
