package com.tangyujun.delines.processor;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.auto.service.AutoService;
import com.tangyujun.delines.annotation.DelinesField;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.regex.Pattern;

@SupportedAnnotationTypes({"com.tangyujun.delines.annotation.DelinesField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DelinesFieldPatternProcessor extends AbstractProcessor {

	public DelinesFieldPatternProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "@DelinesField Checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(DelinesField.class);
//		messager.printMessage(Diagnostic.Kind.NOTE, "total found: " + elements.size());
		boolean success = true;
		for (Element element : elements) {
			if (element.getKind().equals(ElementKind.FIELD)) {
				VariableElement variableElement = (VariableElement) element;
				DelinesField field = variableElement.getAnnotation(DelinesField.class);
				if (field != null) {
					if (CharSequenceUtil.isEmpty(field.regExp())) {
						messager.printMessage(Diagnostic.Kind.ERROR, "@DelinesField without regExp assigned!"
								, element);
					} else {
						success = PatternChecker.check(field.regExp(), messager, element) && success;
					}
				}
			}
		}
		return success;
	}
}
