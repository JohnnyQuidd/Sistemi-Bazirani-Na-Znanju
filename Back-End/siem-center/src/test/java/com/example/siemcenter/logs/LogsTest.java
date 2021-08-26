package com.example.siemcenter.logs;

import com.example.siemcenter.SiemCenterApplication;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.logs.services.LogService;
import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.models.RiskCategory;
import com.example.siemcenter.users.models.Role;
import com.example.siemcenter.users.models.User;
import com.example.siemcenter.users.repositories.UserRepository;
import com.example.siemcenter.users.services.UserService;
import com.example.siemcenter.util.DevicesForUser;
import org.drools.core.ClockType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCenterApplication.class)
public class LogsTest {
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
    public void insertingLogTest() {
        LogDTO logDTO = LogDTO.builder()
                .logType(LogType.WARNING)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        long beforeInsertion = logRepository.count();
        logService.createLog(logDTO);

        ksession.fireAllRules();
        long afterInsertion = logRepository.count();
        assertEquals(beforeInsertion+1, afterInsertion);
    }

    @Test
    public void Test1_FailedToLogInFromADeviceThatHasAlreadyFailedToLogIn() {
        int beforeInsertion = alarmRepository.findByMessageContains("Failed to login from a device").size();
        LogDTO log1 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        LogDTO log2 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        logService.createLog(log1);
        logService.createLog(log2);

        ksession.fireAllRules();
        int afterInsertion = alarmRepository.findByMessageContains("Failed to login from a device").size();
        assertEquals(beforeInsertion+2, afterInsertion);
    }

    @Test
    public void Test2_FailedToLogInWithSameUsernameMultipleTimes() {
        int beforeInsertion = alarmRepository.findByMessageContains("Multiple failed attempts to log with same username").size();
        LogDTO log1 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        LogDTO log2 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        logService.createLog(log1);
        logService.createLog(log2);
        ksession.fireAllRules();

        int afterInsertion = alarmRepository.findByMessageContains("Multiple failed attempts to log with same username").size();
        assertEquals(beforeInsertion+2, afterInsertion);
    }

    @Test
    public void Test3_LogWithTypeErrorOccurred() {
        int beforeInsertion = alarmRepository.findByMessageContains("Log with type ERROR occurred").size();
        LogDTO log1 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        logService.createLog(log1);
        ksession.fireAllRules();

        int afterInsertion = alarmRepository.findByMessageContains("Log with type ERROR occurred").size();
        assertEquals(beforeInsertion+1, afterInsertion);
    }

    @Test
    public void Test4_AttemptToLoginFromAccountThatWasNotActiveForMoreThan90days() {
        int beforeInsertion = alarmRepository.findByMessageContains("User tried to log in an account that was inactive for 90 days or more").size();
        User inactiveUser = createInactiveUser();
        userRepository.save(inactiveUser);

        LogDTO log1 = LogDTO.builder()
                .logType(LogType.ERROR)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows")
                .software("Adobe XD")
                .username("InactiveUser")
                .timestamp(LocalDateTime.now())
                .message("Failed login attempt")
                .build();

        logService.createLog(log1);
        ksession.fireAllRules();

        int afterInsertion = alarmRepository.findByMessageContains("User tried to log in an account that was inactive for 90 days or more").size();
        assertEquals(beforeInsertion+1, afterInsertion);
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
        UserDTO skinnyPete = UserDTO.builder().username("SkinnyPete").password("12345678").build();
        UserDTO misic98 = UserDTO.builder().username("Misic98").password("12345678").build();
        UserDTO zoran55 = UserDTO.builder().username("Zoran55").password("12345678").build();

        userService.registerANewUser(skinnyPete);
        userService.registerANewUser(misic98);
        userService.registerANewUser(zoran55);
        setup = true;
    }

    private User createInactiveUser() {
        return User.builder()
                .lastTimeUserWasActive(LocalDateTime.now().minusDays(91))
                .username("InactiveUser")
                .password("12345678910")
                .riskCategory(RiskCategory.LOW)
                .role(Role.USER)
                .build();
    }
}
