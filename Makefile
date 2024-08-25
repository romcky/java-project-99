build:
	./gradlew clean build

start-development:
	./gradlew bootRun --args='--spring.profiles.active=development'

start-production:
	./gradlew bootRun --args='--spring.profiles.active=production'


jacoco:
	./gradlew jacocoTestReport

.PHONY: build



