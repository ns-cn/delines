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

/**
 * DelinesField注解processor
 */
@SupportedAnnotationTypes({"com.tangyujun.delines.annotation.DelinesField"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DelinesFieldPatternProcessor extends AbstractProcessor {

	/**
	 * DelinesFieldPatternProcessor
	 */
	public DelinesFieldPatternProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "@DelinesField Checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(DelinesField.class);
		boolean success = true;
		for (Element element : elements) {
			if (element.getKind().equals(ElementKind.FIELD)) {
				VariableElement variableElement = (VariableElement) element;
				DelinesField field = variableElement.getAnnotation(DelinesField.class);
				if (field != null) {
					if (CharSequenceUtil.isEmpty(field.value())) {
						messager.printMessage(Diagnostic.Kind.ERROR, "@DelinesField without regExp assigned!"
								, element);
					} else {
						success = PatternChecker.check(field.value(), messager, element) && success;
					}
				}
			}
		}
		return success;
	}
}
