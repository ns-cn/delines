package com.tangyujun.delines.processor;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.auto.service.AutoService;
import com.tangyujun.delines.annotation.DelinesField;
import com.tangyujun.delines.annotation.DelinesNestedField;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({"com.tangyujun.delines.annotation.DelinesNestedField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DelinesNestedFieldProcessor extends AbstractProcessor {
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "@DelinesNestedField Checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(DelinesNestedField.class);
		boolean success = true;
		for (Element element : elements) {
			if (element.getKind().equals(ElementKind.FIELD)) {
				VariableElement variableElement = (VariableElement) element;
				DelinesField field = variableElement.getAnnotation(DelinesField.class);
				if (field != null) {
					messager.printMessage(Diagnostic.Kind.ERROR, "could not @DelinesField when @DelinesNestedField!", element);
					success = false;
				}
				DelinesNestedField nestedField = variableElement.getAnnotation(DelinesNestedField.class);
				if (nestedField != null && CharSequenceUtil.isNotBlank(nestedField.value())) {
					success = PatternChecker.check(nestedField.value(), messager, element) && success;
				}
			}
		}
		return success;
	}
}
