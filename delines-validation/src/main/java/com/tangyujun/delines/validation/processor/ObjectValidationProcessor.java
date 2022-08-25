package com.tangyujun.delines.validation.processor;

import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.AssertFalse;
import com.tangyujun.delines.validation.annotation.AssertNotNull;
import com.tangyujun.delines.validation.annotation.AssertNull;
import com.tangyujun.delines.validation.annotation.AssertTrue;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@SupportedAnnotationTypes({
		"com.tangyujun.delines.validation.annotation.AssertNotNull",
		"com.tangyujun.delines.validation.annotation.AssertNull",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class ObjectValidationProcessor extends AbstractProcessor {

	public ObjectValidationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Validation annotation Checking");
		Set<? extends Element> notNullElements = env.getElementsAnnotatedWith(AssertNotNull.class);
		Set<? extends Element> nullElements = env.getElementsAnnotatedWith(AssertNull.class);
		Consumer<Element> elementConsumer = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
			}
		};
		notNullElements.forEach(elementConsumer);
		nullElements.forEach(elementConsumer);
		return false;
	}
}
