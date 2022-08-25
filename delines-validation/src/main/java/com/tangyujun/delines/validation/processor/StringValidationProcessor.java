package com.tangyujun.delines.validation.processor;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.auto.service.AutoService;
import com.tangyujun.delines.processor.PatternChecker;
import com.tangyujun.delines.validation.annotation.*;

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
		messager.printMessage(Diagnostic.Kind.NOTE, "String validation annotation Checking");
		Set<? extends Element> stringLengthElements = env.getElementsAnnotatedWith(StringLength.class);
		Set<? extends Element> stringNotEmptyElements = env.getElementsAnnotatedWith(StringNotEmpty.class);
		Set<? extends Element> stringPatternElements = env.getElementsAnnotatedWith(StringPattern.class);
		AtomicBoolean success = new AtomicBoolean(true);
		Consumer<Element> typeCheck = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				Boolean isString = Optional.ofNullable(element.asType())
						.map(Objects::toString)
						.map(String.class.getName()::equals)
						.orElse(false);
				if (!isString) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR,
							"@StringLength/@StringNotEmpty/@StringPattern only for String", element);
				}
			}
		};
		Consumer<Element> lengthCheck = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				StringLength length = element.getAnnotation(StringLength.class);
				if (length.min() < 0 || length.max() < 0 || length.max() < length.min()) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR, "@StringLength with illegal range", element);
				}
			}
		};
		Consumer<Element> patternCheck = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				StringPattern pattern = element.getAnnotation(StringPattern.class);
				if (CharSequenceUtil.isEmpty(pattern.value())) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR, "@StringPattern without pattern", element);
				} else {
					success.set(PatternChecker.check(pattern.value(), messager, element) && success.get());
				}
			}
		};
		stringLengthElements.forEach(typeCheck);
		stringLengthElements.forEach(lengthCheck);
		stringNotEmptyElements.forEach(typeCheck);
		stringPatternElements.forEach(typeCheck);
		stringPatternElements.forEach(patternCheck);
		return true;
	}
}
