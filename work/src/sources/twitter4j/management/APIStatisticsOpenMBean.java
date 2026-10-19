package twitter4j.management;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

/* JADX INFO: loaded from: classes.dex */
public class APIStatisticsOpenMBean implements DynamicMBean {
    private final APIStatisticsMBean API_STATISTICS;
    private final TabularType API_STATISTICS_TYPE;
    private final CompositeType METHOD_STATS_TYPE;
    private static final String[] ITEM_NAMES = {"methodName", "callCount", "errorCount", "totalTime", "avgTime"};
    private static final OpenType[] ITEM_TYPES = {SimpleType.STRING, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG};
    private static final String[] ITEM_DESCRIPTIONS = {"The method name", "The number of times this method has been called", "The number of calls that failed", "The total amount of time spent invoking this method in milliseconds", "The average amount of time spent invoking this method in milliseconds"};

    public APIStatisticsOpenMBean(APIStatistics apiStatistics) {
        this.API_STATISTICS = apiStatistics;
        try {
            this.METHOD_STATS_TYPE = new CompositeType("method statistics", "method statistics", ITEM_NAMES, ITEM_DESCRIPTIONS, ITEM_TYPES);
            String[] index = {"methodName"};
            this.API_STATISTICS_TYPE = new TabularType("API statistics", "list of methods", this.METHOD_STATS_TYPE, index);
        } catch (OpenDataException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public MBeanInfo getMBeanInfo() {
        OpenMBeanAttributeInfoSupport[] attributes = new OpenMBeanAttributeInfoSupport[5];
        MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[0];
        int attrIdx = 0 + 1;
        attributes[0] = new OpenMBeanAttributeInfoSupport("callCount", "Total number of API calls", SimpleType.LONG, true, false, false);
        int attrIdx2 = attrIdx + 1;
        attributes[attrIdx] = new OpenMBeanAttributeInfoSupport("errorCount", "The number of failed API calls", SimpleType.LONG, true, false, false);
        int attrIdx3 = attrIdx2 + 1;
        attributes[attrIdx2] = new OpenMBeanAttributeInfoSupport("averageTime", "Average time spent invoking any API method", SimpleType.LONG, true, false, false);
        int attrIdx4 = attrIdx3 + 1;
        attributes[attrIdx3] = new OpenMBeanAttributeInfoSupport("totalTime", "Average time spent invoking any API method", SimpleType.LONG, true, false, false);
        int i = attrIdx4 + 1;
        attributes[attrIdx4] = new OpenMBeanAttributeInfoSupport("statisticsTable", "Table of statisics for all API methods", this.API_STATISTICS_TYPE, true, false, false);
        OpenMBeanConstructorInfoSupport[] constructors = {new OpenMBeanConstructorInfoSupport("APIStatisticsOpenMBean", "Constructs an APIStatisticsOpenMBean instance", new OpenMBeanParameterInfoSupport[0])};
        OpenMBeanOperationInfoSupport[] operations = {new OpenMBeanOperationInfoSupport("reset", "reset the statistics", new OpenMBeanParameterInfoSupport[0], SimpleType.VOID, 0)};
        OpenMBeanInfoSupport info = new OpenMBeanInfoSupport(getClass().getName(), "API Statistics Open MBean", attributes, constructors, operations, notifications);
        return info;
    }

    public synchronized TabularDataSupport getStatistics() {
        TabularDataSupport apiStatisticsTable;
        apiStatisticsTable = new TabularDataSupport(this.API_STATISTICS_TYPE);
        for (InvocationStatistics methodStats : this.API_STATISTICS.getInvocationStatistics()) {
            Object[] itemValues = {methodStats.getName(), new Long(methodStats.getCallCount()), new Long(methodStats.getErrorCount()), new Long(methodStats.getTotalTime()), new Long(methodStats.getAverageTime())};
            try {
                apiStatisticsTable.put(new CompositeDataSupport(this.METHOD_STATS_TYPE, ITEM_NAMES, itemValues));
            } catch (OpenDataException e) {
                throw new RuntimeException((Throwable) e);
            }
        }
        return apiStatisticsTable;
    }

    public void reset() {
        this.API_STATISTICS.reset();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public Object getAttribute(String attribute) throws MBeanException, AttributeNotFoundException, ReflectionException {
        if (attribute.equals("statisticsTable")) {
            return getStatistics();
        }
        if (attribute.equals("callCount")) {
            return new Long(this.API_STATISTICS.getCallCount());
        }
        if (attribute.equals("errorCount")) {
            return new Long(this.API_STATISTICS.getErrorCount());
        }
        if (attribute.equals("totalTime")) {
            return new Long(this.API_STATISTICS.getTotalTime());
        }
        if (attribute.equals("averageTime")) {
            return new Long(this.API_STATISTICS.getAverageTime());
        }
        throw new AttributeNotFoundException(new StringBuffer().append("Cannot find ").append(attribute).append(" attribute ").toString());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.MBeanException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.ReflectionException */
    public AttributeList getAttributes(String[] attributeNames) throws MBeanException, AttributeNotFoundException, ReflectionException {
        AttributeList resultList = new AttributeList();
        if (attributeNames.length != 0) {
            for (int i = 0; i < attributeNames.length; i++) {
                try {
                    Object value = getAttribute(attributeNames[i]);
                    resultList.add(new Attribute(attributeNames[i], value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.ReflectionException */
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        if (actionName.equals("reset")) {
            reset();
            return "Statistics reset";
        }
        throw new ReflectionException(new NoSuchMethodException(actionName), new StringBuffer().append("Cannot find the operation ").append(actionName).toString());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.management.AttributeNotFoundException */
    public void setAttribute(Attribute attribute) throws InvalidAttributeValueException, MBeanException, AttributeNotFoundException, ReflectionException {
        throw new AttributeNotFoundException("No attributes can be set in this MBean");
    }

    public AttributeList setAttributes(AttributeList attributes) {
        return new AttributeList();
    }
}
