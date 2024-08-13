build:
	./gradlew clean build

start-development:
	./gradlew bootRun --args='--spring.profiles.active=dev'


jacoco:
	./gradlew jacocoTestReport

.PHONY: build



