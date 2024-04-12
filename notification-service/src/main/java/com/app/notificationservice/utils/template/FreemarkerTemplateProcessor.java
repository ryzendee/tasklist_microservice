package com.app.notificationservice.utils.template;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

public interface FreemarkerTemplateProcessor {

    String getContentFromTemplateWithModel(String pathToTemplate, Map<String, String> objectModel) throws IOException, TemplateException;
    String getContentFromTemplate(String templateName) throws IOException;
}
