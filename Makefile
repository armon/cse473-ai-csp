CC=javac
CFLAGS=-d build/ -cp src/
DEPS=Backtrack.class

MAIN: $(DEPS)

run:
	cd build; java Main

%.class: src/%.java
	$(CC) $(CFLAGS) $<

clean:
	rm buiild/*.class

