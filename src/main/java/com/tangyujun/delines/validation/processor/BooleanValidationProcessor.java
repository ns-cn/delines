package com.tangyujun.delines.validation.processor;

import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.AssertTrue;
import com.tangyujun.delines.validation.annotation.AssertFalse;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@SupportedAnnotationTypes({
		"com.tangyujun.delines.validation.annotation.AssertFalse",
		"com.tangyujun.delines.validation.annotation.AssertTrue",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class BooleanValidationProcessor extends AbstractProcessor {

	public BooleanValidationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Boolean validation annotation Checking");
		Set<? extends Element> assertFalseElements = env.getElementsAnnotatedWith(AssertFalse.class);
		Set<? extends Element> assertTrueElements = env.getElementsAnnotatedWith(AssertTrue.class);
		AtomicBoolean success = new AtomicBoolean(true);
		Consumer<Element> elementConsumer = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				Boolean isBoolean = Optional.ofNullable(element.asType())
						.map(Objects::toString)
						.map(Boolean.class.getName()::equals)
						.orElse(false);
				if (!isBoolean) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR,
							"@AssertFalse or @AssertTrue only for Boolean", element);
				}
			}
		};
		assertFalseElements.forEach(elementConsumer);
		assertTrueElements.forEach(elementConsumer);
		return success.get();
	}


}
