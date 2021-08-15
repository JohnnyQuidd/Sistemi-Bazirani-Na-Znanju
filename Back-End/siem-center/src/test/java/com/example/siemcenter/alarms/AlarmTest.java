package com.example.siemcenter.alarms;

import com.example.siemcenter.alarms.services.AlarmService;
import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.LogsTest;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.rules.services.RuleService;
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

import static org.junit.Assert.assertNotNull;

public class AlarmTest {
    private KieSession ksession;
    private Logger logger = LoggerFactory.getLogger(LogsTest.class);
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private RuleService ruleService;

    @Before
    public void start() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();
        ksession = kc.newKieSession();
    }

    @Test
    public void multipleFalseLoginsFromASameDevice() {
        assertNotNull(ksession);
        assertNotNull(logger);

        ksession.setGlobal("logger", logger);
        ksession.setGlobal("deviceRepository", deviceRepository);
        ksession.setGlobal("alarmService", alarmService);

        Log log1 = Log.builder()
                .logType(LogType.ERROR)
                .device(new Device("192.168.0.1"))
                .os(new OperatingSystem("Windows"))
                .software(new Software("Adobe XD"))
                .user(User.builder().build())
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        Log log2 = Log.builder()
                .logType(LogType.ERROR)
                .device(new Device("192.168.0.1"))
                .os(new OperatingSystem("Windows"))
                .software(new Software("Adobe XD"))
                .user(User.builder().build())
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        Log log3 = Log.builder()
                .logType(LogType.ERROR)
                .device(new Device("192.168.0.1"))
                .os(new OperatingSystem("Windows"))
                .software(new Software("Adobe XD"))
                .user(User.builder().build())
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        ksession.insert(log1);
        ksession.insert(log2);
        ksession.insert(log3);

        //List<Alarm> alarmList = ruleService.getAlarmForRule("#1 Failed to login from a device that has already failed to log in");
        //assertEquals(1, alarmList.size());
    }
}
