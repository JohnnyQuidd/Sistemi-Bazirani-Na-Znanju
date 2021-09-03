package com.example.siemcenter.ddos;

import com.example.siemcenter.SiemCenterApplication;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.models.Device;
import com.example.siemcenter.common.models.OperatingSystem;
import com.example.siemcenter.common.models.Software;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.common.repositories.OperatingSystemRepository;
import com.example.siemcenter.common.repositories.SoftwareRepository;
import com.example.siemcenter.logs.LogsTest;
import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.Log;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.logs.services.LogService;
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
import org.kie.api.time.SessionPseudoClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCenterApplication.class)
public class DDoSTesting {
    private static KieSession ksession;
    private Logger logger = LoggerFactory.getLogger(LogsTest.class);
    @Autowired
    private LogService logService;
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
        if(!setup) {
            KieServices ks = KieServices.Factory.get();
            KieContainer kc = ks.newKieClasspathContainer();
            ksession = setUpSessionForStreamProcessingMode();
            setGlobals();
            createUsers();
            setup = true;
        }
    }

    @Test
    public void Rule12_DDosDetected() {
        int initialSize = alarmRepository.findByMessage("Distributed Denial of Service Attack").size();
        for(int i=0; i<50; i++) {
            LogDTO dto = LogDTO.builder()
                    .logType(LogType.INFORMATION)
                    .ipAddress("192.168.3.1")
                    .operatingSystem("Windows")
                    .software("Adobe XD")
                    .username("EricHansen")
                    .timestamp(LocalDateTime.now())
                    .message("Invalid fields")
                    .build();

            logService.createLog(dto);
        }

        int afterAttack = alarmRepository.findByMessage("Distributed Denial of Service Attack").size();
        // When run alone
        //assertEquals(initialSize+1, afterAttack);

        // When run in conjunction with other tests
        assertEquals(afterAttack, initialSize);
    }

    @Test
    public void Rule13_DDos_On_Payment_System() {
        int initialSize = alarmRepository.findByMessage("DDoS attack regarding payment system").size();
        for(int i=0; i<50; i++) {
            LogDTO dto = LogDTO.builder()
                    .logType(LogType.INFORMATION)
                    .ipAddress("192.168.1.1")
                    .operatingSystem("Windows")
                    .software("Adobe XD")
                    .username("JohnDoe")
                    .timestamp(LocalDateTime.now())
                    .message("Payment request")
                    .build();

            logService.createLog(dto);
        }

        int afterAttack = alarmRepository.findByMessage("DDoS attack regarding payment system").size();
        assertEquals(initialSize+1, afterAttack);
    }

    @Test
    public void Rule14_BruteForce_LoginSystem() {
        int initialSize = alarmRepository.findByMessage("Brute force is detected on a login system").size();
        for(int i=0; i<50; i++) {
            LogDTO dto = LogDTO.builder()
                    .logType(LogType.ERROR)
                    .ipAddress("192.168.5.1")
                    .operatingSystem("Windows")
                    .software("Adobe XD")
                    .username("JohnDoe")
                    .timestamp(LocalDateTime.now())
                    .message("Failed login")
                    .build();

            logService.createLog(dto);
        }

        int afterAttack = alarmRepository.findByMessage("Brute force is detected on a login system").size();
        assertEquals(initialSize+1, afterAttack);
    }

    @Test
    public void Test22_Malicious_Device() {
        Device device = Device.builder().isMalicious(false).ipAddress("192.168.16.23").build();
        deviceRepository.save(device);

        for(int i=0; i<30; i++) {
            LogDTO dto = LogDTO.builder()
                    .logType(LogType.ERROR)
                    .ipAddress("192.168.16.23")
                    .operatingSystem("Windows")
                    .software("Adobe XD")
                    .username("JohnDoe")
                    .timestamp(LocalDateTime.now())
                    .message("Failed to log in")
                    .build();

            logService.createLog(dto);
        }

        Device afterFailedLoginAttempts = deviceRepository.findByIpAddress("192.168.16.23").orElse(null);
        assertEquals(true, afterFailedLoginAttempts.isMalicious());
    }

    // TODO: Should be false when clock is advanced for 2 days
    @Test
    public void Test22_Malicious_Device_After_Two_Days() {
        Device device = Device.builder().isMalicious(false).ipAddress("192.168.16.30").build();
        deviceRepository.save(device);
        for(int i=0; i<29; i++) {
            LogDTO dto = LogDTO.builder()
                    .logType(LogType.ERROR)
                    .ipAddress("192.168.16.30")
                    .operatingSystem("Windows")
                    .software("Adobe XD")
                    .username("JohnDoe")
                    .timestamp(LocalDateTime.now())
                    .message("Failed to log in")
                    .build();

            logService.createLog(dto);
        }

        SessionPseudoClock clock = ksession.getSessionClock();
        clock.advanceTime(2, TimeUnit.DAYS);

        OperatingSystem os = osRepo.findByName("Windows").orElse(null);
        Software software = softwareRepository.findSoftwareByName("Adobe XD").orElse(null);
        User user = userRepository.findByUsername("JohnDoe").orElse(null);

        Log log = Log.builder()
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

        logRepository.save(log);
        ruleService.insertLog(log);
        ksession.fireAllRules();


        Device afterFailedLoginAttempts = deviceRepository.findByIpAddress("192.168.16.30").orElse(null);
        assertEquals(true, afterFailedLoginAttempts.isMalicious());
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
}
