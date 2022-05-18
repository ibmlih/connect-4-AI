all:
	javac *java

clean:
	find . -type f -name '*.class' -delete
