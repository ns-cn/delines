package com.tangyujun.delines.processor;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.tangyujun.delines.annotation.DelinesEntity;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.regex.Pattern;

@SupportedAnnotationTypes({"com.tangyujun.delines.annotation.DelinesEntity"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class PatternProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "delines entity pattern checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(DelinesEntity.class);
		boolean success = true;
		try {
			for (Element element : elements) {
				if (element.getKind().equals(ElementKind.CLASS)) {
					TypeElement typeElement = (TypeElement) element;
					DelinesEntity entity = typeElement.getAnnotation(DelinesEntity.class);
					if (entity.required() != null && !"".equals(entity.required())) {
						success = success && checkPattern(messager, typeElement, entity.required());
						if (DelinesEntity.RangeType.REGULAR.equals(entity.rangeStartType())) {
							success = success && checkPattern(messager, typeElement, entity.rangeStart());
						}
						if (DelinesEntity.RangeType.REGULAR.equals(entity.rangeEndType())) {
							success = success && checkPattern(messager, typeElement, entity.rangeEnd());
						}
					}
				}
			}
		} catch (Exception e) {
			messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
		}
		return false;
	}

	private boolean checkPattern(Messager messager, TypeElement element, String pattern) {
		try {
			if (!Strings.isNullOrEmpty(pattern)) {
				Pattern.compile(pattern);
			}
			return true;
		} catch (Exception e) {
			messager.printMessage(Diagnostic.Kind.ERROR, String.format("wrong pattern in %s: %s",
					element.getQualifiedName(), e.getMessage()));
			return false;
		}
	}
}
