#############################################
#											#
# Assignment 2 Makefile						#
#											#
# @author Matthew Poulter (PLTMAT001)		#
# @version 1.0, 24 April 2017				#
#											#
#############################################

# Variables
JAVAC := /usr/bin/javac
JAVA := /usr/bin/java
JAVADOC := /usr/bin/javadoc
TIME := /usr/bin/time
LIB := lib:lib/*
JCA := lib/jacocoagent.jar
SRC := src
BIN := bin
DOC := doc
TEST := test
COV := coverage
SRC_F := $(wildcard src/*.java)
TEST_F := $(wildcard test/*.java)

LIST := $(SRC_F:$(SRC)/%.java=$(BIN)/%.class)

# Default
all: setup $(LIST) complete

# Clean
clean:
	@rm -rf $(BIN)/*.class $(DOC)/* $(TEST)/*.class $(COV) jacoco.exec

# Setup
setup:
	@echo Compiling...
	@mkdir -p $(BIN) $(DOC) $(COV)

# Build
$(BIN)/%.class: $(SRC)/%.java | $(BIN)
	@$(JAVAC) -cp $(BIN) -sourcepath $(SRC) -g -d $| $<

# Complete
complete:
	@echo Complete!
	
# Docs	
docs: all
	@echo Generating docs...
	@$(JAVADOC) -quiet -cp $(BIN) -sourcepath $(SRC) -d $(DOC) $(SRC_F)
	@echo Complete!

# Test
junit: all
	@echo Running tests...
	@$(JAVAC) -cp $(BIN):$(TEST):$(LIB) -sourcepath $(TEST) -g -d $(TEST) $(TEST)/SearchAVLTest.java
	@$(JAVA) -ea -cp $(BIN):$(TEST):$(LIB) org.junit.runner.JUnitCore SearchAVLTest
	@echo Complete!
	
# Coverage
jacoco: junit
	@echo Running coverage...
	@$(JAVA) -ea -javaagent:$(JCA) -cp $(BIN):$(TEST):$(LIB) org.junit.runner.JUnitCore SearchAVLTest
	@$(JAVA) -cp $(BIN):$(TEST):$(LIB) Report --reporttype html --target coverage .
	@echo Complete!

# Run
%.run:
	@$(TIME) --format="Duration: %E" $(JAVA) -cp $(BIN) $(patsubst %.run,%,$@)

### CUSTOM FOR THIS ASSIGNENT ###
%.gen:
	@$(JAVA) -cp $(BIN) GenQuery $(patsubst %.gen,%,$@)
	@echo Generated $(patsubst %.gen,%,$@) queries.