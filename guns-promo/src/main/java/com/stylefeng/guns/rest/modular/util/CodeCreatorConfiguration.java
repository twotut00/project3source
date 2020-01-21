package com.stylefeng.guns.rest.modular.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author: jia.xue
 * @create: 2019-08-08 17:02
 * @Description
 **/
@Configuration
@ComponentScan(value = "com.stylefeng.guns.rest.modular.util")
    public class CodeCreatorConfiguration {


    @Bean(initMethod = "initMachineCodeMap")
    public MachineCode machineCode(){
        return new MachineCode();
    }
}