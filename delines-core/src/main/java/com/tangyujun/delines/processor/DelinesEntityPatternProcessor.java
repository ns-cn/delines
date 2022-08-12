package com.tangyujun.delines.processor;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.auto.service.AutoService;
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
public class DelinesEntityPatternProcessor extends AbstractProcessor {

	public DelinesEntityPatternProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "@DelinesEntity Checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(DelinesEntity.class);
//		messager.printMessage(Diagnostic.Kind.NOTE, "total found: " + elements.size());
		boolean success = true;
		for (Element element : elements) {
			if (element.getKind().equals(ElementKind.CLASS)) {
				TypeElement typeElement = (TypeElement) element;
				messager.printMessage(Diagnostic.Kind.NOTE,
						"delines entity class: " + typeElement.getQualifiedName());
				DelinesEntity entity = typeElement.getAnnotation(DelinesEntity.class);
				if (entity != null) {
					if (CharSequenceUtil.isNotEmpty(entity.required())) {
						try {
							Pattern.compile(entity.required());
						} catch (Exception e) {
							messager.printMessage(Diagnostic.Kind.ERROR,
									"@DelinesEntity with wrong pattern: " + entity.required(),
									element);
							success = false;
						}
					}
					if (DelinesEntity.RangeType.REGULAR.equals(entity.rangeStartType())) {
						try {
							if (CharSequenceUtil.isEmpty(entity.rangeStart())) {
								messager.printMessage(Diagnostic.Kind.ERROR,
										"@DelinesEntity assigned rangeStartType without rangeStart", element);
							} else {
								Pattern.compile(entity.rangeStart());
							}
						} catch (Exception e) {
							messager.printMessage(Diagnostic.Kind.ERROR,
									"@DelinesEntity with wrong pattern: " + entity.rangeStart(),
									element);
							success = false;
						}
					}
					if (DelinesEntity.RangeType.REGULAR.equals(entity.rangeEndType())) {
						try {
							if (CharSequenceUtil.isEmpty(entity.rangeEnd())) {
								messager.printMessage(Diagnostic.Kind.ERROR,
										"@DelinesEntity assigned rangeEndType without rangeEnd", element);
							} else {
								Pattern.compile(entity.rangeEnd());
							}
						} catch (Exception e) {
							messager.printMessage(Diagnostic.Kind.ERROR,
									"@DelinesEntity with wrong pattern: " + entity.rangeEnd(), element);
							success = false;
						}
					}
				}
			}
		}
		return success;
	}
}
