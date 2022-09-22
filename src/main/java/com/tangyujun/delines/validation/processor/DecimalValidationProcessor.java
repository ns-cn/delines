package com.tangyujun.delines.validation.processor;

import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.AssertFalse;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * 数值类型校验器processor
 */
@SupportedAnnotationTypes({
		"com.tangyujun.delines.validation.annotation.DecimalMax",
		"com.tangyujun.delines.validation.annotation.DecimalMin",
		"com.tangyujun.delines.validation.annotation.DecimalRange",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DecimalValidationProcessor extends AbstractProcessor {

	/**
	 * 数值类型校验器processor
	 */
	public DecimalValidationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Decimal validation annotation Checking");
		Set<? extends Element> assertFalseElements = env.getElementsAnnotatedWith(AssertFalse.class);

		return false;
	}
}
