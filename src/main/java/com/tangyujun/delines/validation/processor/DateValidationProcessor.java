package com.tangyujun.delines.validation.processor;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.auto.service.AutoService;
import com.tangyujun.delines.validation.annotation.DateFuture;
import com.tangyujun.delines.validation.annotation.DatePast;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SupportedAnnotationTypes({
		"com.tangyujun.delines.validation.annotation.DateFuture",
		"com.tangyujun.delines.validation.annotation.DatePast",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class DateValidationProcessor extends AbstractProcessor {

	public DateValidationProcessor() {
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Date validation annotation Checking");
		Set<? extends Element> dateFutureElements = env.getElementsAnnotatedWith(DateFuture.class);
		Set<? extends Element> datePastElements = env.getElementsAnnotatedWith(DatePast.class);

		Map<Class<?>, BiFunction<String, String, Object>> dateParser = new HashMap<>();
		dateParser.put(Date.class, (value, format) -> {
			try {
				return new SimpleDateFormat(format).parse(value);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		});
		dateParser.put(LocalDateTime.class,
				(value, format) -> LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format)));
		dateParser.put(LocalDate.class,
				(value, format) -> LocalDate.parse(value, DateTimeFormatter.ofPattern(format)));
		dateParser.put(LocalTime.class,
				(value, format) -> LocalTime.parse(value, DateTimeFormatter.ofPattern(format)));

		AtomicBoolean success = new AtomicBoolean(true);
		Consumer<Element> futureConsumer = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				DateFuture dateFuture = element.getAnnotation(DateFuture.class);
				Class<?> type = Optional.ofNullable(element.asType())
						.map(Objects::toString)
						.map(name -> Date.class.getName().equals(name) ? Date.class :
								LocalDateTime.class.getName().equals(name) ? LocalDateTime.class :
										LocalDate.class.getName().equals(name) ? LocalDate.class :
												LocalTime.class.getName().equals(name) ? LocalTime.class : null)
						.orElse(null);
				if (type == null) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR, "@DateFuture only for TIME type", element);
				}
				if (CharSequenceUtil.isNotEmpty(dateFuture.value())) {
					if (CharSequenceUtil.isEmpty(dateFuture.format())) {
						success.set(false);
						messager.printMessage(Diagnostic.Kind.ERROR, "@DateFuture without format", element);
					} else {
						try {
							Optional.ofNullable(dateParser.get(type))
									.map(parser -> parser.apply(dateFuture.value(), dateFuture.format()))
									.orElseThrow(() -> new ParseException(dateFuture.value(), 0));
						} catch (Exception e) {
							success.set(false);
							messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
						}
					}
				}
			}
		};
		dateFutureElements.forEach(futureConsumer);
		Consumer<Element> pastConsumer = element -> {
			if (element.getKind().equals(ElementKind.FIELD)) {
				DatePast datePast = element.getAnnotation(DatePast.class);
				Class<?> type = Optional.ofNullable(element.asType())
						.map(Objects::toString)
						.map(name -> Date.class.getName().equals(name) ? Date.class :
								LocalDateTime.class.getName().equals(name) ? LocalDateTime.class :
										LocalDate.class.getName().equals(name) ? LocalDate.class :
												LocalTime.class.getName().equals(name) ? LocalTime.class : null)
						.orElse(null);
				if (type == null) {
					success.set(false);
					messager.printMessage(Diagnostic.Kind.ERROR, "@DatePast only for TIME type", element);
				}
				if (CharSequenceUtil.isNotEmpty(datePast.value())) {
					if (CharSequenceUtil.isEmpty(datePast.format())) {
						success.set(false);
						messager.printMessage(Diagnostic.Kind.ERROR, "@DatePast without format", element);
					} else {
						try {
							Optional.ofNullable(dateParser.get(type))
									.map(parser -> parser.apply(datePast.value(), datePast.format()))
									.orElseThrow(() -> new ParseException(datePast.value(), 0));
						} catch (Exception e) {
							success.set(false);
							messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
						}
					}
				}
			}
		};
		datePastElements.forEach(pastConsumer);
		return success.get();
	}
}
