.PHONY: refresh
refresh:
	./gradlew install
	cd samples
	./gradlew tasks --refresh-dependencies
