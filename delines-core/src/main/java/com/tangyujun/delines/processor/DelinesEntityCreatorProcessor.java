package com.tangyujun.delines.processor;

import cn.hutool.core.collection.CollectionUtil;
import com.google.auto.service.AutoService;
import com.tangyujun.delines.annotation.EntityCreator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@SupportedAnnotationTypes({"com.tangyujun.delines.annotation.EntityCreator"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DelinesEntityCreatorProcessor extends AbstractProcessor {

	public DelinesEntityCreatorProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "@EntityCreator Checking");
		Set<? extends Element> elements = env.getElementsAnnotatedWith(EntityCreator.class);
//		messager.printMessage(Diagnostic.Kind.NOTE, "total found: " + elements.size());
		boolean success = true;
		final int value = 1 << Modifier.PUBLIC.ordinal() | 1 << Modifier.STATIC.ordinal();
		for (Element element : elements) {
			if (element.getKind().equals(ElementKind.METHOD)) {
				ExecutableElement executableElement = (ExecutableElement) element;
				// 修饰符校验
				Set<Modifier> modifiers = executableElement.getModifiers();
				boolean wrongMethodDeclared = false;
				if (modifiers != null) {
					final AtomicInteger elementModifierValue = new AtomicInteger();
					modifiers.forEach(modifier -> elementModifierValue.set(elementModifierValue.get() | 1 << modifier.ordinal()));
					wrongMethodDeclared = elementModifierValue.get() != value;
				}
				// 返回值类型校验
				TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
				wrongMethodDeclared = wrongMethodDeclared
						|| !typeElement.getQualifiedName().contentEquals(executableElement.getReturnType().toString());
				// 参数校验
				wrongMethodDeclared = wrongMethodDeclared || CollectionUtil.isNotEmpty(executableElement.getParameters());
				if (wrongMethodDeclared) {
					messager.printMessage(Diagnostic.Kind.ERROR,
							"@EntityCreator required: public static " + typeElement.getSimpleName() + " "
									+ executableElement.getSimpleName() + "() {", element);
				}
			}
		}
		return success;
	}
}
