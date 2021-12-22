all : clean build run

clean :
	rm -rf bin/*
	
build :
	javac -d "bin" -encoding ISO-8859-1 -cp "lib/*" src/fr/actionrpg3d/*.java src/fr/actionrpg3d/*/*.java src/fr/actionrpg3d/*/*/*.java

run :
	java -cp "bin:lib/*" fr.actionrpg3d.Main
