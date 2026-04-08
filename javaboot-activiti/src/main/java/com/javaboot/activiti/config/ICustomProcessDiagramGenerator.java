package com.javaboot.activiti.config;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.image.ProcessDiagramGenerator;

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * @author javaboot
 */
public interface ICustomProcessDiagramGenerator extends ProcessDiagramGenerator {
    InputStream generateDiagram(BpmnModel bpmnModel, String imageType, List<String> highLightedActivities,
                                List<String> highLightedFlows, String activityFontName, String labelFontName, String annotationFontName,
                                ClassLoader customClassLoader, double scaleFactor, Color[] colors, Set<String> currIds);
}
