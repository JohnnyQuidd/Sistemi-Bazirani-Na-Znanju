package com.example.siemcenter.queries;

import com.example.siemcenter.SiemCenterApplication;
import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.common.repositories.OperatingSystemRepository;
import com.example.siemcenter.common.repositories.SoftwareRepository;
import com.example.siemcenter.logs.LogsTest;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.rules.services.RuleService;
import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import com.example.siemcenter.users.services.UserService;
import com.example.siemcenter.util.DevicesForUser;
import org.drools.core.ClockType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCenterApplication.class)
public class QueriesTesting {
    private static KieSession ksession;
    private Logger logger = LoggerFactory.getLogger(LogsTest.class);
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DevicesForUser devicesForUser;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private OperatingSystemRepository osRepo;
    @Autowired
    private SoftwareRepository softwareRepository;
    @Autowired
    private LogRepository logRepository;
    private static Integer DEVICE_NUMBER = 1;
    private static boolean setup = false;


    @Before
    public void start() {
        if (!setup) {
            KieServices ks = KieServices.Factory.get();
            KieContainer kc = ks.newKieClasspathContainer();
            ksession = setUpSessionForStreamProcessingMode();
            setGlobals();
            createUsers();
            setDummyData();
            setup = true;
        }
    }

    @Test
    public void logsFetchingTest() {
        assertEquals(0, ruleService.getLogsBySession(ksession).size());

        Log log = createLog();
        ksession.insert(log);

        assertEquals(1, ruleService.getLogsBySession(ksession).size());
    }

    @Test
    public void alarmsFetchingTest() {
        assertEquals(0, ruleService.getAlarmsBySession(ksession).size());

        Alarm alarm = createAlarm();
        ksession.insert(alarm);

        assertEquals(1, ruleService.getAlarmsBySession(ksession).size());
    }

    @Test
    public void alarmsFetchingByRuleName() {
        assertEquals(0, ruleService.getAlarmForRuleAndSession("#1", ksession).size());

        Alarm alarm = createAlarmWithRule();
        ksession.insert(alarm);

        assertEquals(1, ruleService.getAlarmForRuleAndSession("#1", ksession).size());
    }

    @Test
    public void logsRegexFetching() {
        Log log = createRegexLog();
        ksession.insert(log);

        assertEquals(1, ruleService.fetchLogsByRegexAndSession("^[0-9]{10}", ksession).size());
    }

    @Test
    public void alarmsRegexFetching() {
        Alarm alarm = createRegexAlarm();
        ksession.insert(alarm);

        assertEquals(1, ruleService.fetchAlarmsByRegexAndSession("^[A-Z]{5}", ksession).size());
    }

    private Log createLog() {
        OperatingSystem os = osRepo.findByName("Windows").orElse(null);
        Software software = softwareRepository.findSoftwareByName("Adobe XD").orElse(null);
        User user = userRepository.findByUsername("JohnDoe").orElse(null);
        Device device = deviceRepository.findByIpAddress("192.168.0.1").orElse(null);

        return Log.builder()
                ._timestamp(new Date())
                .uuid(UUID.randomUUID())
                .logType(LogType.ERROR)
                .device(device)
                .os(os)
                .software(software)
                .user(user)
                .timestamp(LocalDateTime.now())
                .message("Failed to log in")
                .build();
    }

    private Log createRegexLog() {
        OperatingSystem os = osRepo.findByName("Ubuntu").orElse(null);
        Software software = softwareRepository.findSoftwareByName("Powerpoint").orElse(null);
        User user = userRepository.findByUsername("JaneDoe").orElse(null);
        Device device = deviceRepository.findByIpAddress("192.168.1.1").orElse(null);

        return Log.builder()
                ._timestamp(new Date())
                .uuid(UUID.randomUUID())
                .logType(LogType.ERROR)
                .device(device)
                .os(os)
                .software(software)
                .user(user)
                .timestamp(LocalDateTime.now())
                .message("0652210021")
                .build();
    }


    private Alarm createAlarm() {
        return Alarm.builder()
                .message("Default alarm")
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Alarm createRegexAlarm() {
        return Alarm.builder()
                .message("ALARM")
                .timestamp(LocalDateTime.now())
                .build();
    }

    private Alarm createAlarmWithRule() {
        return Alarm.builder()
                .message("Default alarm")
                .ruleTriggered("#1")
                .timestamp(LocalDateTime.now())
                .build();
    }

    private static KieSession setUpSessionForStreamProcessingMode() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.newKieClasspathContainer();

        KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        ksconf.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));

        KieSession session = kc.newKieSession("session1", ksconf);
        return session;
    }

    private void setGlobals() {
        ksession.setGlobal("logger", logger);
        ksession.setGlobal("deviceRepository", deviceRepository);
        ksession.setGlobal("alarmRepository", alarmRepository);
        ksession.setGlobal("userRepository", userRepository);
        ksession.setGlobal("devicesForUser", devicesForUser);
        ksession.setGlobal("deviceNumber", DEVICE_NUMBER);
    }

    private void createUsers() {
        logger.info("CREATED");
        UserDTO johnDoe = UserDTO.builder().username("JohnDoe").password("12345678").build();
        UserDTO janeDoe = UserDTO.builder().username("JaneDoe").password("12345678").build();
        UserDTO ericHansen = UserDTO.builder().username("EricHansen").password("12345678").build();

        userService.registerANewUser(johnDoe);
        userService.registerANewUser(janeDoe);
        userService.registerANewUser(ericHansen);
        setup = true;
    }

    private void setDummyData() {
        OperatingSystem ubuntu = new OperatingSystem("Ubuntu");
        osRepo.save(ubuntu);
        OperatingSystem windows = new OperatingSystem("Windows");
        osRepo.save(windows);

        Software powerpoint = new Software("Powerpoint");
        softwareRepository.save(powerpoint);
        Software adobe_xd = new Software("Adobe XD");
        softwareRepository.save(adobe_xd);

        Device device1 = new Device("192.168.1.1");
        deviceRepository.save(device1);

        Device device2 = new Device("192.168.0.1");
        deviceRepository.save(device2);
    }

}
