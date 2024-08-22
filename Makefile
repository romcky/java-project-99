build:
	SENTRY_AUTH_TOKEN=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 ./gradlew clean build

start-development:
	./gradlew bootRun --args='--spring.profiles.active=dev'


jacoco:
	./gradlew jacocoTestReport

.PHONY: build



