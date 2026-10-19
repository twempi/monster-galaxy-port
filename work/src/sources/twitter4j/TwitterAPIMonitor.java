package twitter4j;

import java.lang.management.ManagementFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.logging.Logger;
import twitter4j.management.APIStatistics;
import twitter4j.management.APIStatisticsMBean;
import twitter4j.management.APIStatisticsOpenMBean;

/* JADX INFO: loaded from: classes.dex */
public class TwitterAPIMonitor {
    private static final TwitterAPIMonitor SINGLETON;
    static Class class$twitter4j$TwitterAPIMonitor;
    private static final Logger logger;
    private static final Pattern pattern;
    private final APIStatistics STATISTICS = new APIStatistics(100);

    static {
        Class clsClass$;
        if (class$twitter4j$TwitterAPIMonitor == null) {
            clsClass$ = class$("twitter4j.TwitterAPIMonitor");
            class$twitter4j$TwitterAPIMonitor = clsClass$;
        } else {
            clsClass$ = class$twitter4j$TwitterAPIMonitor;
        }
        logger = Logger.getLogger(clsClass$);
        pattern = Pattern.compile("https?:\\/\\/[^\\/]+\\/([a-zA-Z_\\.]*).*");
        SINGLETON = new TwitterAPIMonitor();
        boolean isJDK14orEarlier = false;
        try {
            String versionStr = System.getProperty("java.specification.version");
            if (versionStr != null) {
                isJDK14orEarlier = 1.5d > Double.parseDouble(versionStr);
            }
            if (ConfigurationContext.getInstance().isDalvik()) {
                System.setProperty("http.keepAlive", "false");
            }
        } catch (SecurityException e) {
            isJDK14orEarlier = true;
        }
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            APIStatistics statsMBean = new APIStatistics(100);
            if (isJDK14orEarlier) {
                ObjectName oName = new ObjectName("twitter4j.mbean:type=APIStatistics");
                mbs.registerMBean(statsMBean, oName);
            } else {
                ObjectName oName2 = new ObjectName("twitter4j.mbean:type=APIStatisticsOpenMBean");
                APIStatisticsOpenMBean openMBean = new APIStatisticsOpenMBean(statsMBean);
                mbs.registerMBean(openMBean, oName2);
            }
        } catch (MalformedObjectNameException e2) {
            e2.printStackTrace();
            logger.error(e2.getMessage());
        } catch (InstanceAlreadyExistsException e3) {
            e3.printStackTrace();
            logger.error(e3.getMessage());
        } catch (NotCompliantMBeanException e4) {
            e4.printStackTrace();
            logger.error(e4.getMessage());
        } catch (MBeanRegistrationException e5) {
            e5.printStackTrace();
            logger.error(e5.getMessage());
        }
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private TwitterAPIMonitor() {
    }

    public static TwitterAPIMonitor getInstance() {
        return SINGLETON;
    }

    public APIStatisticsMBean getStatistics() {
        return this.STATISTICS;
    }

    void methodCalled(String twitterUrl, long elapsedTime, boolean success) {
        Matcher matcher = pattern.matcher(twitterUrl);
        if (matcher.matches() && matcher.groupCount() > 0) {
            String method = matcher.group();
            this.STATISTICS.methodCalled(method, elapsedTime, success);
        }
    }
}
