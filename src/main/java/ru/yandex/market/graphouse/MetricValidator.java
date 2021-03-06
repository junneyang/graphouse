package ru.yandex.market.graphouse;

import java.util.regex.Pattern;

/**
 * @author Dmitry Andreev <a href="mailto:AndreevDm@yandex-team.ru"/>
 * @date 27/05/15
 */
public class MetricValidator {

    private final Pattern metricPattern;

    private final int minMetricLength;

    private final int maxMetricLength;

    private final int minDots;

    private final int maxDots;

    public MetricValidator(String metricRegexp, int minMetricLength, int maxMetricLength, int minDots, int maxDots) {
        this.minMetricLength = minMetricLength;
        this.maxMetricLength = maxMetricLength;
        this.minDots = minDots;
        this.maxDots = maxDots;
        metricPattern = Pattern.compile(metricRegexp);
    }

    public boolean validate(String name, boolean allowDirs) {
        boolean isDir = MetricUtil.isDir(name);
        if ((!isDir && name.length() < minMetricLength) || name.length() > maxMetricLength) {
            return false;
        }
        if (!validateDots(name, allowDirs, isDir)) {
            return false;
        }
        return metricPattern.matcher(name).matches();
    }

    private boolean validateDots(String name, boolean allowDirs, boolean isDir) {
        if (name.charAt(0) == '.') {
            return false;
        }
        if (!allowDirs && isDir) {
            return false;
        }
        int prevDotIndex = -1;
        int dotIndex = -1;
        int dotCount = 0;
        while ((dotIndex = name.indexOf('.', prevDotIndex + 1)) > 0) {
            if (prevDotIndex + 1 == dotIndex) {
                return false; //Две точки подряд
            }
            prevDotIndex = dotIndex;
            dotCount++;
        }

        if ((!isDir && dotCount < minDots) || dotCount > maxDots) {
            return false;
        }
        return true;
    }
}

