build:
	mvn clean package
deploy:
	mvn clean package deploy
clean:
	${RM} -r target
	${RM} -r delines/target
	${RM} -r delines-checker/target
	${RM} -r delines-core/target
	${RM} -r delines-demo/target
	${RM} -r delines-spring/target
	${RM} -r delines-validation/target
