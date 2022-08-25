package com.tangyujun.delines.validation.processor;

import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.AssertFalse;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({
		"com.tangyujun.delines.validation.annotation.StringLength",
		"com.tangyujun.delines.validation.annotation.StringNotEmpty",
		"com.tangyujun.delines.validation.annotation.StringPattern",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class StringValidationProcessor extends AbstractProcessor {

	public StringValidationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Validation annotation Checking");
		Set<? extends Element> assertFalseElements = env.getElementsAnnotatedWith(AssertFalse.class);

		return false;
	}



}
