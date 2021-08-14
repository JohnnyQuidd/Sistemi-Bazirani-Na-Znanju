package com.example.siemcenter.logs;

import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.services.LogService;
import com.example.siemcenter.users.models.RiskCategory;
import com.example.siemcenter.users.models.Role;
import com.example.siemcenter.users.models.User;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LogsTest {
    private KieSession ksession;
    private Logger logger = LoggerFactory.getLogger(LogsTest.class);

    @Autowired
    private LogService logService;

    @Before
    public void start() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();
        ksession = kc.newKieSession();
    }

    @Test
    public void insertingLogTest() {
        assertNotNull(ksession);
        assertNotNull(logger);
        ksession.setGlobal("logger", logger);

        User user = User.builder()
                .username("SkinnyPete")
                .riskCategory(RiskCategory.LOW)
                .role(Role.USER)
                .id(1l)
                .build();

        Log log = Log.builder()
                .logType(LogType.ERROR)
                .device(new Device("192.168.0.1"))
                .os(new OperatingSystem("Windows"))
                .software(new Software("Adobe XD"))
                .user(user)
                .timestamp(LocalDateTime.now())
                .message("Failed logging attempt")
                .build();

        ksession.insert(log);

        int firedRules = ksession.fireAllRules();
        assertEquals(1, firedRules);
    }
}
