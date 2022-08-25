package com.tangyujun.delines.validation.processor;

import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.AssertFalse;
import com.tangyujun.delines.validation.annotation.AssertTrue;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
		messager.printMessage(Diagnostic.Kind.NOTE, "Validation annotation Checking");
		Set<? extends Element> assertFalseElements = env.getElementsAnnotatedWith(AssertFalse.class);
		Set<? extends Element> assertTrueElements = env.getElementsAnnotatedWith(AssertTrue.class);
		Consumer<Element> elementConsumer = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				Boolean isBoolean = Optional.ofNullable(element.asType())
						.map(Objects::toString)
						.map(Boolean.class.getName()::equals)
						.orElse(false);
				if (!isBoolean) {
					messager.printMessage(Diagnostic.Kind.ERROR,
							"@AssertFalse or @AssertTrue only for Boolean", element);
				}
			}
		};
		assertFalseElements.forEach(elementConsumer);
		assertTrueElements.forEach(elementConsumer);
		return false;
	}


}
