package com.example.siemcenter.users;

import com.example.siemcenter.SiemCenterApplication;
import com.example.siemcenter.alarms.models.Alarm;
import com.example.siemcenter.alarms.repositories.AlarmRepository;
import com.example.siemcenter.common.models.FactStatus;
import com.example.siemcenter.common.repositories.DeviceRepository;
import com.example.siemcenter.logs.LogsTest;
import com.example.siemcenter.logs.dtos.LogDTO;
import com.example.siemcenter.logs.models.LogType;
import com.example.siemcenter.logs.repositories.LogRepository;
import com.example.siemcenter.logs.services.LogService;
import com.example.siemcenter.rules.services.RuleService;
import com.example.siemcenter.users.dtos.UserDTO;
import com.example.siemcenter.users.models.RiskCategory;
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
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SiemCenterApplication.class)
public class ClassifyingUsersTest {
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
    @Autowired
    private RuleService ruleService;
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
    public void Test15_LowCategory_User_Not_Associated_With_Alarm_90d() {
        User user = userRepository.findByUsername("Misic98").orElse(null);
        LogDTO log1 = LogDTO.builder()
                .logType(LogType.INFORMATION)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows 10")
                .software("Adobe XD")
                .username("Misic98")
                .timestamp(LocalDateTime.now())
                .message("threat detected")
                .build();
        assertEquals(RiskCategory.LOW, user.getRiskCategory());
        logService.createLog(log1);

        Alarm alarm = Alarm.builder()
                .message("threat detected")
                .factStatus(FactStatus.ACTIVE)
                .os("Windows 10")
                .ipAddress("192.168.0.1")
                .relatedUsers(Arrays.asList(user))
                .timestamp(LocalDateTime.now())
                .build();

        ruleService.insertAlarm(alarm);

        User updatedUser = userRepository.findByUsername("Misic98").orElse(null);
        assertEquals(RiskCategory.MODERATE, updatedUser.getRiskCategory());

        SessionPseudoClock clock = ksession.getSessionClock();
        clock.advanceTime(91, TimeUnit.DAYS);
        ksession.fireAllRules();

        User afterSixMonths = userRepository.findByUsername("Misic98").orElse(null);
        assertEquals(RiskCategory.LOW, afterSixMonths.getRiskCategory());
    }

    @Test
    public void Test16_Moderate_Category_Antivirus_Past_Six_Months() {
        User user = userRepository.findByUsername("SkinnyPete").orElse(null);
        LogDTO log1 = LogDTO.builder()
                .logType(LogType.INFORMATION)
                .ipAddress("192.168.0.1")
                .operatingSystem("Windows 10")
                .software("Adobe XD")
                .username("SkinnyPete")
                .timestamp(LocalDateTime.now())
                .message("threat detected")
                .build();
        assertEquals(RiskCategory.LOW, user.getRiskCategory());
        logService.createLog(log1);

        Alarm alarm = Alarm.builder()
                .message("threat detected")
                .factStatus(FactStatus.ACTIVE)
                .os("Windows 10")
                .ipAddress("192.168.0.1")
                .relatedUsers(Arrays.asList(user))
                .timestamp(LocalDateTime.now())
                .build();

        ruleService.insertAlarm(alarm);

        User updatedUser = userRepository.findByUsername("SkinnyPete").orElse(null);
        assertEquals(RiskCategory.MODERATE, updatedUser.getRiskCategory());
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
}
