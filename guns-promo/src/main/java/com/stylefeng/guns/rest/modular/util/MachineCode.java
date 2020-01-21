package com.stylefeng.guns.rest.modular.util;

import com.alibaba.dubbo.common.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务生成机器码配置
 */
public class MachineCode {
	private transient static final Logger logger = LoggerFactory.getLogger(MachineCode.class);

	@Value("${machine.useConfig}")
	private String useConfig;

	@Value("${machine.codeConfig}")
	private String machineCodeConfig;

	private Integer machineNo;

	private Map<String, Integer> machineCodeMap;


	public void setUseConfig(String useConfig) {
		this.useConfig = useConfig;
	}

	public void setMachineCodeConfig(String machineCodeConfig) {
		this.machineCodeConfig = machineCodeConfig;
	}
	public Integer getMachineNo() {
		return machineNo;
	}

	public void initMachineCodeMap() {
		logger.info("初始化MachineCode，参数useConfig={}，machineCodeConfig={}", useConfig, machineCodeConfig);
		try {
			if ("TRUE".equalsIgnoreCase(useConfig)) {
				if (machineCodeConfig == null || machineCodeConfig.trim().length() == 0) {
					throw new RuntimeException("读取机器码配置内容为空");
				}

				String[] machineCodeConfigs = machineCodeConfig.split("\\|");
				String[] machineCodes = null;
				machineCodeMap = new HashMap<String, Integer>();
				for (String tempMachineCodeConfig : machineCodeConfigs) {
					if (tempMachineCodeConfig != null && tempMachineCodeConfig.trim().length() > 0) {
						machineCodes = tempMachineCodeConfig.split("\\^");
						machineCodeMap.put(machineCodes[0], Integer.parseInt(machineCodes[1]));
					}
				}

				if (machineCodeMap == null || machineCodeMap.size() == 0) {
					throw new RuntimeException("解析机器码配置后得到内容为空");
				}
				String ip = NetUtils.getLocalAddress().getHostAddress();
				machineNo = machineCodeMap.get(ip);
				if (machineNo == null) {
					throw new RuntimeException("解析机器码配置内容无法获取本机ip("+ip+")对应的机器码");
				}
				logger.info("解析MachineCode，本机IP=【{}】，得到编码=【{}】", ip, machineNo);
			}
		} catch (Exception e) {
			throw new BeanInitializationException("MachineCode初始化失败...", e);
		}
	}
}
