package com.app.notificationservice.utils.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FreemarkerTemplateTemplateProcessorImpl implements FreemarkerTemplateProcessor {

    private final Configuration freemarkerConfiguration;
    private final StringWriter stringWriter = new StringWriter();

    @Override
    public String getContentFromTemplateWithModel(String pathToTemplate, Map<String, String> objectModel) throws IOException, TemplateException {
        freemarkerConfiguration.getTemplate(pathToTemplate)
                .process(objectModel, stringWriter);

        return stringWriter.getBuffer().toString();
    }

    @Override
    public String getContentFromTemplate(String templateName) throws IOException {
        return freemarkerConfiguration.getTemplate(templateName).toString();
    }
}
