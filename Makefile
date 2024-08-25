build:
	./gradlew clean build

start-development:
	./gradlew bootRun --args='--spring.profiles.active=dev'

start-production:
	./gradlew bootRun --args='--spring.profiles.active=prod'


jacoco:
	./gradlew jacocoTestReport

.PHONY: build



